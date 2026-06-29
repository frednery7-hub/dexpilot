package com.dexpilot.infrastructure.dex

import com.dexpilot.domain.dex.model.DexStringSummary
import com.dexpilot.domain.dex.model.DexTypeData
import com.dexpilot.domain.dex.model.DexTypeSummary
import java.nio.ByteBuffer
import java.nio.ByteOrder

class DexTypeIdsParser {
    fun parse(
        bytes: ByteArray,
        typeIdsSize: Long,
        typeIdsOff: Long,
        stringIdsSize: Long,
        stringSummary: DexStringSummary?
    ): DexTypeSummary? {
        if (typeIdsSize == 0L) return null

        if (typeIdsOff <= 0 || typeIdsOff > bytes.size - TYPE_ID_ITEM_SIZE_BYTES) {
            throw DexParseException(
                DexParseErrorCode.INVALID_TYPE_IDS,
                "type_ids_off $typeIdsOff does not point to a readable type_id_item."
            )
        }

        if (typeIdsSize > MAX_REASONABLE_TYPE_IDS) {
            throw DexParseException(
                DexParseErrorCode.INVALID_TYPE_IDS,
                "type_ids_size $typeIdsSize exceeds safety limit."
            )
        }

        val endExclusive = typeIdsOff + (typeIdsSize * TYPE_ID_ITEM_SIZE_BYTES)

        if (endExclusive > bytes.size || endExclusive < typeIdsOff) {
            throw DexParseException(
                DexParseErrorCode.INVALID_TYPE_IDS,
                "type_ids range [$typeIdsOff, $endExclusive) exceeds file size ${bytes.size}."
            )
        }

        val buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN)
        val sampleCount = minOf(typeIdsSize, MAX_TYPE_SAMPLE_COUNT).toInt()

        val sample = buildList {
            repeat(sampleCount) { index ->
                val typeIdOffset = typeIdsOff.toInt() + (index * TYPE_ID_ITEM_SIZE_BYTES.toInt())
                buffer.position(typeIdOffset)

                val descriptorIndex = Integer.toUnsignedLong(buffer.int)

                if (descriptorIndex >= stringIdsSize) {
                    throw DexParseException(
                        DexParseErrorCode.INVALID_TYPE_IDS,
                        "type_id descriptor_idx $descriptorIndex is outside string_ids_size $stringIdsSize."
                    )
                }

                add(
                    DexTypeData(
                        index = index,
                        descriptorIndex = descriptorIndex,
                        descriptor = resolveDescriptor(descriptorIndex, stringSummary)
                    )
                )
            }
        }

        return DexTypeSummary(
            declaredCount = typeIdsSize,
            sample = sample
        )
    }

    private fun resolveDescriptor(
        descriptorIndex: Long,
        stringSummary: DexStringSummary?
    ): String {
        val resolved = stringSummary
            ?.sample
            ?.firstOrNull { it.index == descriptorIndex.toInt() }
            ?.value

        return resolved ?: "<string_not_sampled:$descriptorIndex>"
    }

    private companion object {
        const val TYPE_ID_ITEM_SIZE_BYTES: Long = 4
        const val MAX_REASONABLE_TYPE_IDS: Long = 1_000_000
        const val MAX_TYPE_SAMPLE_COUNT: Long = 10
    }
}
