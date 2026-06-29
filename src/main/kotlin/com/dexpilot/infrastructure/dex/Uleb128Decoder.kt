package com.dexpilot.infrastructure.dex

data class Uleb128Result(
    val value: Long,
    val nextOffset: Int
)

class Uleb128Decoder {
    fun decode(bytes: ByteArray, offset: Int): Uleb128Result {
        if (offset < 0 || offset >= bytes.size) {
            throw DexParseException(
                DexParseErrorCode.INVALID_STRING_IDS,
                "ULEB128 offset $offset is outside file bounds."
            )
        }

        var result = 0L
        var shift = 0
        var cursor = offset

        repeat(MAX_ULEB128_BYTES) {
            if (cursor >= bytes.size) {
                throw DexParseException(
                    DexParseErrorCode.INVALID_STRING_IDS,
                    "ULEB128 value exceeded file bounds."
                )
            }

            val current = bytes[cursor].toInt() and 0xff
            result = result or ((current and 0x7f).toLong() shl shift)
            cursor += 1

            if ((current and 0x80) == 0) {
                return Uleb128Result(
                    value = result,
                    nextOffset = cursor
                )
            }

            shift += 7
        }

        throw DexParseException(
            DexParseErrorCode.INVALID_STRING_IDS,
            "ULEB128 value exceeds safety limit of $MAX_ULEB128_BYTES bytes."
        )
    }

    private companion object {
        const val MAX_ULEB128_BYTES = 5
    }
}
