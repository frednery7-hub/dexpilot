package com.dexpilot.domain.dex.validation

enum class DexValidationStatus {
    VALID_DEX,
    INVALID_DEX
}

data class DexValidationResult(
    val status: DexValidationStatus,
    val errors: List<DexValidationError>
) {
    val isValid: Boolean
        get() = status == DexValidationStatus.VALID_DEX

    companion object {
        fun valid(): DexValidationResult =
            DexValidationResult(DexValidationStatus.VALID_DEX, emptyList())

        fun invalid(errors: List<DexValidationError>): DexValidationResult =
            DexValidationResult(DexValidationStatus.INVALID_DEX, errors)
    }
}
