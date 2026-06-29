package com.dexpilot.infrastructure.dex

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class Uleb128DecoderTest {
    private val decoder = Uleb128Decoder()

    @Test
    fun `decodes one byte value`() {
        val result = decoder.decode(byteArrayOf(0x05), offset = 0)

        result.value shouldBe 5L
        result.nextOffset shouldBe 1
    }

    @Test
    fun `decodes multi byte value`() {
        val result = decoder.decode(byteArrayOf(0x80.toByte(), 0x01), offset = 0)

        result.value shouldBe 128L
        result.nextOffset shouldBe 2
    }

    @Test
    fun `rejects unbounded value`() {
        val exception = assertThrows<DexParseException> {
            decoder.decode(byteArrayOf(0x80.toByte(), 0x80.toByte(), 0x80.toByte(), 0x80.toByte(), 0x80.toByte(), 0x01), offset = 0)
        }

        exception.code shouldBe DexParseErrorCode.INVALID_STRING_IDS
    }
}
