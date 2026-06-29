package com.dexpilot.infrastructure.dex

enum class DexParseErrorCode {
    FILE_TOO_SMALL,
    INVALID_MAP_LIST,
    INVALID_STRING_IDS,
    INVALID_TYPE_IDS
}

class DexParseException(
    val code: DexParseErrorCode,
    message: String,
    cause: Throwable? = null
) : RuntimeException(message, cause)
