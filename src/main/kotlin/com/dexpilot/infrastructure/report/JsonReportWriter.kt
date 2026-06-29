package com.dexpilot.infrastructure.report

import com.dexpilot.application.usecase.DexFileSummary

class JsonReportWriter {
    fun write(summary: DexFileSummary): String {
        val header = summary.header
        val validation = summary.validation

        return buildString {
            appendLine("{")
            appendLine("  \"tool\": \"DexPilot\",")
            appendLine("  \"formatVersion\": \"1.0\",")
            appendLine("  \"path\": ${ReportEscaper.jsonString(summary.path)},")
            appendLine("  \"actualFileSize\": ${summary.actualFileSize},")
            appendLine("  \"status\": ${ReportEscaper.jsonString(validation.status.name)},")
            appendLine("  \"header\": {")
            appendLine("    \"magic\": ${ReportEscaper.jsonString(ReportEscaper.displayMagic(header.magicText))},")
            appendLine("    \"version\": ${ReportEscaper.jsonString(header.version)},")
            appendLine("    \"checksum\": ${header.checksum},")
            appendLine("    \"signatureHex\": ${ReportEscaper.jsonString(header.signatureHex)},")
            appendLine("    \"fileSize\": ${header.fileSize},")
            appendLine("    \"headerSize\": ${header.headerSize},")
            appendLine("    \"endianTag\": ${header.endianTag},")
            appendLine("    \"linkSize\": ${header.linkSize},")
            appendLine("    \"linkOff\": ${header.linkOff},")
            appendLine("    \"mapOff\": ${header.mapOff},")
            appendLine("    \"stringIdsSize\": ${header.stringIdsSize},")
            appendLine("    \"stringIdsOff\": ${header.stringIdsOff},")
            appendLine("    \"typeIdsSize\": ${header.typeIdsSize},")
            appendLine("    \"typeIdsOff\": ${header.typeIdsOff},")
            appendLine("    \"protoIdsSize\": ${header.protoIdsSize},")
            appendLine("    \"protoIdsOff\": ${header.protoIdsOff},")
            appendLine("    \"fieldIdsSize\": ${header.fieldIdsSize},")
            appendLine("    \"fieldIdsOff\": ${header.fieldIdsOff},")
            appendLine("    \"methodIdsSize\": ${header.methodIdsSize},")
            appendLine("    \"methodIdsOff\": ${header.methodIdsOff},")
            appendLine("    \"classDefsSize\": ${header.classDefsSize},")
            appendLine("    \"classDefsOff\": ${header.classDefsOff},")
            appendLine("    \"dataSize\": ${header.dataSize},")
            appendLine("    \"dataOff\": ${header.dataOff}")
            appendLine("  },")
            appendLine("  \"validationErrors\": [")
            validation.errors.forEachIndexed { index, error ->
                val suffix = if (index == validation.errors.lastIndex) "" else ","
                appendLine("    { \"code\": ${ReportEscaper.jsonString(error.code.name)}, \"message\": ${ReportEscaper.jsonString(error.message)} }$suffix")
            }
            appendLine("  ]")
            appendLine("}")
        }
    }
}
