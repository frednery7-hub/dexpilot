package com.dexpilot.infrastructure.report

import com.dexpilot.application.usecase.DexFileSummary

class TextReportWriter {
    fun write(summary: DexFileSummary): String {
        val header = summary.header
        val validation = summary.validation

        return buildString {
            appendLine("DexPilot — DEX Inspector")
            appendLine()
            appendLine("File: ${summary.path}")
            appendLine("Status: ${validation.status}")
            appendLine()
            appendLine("Magic: ${ReportEscaper.displayMagic(header.magicText)}")
            appendLine("Version: ${header.version}")
            appendLine("File size declared: ${header.fileSize} bytes")
            appendLine("File size actual: ${summary.actualFileSize} bytes")
            appendLine("Header size: ${header.headerSize} bytes")
            appendLine("Endian tag: 0x${header.endianTag.toString(16)}")
            appendLine()
            appendLine("String IDs: ${header.stringIdsSize}")
            appendLine("Type IDs: ${header.typeIdsSize}")
            appendLine("Proto IDs: ${header.protoIdsSize}")
            appendLine("Field IDs: ${header.fieldIdsSize}")
            appendLine("Method IDs: ${header.methodIdsSize}")
            appendLine("Class Definitions: ${header.classDefsSize}")

            summary.mapList?.let { mapList ->
                appendLine()
                appendLine("Map List:")
                appendLine("Declared items: ${mapList.declaredSize}")
                mapList.items.forEach { item ->
                    appendLine("- ${item.typeName}: size=${item.size}, offset=${item.offset}")
                }
            }

            summary.stringSummary?.let { stringSummary ->
                appendLine()
                appendLine("Strings:")
                appendLine("Declared strings: ${stringSummary.declaredCount}")
                appendLine("Sample size: ${stringSummary.sample.size}")
                stringSummary.sample.forEach { item ->
                    appendLine("- #${item.index} @${item.offset}: ${item.value}")
                }
            }

            if (validation.errors.isNotEmpty()) {
                appendLine()
                appendLine("Validation errors:")
                validation.errors.forEach { error ->
                    appendLine("- ${error.code}: ${error.message}")
                }
            }
        }
    }
}
