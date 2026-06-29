package com.dexpilot.domain.dex.validation

import com.dexpilot.domain.dex.model.DexHeader

class DexHeaderValidator {
    fun validate(header: DexHeader, actualFileSize: Long): DexValidationResult {
        val errors = mutableListOf<DexValidationError>()

        validateMagic(header, errors)
        validateVersion(header, errors)
        validateFileSize(header, actualFileSize, errors)
        validateHeaderSize(header, errors)
        validateEndianTag(header, errors)
        validateTableRanges(header, actualFileSize, errors)

        return if (errors.isEmpty()) {
            DexValidationResult.valid()
        } else {
            DexValidationResult.invalid(errors)
        }
    }

    private fun validateMagic(header: DexHeader, errors: MutableList<DexValidationError>) {
        val hasValidPrefix = header.magicText.startsWith("dex\n")
        val hasVersion = header.version.length == 3

        if (!hasValidPrefix || !hasVersion) {
            errors += DexValidationError(
                DexValidationCode.INVALID_MAGIC,
                "DEX magic must start with dex followed by newline and contain a three-digit version."
            )
        }
    }

    private fun validateVersion(header: DexHeader, errors: MutableList<DexValidationError>) {
        when {
            DexVersionPolicy.isSupported(header.version) -> Unit
            DexVersionPolicy.isRecognizedButUnsupported(header.version) -> {
                errors += DexValidationError(
                    DexValidationCode.UNSUPPORTED_EXPERIMENTAL_DEX_VERSION,
                    "DEX version ${header.version} is recognized but unsupported in MVP 1."
                )
            }
            else -> {
                errors += DexValidationError(
                    DexValidationCode.UNSUPPORTED_DEX_VERSION,
                    "DEX version ${header.version} is not supported in MVP 1."
                )
            }
        }
    }

    private fun validateFileSize(
        header: DexHeader,
        actualFileSize: Long,
        errors: MutableList<DexValidationError>
    ) {
        if (header.fileSize != actualFileSize) {
            errors += DexValidationError(
                DexValidationCode.INVALID_FILE_SIZE,
                "Declared file_size ${header.fileSize} does not match actual size $actualFileSize."
            )
        }
    }

    private fun validateHeaderSize(header: DexHeader, errors: MutableList<DexValidationError>) {
        if (DexVersionPolicy.isSupported(header.version) && header.headerSize != DexHeader.STANDARD_HEADER_SIZE_BYTES) {
            errors += DexValidationError(
                DexValidationCode.INVALID_HEADER_SIZE,
                "header_size must be exactly 0x70 / 112 bytes for supported DEX versions."
            )
        }
    }

    private fun validateEndianTag(header: DexHeader, errors: MutableList<DexValidationError>) {
        if (header.endianTag != DexHeader.STANDARD_ENDIAN_CONSTANT) {
            errors += DexValidationError(
                DexValidationCode.INVALID_ENDIAN_TAG,
                "endian_tag must be 0x12345678 in MVP 1."
            )
        }
    }

    private fun validateTableRanges(
        header: DexHeader,
        actualFileSize: Long,
        errors: MutableList<DexValidationError>
    ) {
        validateRange("string_ids", header.stringIdsSize, header.stringIdsOff, 4, actualFileSize, errors)
        validateRange("type_ids", header.typeIdsSize, header.typeIdsOff, 4, actualFileSize, errors)
        validateRange("proto_ids", header.protoIdsSize, header.protoIdsOff, 12, actualFileSize, errors)
        validateRange("field_ids", header.fieldIdsSize, header.fieldIdsOff, 8, actualFileSize, errors)
        validateRange("method_ids", header.methodIdsSize, header.methodIdsOff, 8, actualFileSize, errors)
        validateRange("class_defs", header.classDefsSize, header.classDefsOff, 32, actualFileSize, errors)
    }

    private fun validateRange(
        name: String,
        size: Long,
        offset: Long,
        itemSize: Long,
        actualFileSize: Long,
        errors: MutableList<DexValidationError>
    ) {
        if (size == 0L) return

        if (offset == 0L || offset > actualFileSize) {
            errors += DexValidationError(
                DexValidationCode.INVALID_OFFSET_RANGE,
                "$name offset $offset is outside the file."
            )
            return
        }

        val byteCount = size * itemSize
        val endExclusive = offset + byteCount

        if (endExclusive > actualFileSize || endExclusive < offset) {
            errors += DexValidationError(
                DexValidationCode.INVALID_OFFSET_RANGE,
                "$name range [$offset, $endExclusive) exceeds file size $actualFileSize."
            )
        }
    }
}
