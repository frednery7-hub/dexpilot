package com.dexpilot.domain.dex.validation

data class DexValidationError(
    val code: DexValidationCode,
    val message: String
)
