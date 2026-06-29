package com.dexpilot.infrastructure.dex

import com.dexpilot.domain.dex.model.DexStringData
import com.dexpilot.domain.dex.model.DexStringSummary
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.charset.StandardCharsets

class DexStringIdsParser(
    private val uleb128Decoder: Uleb128Decoder = Uleb128Decoder()
) {
    fun parse(
        bytes: ByteArray,
        stringIdsSize: Long,
        stringIdsOff: Long
    ): DexStringSummary? {
        if (stringIdsSize == 0L) return null

        if (stringIdsOff <= 0 || stringIdsOff > bytes.size - STRING_ID_ITEM_SIZE_BYTES) {
            throw DexParseException(
                DexParseErrorCode.INVALID_STRING_IDS,
                "string_ids_off $stringIdsOff does not point to a readable string_id_item."
            )
        }

        if (stringIdsSize > MAX_REASONABLE_STRING_IDS) {
            throw DexParseException(
                DexParseErrorCode.INVALID_STRING_IDS,
                "string_ids_size $stringIdsSize exceeds safety limit."
            )
        }

        val endExclusive = stringIdsOff + (stringIdsSize * STRING_ID_ITEM_SIZE_BYTES)

        if (endExclusive > bytes.size || endExclusive < stringIdsOff) {
            throw DexParseException(
                DexParseErrorCode.INVALID_STRING_IDS,
                "string_ids range [$stringIdsOff, $endExclusive) exceeds file size ${bytes.size}."
            )
        }

        val buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN)
        val sampleCount = minOf(stringIdsSize, MAX_STRING_SAMPLE_COUNT).toInt()

        val sample = buildList {
            repeat(sampleCount) { index ->
                val stringIdOffset = stringIdsOff.toInt() + (index * STRING_ID_ITEM_SIZE_BYTES.toInt())
                buffer.position(stringIdOffset)

                val stringDataOff = Integer.toUnsignedLong(buffer.int)

                add(readStringData(bytes, index, stringDataOff))
            }
        }

        return DexStringSummary(
            declaredCount = stringIdsSize,
            sample = sample
        )
    }

    private fun readStringData(
        bytes: ByteArray,
        index: Int,
        stringDataOff: Long
    ): DexStringData {
        if (stringDataOff < 0 || stringDataOff >= bytes.size) {
            throw DexParseException(
                DexParseErrorCode.INVALID_STRING_IDS,
                "string_data_off $stringDataOff is outside file bounds."
            )
        }

        val size = uleb128Decoder.decode(bytes, stringDataOff.toInt())
        val valueStart = size.nextOffset
        val valueEnd = findNullTerminator(bytes, valueStart)

        val raw = bytes.copyOfRange(valueStart, valueEnd)
        val decoded = String(raw, StandardCharsets.UTF_8)
        val safeValue = decoded
            .replace("\\", "\\\\")
            .replace("\n", "\\n")
            .replace("\r", "\\r")
            .replace("\t", "\\t")
            .let { value ->
                if (value.length <= MAX_STRING_DISPLAY_LENGTH) {
                    value
                } else {
                    value.take(MAX_STRING_DISPLAY_LENGTH) + "..."
                }
            }

        return DexStringData(
            index = index,
            offset = stringDataOff,
            declaredUtf16Size = size.value,
            value = safeValue
        )
    }

    private fun findNullTerminator(bytes: ByteArray, start: Int): Int {
        var cursor = start
        val maxEnd = minOf(bytes.size, start + MAX_STRING_RAW_BYTES)

        while (cursor < maxEnd) {
            if (bytes[cursor].toInt() == 0) {
                return cursor
            }
            cursor += 1
        }

        throw DexParseException(
            DexParseErrorCode.INVALID_STRING_IDS,
            "string_data_item does not contain a null terminator within safety limit."
        )
    }

    private companion object {
        const val STRING_ID_ITEM_SIZE_BYTES: Long = 4
        const val MAX_REASONABLE_STRING_IDS: Long = 1_000_000
        const val MAX_STRING_SAMPLE_COUNT: Long = 10
        const val MAX_STRING_DISPLAY_LENGTH = 120
        const val MAX_STRING_RAW_BYTES = 4096
    }
}
