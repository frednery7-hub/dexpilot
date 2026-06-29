package com.dexpilot.infrastructure.dex

enum class DexParseErrorCode {
    FILE_TOO_SMALL
}

class DexParseException(
    val code: DexParseErrorCode,
    message: String,
    cause: Throwable? = null
) : RuntimeException(message, cause)
