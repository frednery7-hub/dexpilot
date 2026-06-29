package com.dexpilot.domain.dex.validation

import com.dexpilot.domain.dex.model.DexHeader
import com.dexpilot.infrastructure.dex.DexHeaderParser
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.nio.ByteBuffer
import java.nio.ByteOrder

class DexHeaderValidatorTest {
    private val parser = DexHeaderParser()
    private val validator = DexHeaderValidator()

    @Test
    fun `accepts supported DEX version with valid standard header`() {
        val bytes = validDexHeader(version = "035", fileSize = 112)
        val header = parser.parse(bytes)

        val result = validator.validate(header, actualFileSize = 112)

        result.isValid shouldBe true
        result.errors shouldBe emptyList()
    }

    @Test
    fun `rejects recognized experimental DEX version 041`() {
        val bytes = validDexHeader(version = "041", fileSize = 112)
        val header = parser.parse(bytes)

        val result = validator.validate(header, actualFileSize = 112)

        result.isValid shouldBe false
        result.errors.map { it.code } shouldContain DexValidationCode.UNSUPPORTED_EXPERIMENTAL_DEX_VERSION
    }

    @Test
    fun `rejects invalid declared file size`() {
        val bytes = validDexHeader(version = "040", fileSize = 999)
        val header = parser.parse(bytes)

        val result = validator.validate(header, actualFileSize = 112)

        result.isValid shouldBe false
        result.errors.map { it.code } shouldContain DexValidationCode.INVALID_FILE_SIZE
    }

    @Test
    fun `rejects invalid header size for supported versions`() {
        val bytes = validDexHeader(version = "040", fileSize = 112, headerSize = 120)
        val header = parser.parse(bytes)

        val result = validator.validate(header, actualFileSize = 112)

        result.isValid shouldBe false
        result.errors.map { it.code } shouldContain DexValidationCode.INVALID_HEADER_SIZE
    }

    private fun validDexHeader(
        version: String,
        fileSize: Int,
        headerSize: Int = DexHeader.STANDARD_HEADER_SIZE_BYTES.toInt()
    ): ByteArray {
        val bytes = ByteArray(DexHeader.STANDARD_HEADER_SIZE_BYTES.toInt())
        val buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN)

        buffer.put("dex\n$version\u0000".toByteArray(Charsets.US_ASCII))
        buffer.putInt(0)
        buffer.put(ByteArray(20))
        buffer.putInt(fileSize)
        buffer.putInt(headerSize)
        buffer.putInt(DexHeader.STANDARD_ENDIAN_CONSTANT.toInt())

        return bytes
    }
}
