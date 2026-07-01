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
            appendLine("  \"mapList\": ${writeMapList(summary)},")
            appendLine("  \"stringSummary\": ${writeStringSummary(summary)},")
            appendLine("  \"typeSummary\": ${writeTypeSummary(summary)},")
            appendLine("  \"protoSummary\": ${writeProtoSummary(summary)},")
            appendLine("  \"validationErrors\": [")
            validation.errors.forEachIndexed { index, error ->
                val suffix = if (index == validation.errors.lastIndex) "" else ","
                appendLine("    { \"code\": ${ReportEscaper.jsonString(error.code.name)}, \"message\": ${ReportEscaper.jsonString(error.message)} }$suffix")
            }
            appendLine("  ]")
            appendLine("}")
        }
    }

    private fun writeMapList(summary: DexFileSummary): String {
        val mapList = summary.mapList ?: return "null"
        return buildString {
            appendLine("{")
            appendLine("    \"declaredSize\": ${mapList.declaredSize},")
            appendLine("    \"items\": [")
            mapList.items.forEachIndexed { index, item ->
                val suffix = if (index == mapList.items.lastIndex) "" else ","
                appendLine("      { \"typeCode\": ${item.typeCode}, \"typeName\": ${ReportEscaper.jsonString(item.typeName)}, \"size\": ${item.size}, \"offset\": ${item.offset} }$suffix")
            }
            append("    ]\n  }")
        }
    }

    private fun writeStringSummary(summary: DexFileSummary): String {
        val stringSummary = summary.stringSummary ?: return "null"
        return buildString {
            appendLine("{")
            appendLine("    \"declaredCount\": ${stringSummary.declaredCount},")
            appendLine("    \"sample\": [")
            stringSummary.sample.forEachIndexed { index, item ->
                val suffix = if (index == stringSummary.sample.lastIndex) "" else ","
                appendLine("      { \"index\": ${item.index}, \"offset\": ${item.offset}, \"declaredUtf16Size\": ${item.declaredUtf16Size}, \"value\": ${ReportEscaper.jsonString(item.value)} }$suffix")
            }
            append("    ]\n  }")
        }
    }

    private fun writeTypeSummary(summary: DexFileSummary): String {
        val typeSummary = summary.typeSummary ?: return "null"
        return buildString {
            appendLine("{")
            appendLine("    \"declaredCount\": ${typeSummary.declaredCount},")
            appendLine("    \"sample\": [")
            typeSummary.sample.forEachIndexed { index, item ->
                val suffix = if (index == typeSummary.sample.lastIndex) "" else ","
                appendLine("      { \"index\": ${item.index}, \"descriptorIndex\": ${item.descriptorIndex}, \"descriptor\": ${ReportEscaper.jsonString(item.descriptor)} }$suffix")
            }
            append("    ]\n  }")
        }
    }

    private fun writeProtoSummary(summary: DexFileSummary): String {
        val protoSummary = summary.protoSummary ?: return "null"
        return buildString {
            appendLine("{")
            appendLine("    \"declaredCount\": ${protoSummary.declaredCount},")
            appendLine("    \"sample\": [")
            protoSummary.sample.forEachIndexed { index, item ->
                val suffix = if (index == protoSummary.sample.lastIndex) "" else ","
                appendLine("      { \"index\": ${item.index}, \"shortyIndex\": ${item.shortyIndex}, \"shortyDescriptor\": ${ReportEscaper.jsonString(item.shortyDescriptor ?: "")}, \"returnTypeIndex\": ${item.returnTypeIndex}, \"returnTypeDescriptor\": ${ReportEscaper.jsonString(item.returnTypeDescriptor ?: "")}, \"parametersOffset\": ${item.parametersOffset} }$suffix")
            }
            append("    ]\n  }")
        }
    }
}
