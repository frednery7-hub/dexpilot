package com.dexpilot.infrastructure.dex

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.nio.ByteBuffer
import java.nio.ByteOrder

class DexMapListParserTest {
    private val parser = DexMapListParser()

    @Test
    fun `returns null when map offset is zero`() {
        parser.parse(ByteArray(112), mapOff = 0) shouldBe null
    }

    @Test
    fun `parses map list with known item types`() {
        val bytes = ByteArray(160)
        val buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN)
        buffer.position(112)
        buffer.putInt(2)

        buffer.putShort(0x0000)
        buffer.putShort(0)
        buffer.putInt(1)
        buffer.putInt(0)

        buffer.putShort(0x1000)
        buffer.putShort(0)
        buffer.putInt(1)
        buffer.putInt(112)

        val mapList = parser.parse(bytes, mapOff = 112)

        mapList?.declaredSize shouldBe 2L
        mapList?.items?.size shouldBe 2
        mapList?.items?.get(0)?.typeName shouldBe "header_item"
        mapList?.items?.get(1)?.typeName shouldBe "map_list"
    }

    @Test
    fun `rejects map offset outside file bounds`() {
        val exception = assertThrows<DexParseException> {
            parser.parse(ByteArray(112), mapOff = 111)
        }

        exception.code shouldBe DexParseErrorCode.INVALID_MAP_LIST
    }
}
