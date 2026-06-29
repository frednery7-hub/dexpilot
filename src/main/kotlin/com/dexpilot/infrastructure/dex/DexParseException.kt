package com.dexpilot.infrastructure.dex

enum class DexParseErrorCode {
    FILE_TOO_SMALL,
    INVALID_MAP_LIST
}

class DexParseException(
    val code: DexParseErrorCode,
    message: String,
    cause: Throwable? = null
) : RuntimeException(message, cause)
