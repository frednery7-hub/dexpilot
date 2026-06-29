package com.dexpilot.infrastructure.dex

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.nio.ByteBuffer
import java.nio.ByteOrder

class DexStringIdsParserTest {
    private val parser = DexStringIdsParser()

    @Test
    fun `returns null when there are no string ids`() {
        parser.parse(ByteArray(112), stringIdsSize = 0, stringIdsOff = 0) shouldBe null
    }

    @Test
    fun `parses string id and string data item`() {
        val bytes = ByteArray(140)
        val buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN)

        buffer.position(112)
        buffer.putInt(120)

        bytes[120] = 0x05
        "hello".encodeToByteArray().copyInto(bytes, destinationOffset = 121)
        bytes[126] = 0

        val summary = parser.parse(bytes, stringIdsSize = 1, stringIdsOff = 112)

        summary?.declaredCount shouldBe 1L
        summary?.sample?.size shouldBe 1
        summary?.sample?.get(0)?.index shouldBe 0
        summary?.sample?.get(0)?.offset shouldBe 120L
        summary?.sample?.get(0)?.declaredUtf16Size shouldBe 5L
        summary?.sample?.get(0)?.value shouldBe "hello"
    }

    @Test
    fun `rejects string ids outside file bounds`() {
        val exception = assertThrows<DexParseException> {
            parser.parse(ByteArray(112), stringIdsSize = 1, stringIdsOff = 111)
        }

        exception.code shouldBe DexParseErrorCode.INVALID_STRING_IDS
    }
}
