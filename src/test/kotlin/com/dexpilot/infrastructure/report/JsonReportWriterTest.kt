package com.dexpilot.infrastructure.report

import com.dexpilot.application.usecase.DexFileSummary
import com.dexpilot.domain.dex.model.DexHeader
import com.dexpilot.domain.dex.validation.DexValidationResult
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.Test

class JsonReportWriterTest {
    private val writer = JsonReportWriter()

    @Test
    fun `writes JSON report with escaped magic and validation status`() {
        val summary = DexFileSummary(
            path = "src/test/resources/fixtures/valid/minimal-header.dex",
            actualFileSize = 112,
            header = DexHeader(
                magicText = "dex\n040\u0000",
                version = "040",
                checksum = 0,
                signatureHex = "0000000000000000000000000000000000000000",
                fileSize = 112,
                headerSize = 112,
                endianTag = DexHeader.STANDARD_ENDIAN_CONSTANT,
                linkSize = 0,
                linkOff = 0,
                mapOff = 0,
                stringIdsSize = 0,
                stringIdsOff = 0,
                typeIdsSize = 0,
                typeIdsOff = 0,
                protoIdsSize = 0,
                protoIdsOff = 0,
                fieldIdsSize = 0,
                fieldIdsOff = 0,
                methodIdsSize = 0,
                methodIdsOff = 0,
                classDefsSize = 0,
                classDefsOff = 0,
                dataSize = 0,
                dataOff = 0
            ),
            validation = DexValidationResult.valid()
        )

        val json = writer.write(summary)

        json shouldContain "\"tool\": \"DexPilot\""
        json shouldContain "\"status\": \"VALID_DEX\""
        json shouldContain "\"magic\": \"dex\\\\n040\\\\0\""
        json shouldContain "\"validationErrors\": ["
    }
}
