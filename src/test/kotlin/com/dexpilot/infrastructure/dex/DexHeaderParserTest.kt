package com.dexpilot.infrastructure.dex

import com.dexpilot.domain.dex.model.DexHeader
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.nio.ByteBuffer
import java.nio.ByteOrder

class DexHeaderParserTest {
    private val parser = DexHeaderParser()

    @Test
    fun `parses standard DEX header fields`() {
        val bytes = validDexHeader(version = "040", fileSize = 112)

        val header = parser.parse(bytes)

        header.version shouldBe "040"
        header.fileSize shouldBe 112L
        header.headerSize shouldBe DexHeader.STANDARD_HEADER_SIZE_BYTES
        header.endianTag shouldBe DexHeader.STANDARD_ENDIAN_CONSTANT
    }

    @Test
    fun `throws parse exception when file is smaller than DEX header`() {
        val bytes = ByteArray(32)

        val exception = assertThrows<DexParseException> {
            parser.parse(bytes)
        }

        exception.code shouldBe DexParseErrorCode.FILE_TOO_SMALL
    }

    private fun validDexHeader(version: String, fileSize: Int): ByteArray {
        val bytes = ByteArray(DexHeader.STANDARD_HEADER_SIZE_BYTES.toInt())
        val buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN)

        buffer.put("dex\n$version\u0000".toByteArray(Charsets.US_ASCII))
        buffer.putInt(0)
        buffer.put(ByteArray(20))
        buffer.putInt(fileSize)
        buffer.putInt(DexHeader.STANDARD_HEADER_SIZE_BYTES.toInt())
        buffer.putInt(DexHeader.STANDARD_ENDIAN_CONSTANT.toInt())

        return bytes
    }
}
