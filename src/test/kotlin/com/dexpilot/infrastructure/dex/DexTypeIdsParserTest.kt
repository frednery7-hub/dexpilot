package com.dexpilot.infrastructure.dex

import com.dexpilot.domain.dex.model.DexStringData
import com.dexpilot.domain.dex.model.DexStringSummary
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.nio.ByteBuffer
import java.nio.ByteOrder

class DexTypeIdsParserTest {
    private val parser = DexTypeIdsParser()

    @Test
    fun `returns null when there are no type ids`() {
        parser.parse(
            bytes = ByteArray(112),
            typeIdsSize = 0,
            typeIdsOff = 0,
            stringIdsSize = 1,
            stringSummary = stringSummary()
        ) shouldBe null
    }

    @Test
    fun `parses type id and resolves descriptor through string summary`() {
        val bytes = ByteArray(140)
        val buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN)

        buffer.position(112)
        buffer.putInt(0)

        val summary = parser.parse(
            bytes = bytes,
            typeIdsSize = 1,
            typeIdsOff = 112,
            stringIdsSize = 1,
            stringSummary = stringSummary()
        )

        summary?.declaredCount shouldBe 1L
        summary?.sample?.size shouldBe 1
        summary?.sample?.get(0)?.index shouldBe 0
        summary?.sample?.get(0)?.descriptorIndex shouldBe 0L
        summary?.sample?.get(0)?.descriptor shouldBe "Ljava/lang/String;"
    }

    @Test
    fun `rejects descriptor index outside string ids size`() {
        val bytes = ByteArray(140)
        val buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN)

        buffer.position(112)
        buffer.putInt(1)

        val exception = assertThrows<DexParseException> {
            parser.parse(
                bytes = bytes,
                typeIdsSize = 1,
                typeIdsOff = 112,
                stringIdsSize = 1,
                stringSummary = stringSummary()
            )
        }

        exception.code shouldBe DexParseErrorCode.INVALID_TYPE_IDS
    }

    @Test
    fun `rejects type ids outside file bounds`() {
        val exception = assertThrows<DexParseException> {
            parser.parse(
                bytes = ByteArray(112),
                typeIdsSize = 1,
                typeIdsOff = 111,
                stringIdsSize = 1,
                stringSummary = stringSummary()
            )
        }

        exception.code shouldBe DexParseErrorCode.INVALID_TYPE_IDS
    }

    private fun stringSummary(): DexStringSummary =
        DexStringSummary(
            declaredCount = 1,
            sample = listOf(
                DexStringData(
                    index = 0,
                    offset = 120,
                    declaredUtf16Size = 18,
                    value = "Ljava/lang/String;"
                )
            )
        )
}
