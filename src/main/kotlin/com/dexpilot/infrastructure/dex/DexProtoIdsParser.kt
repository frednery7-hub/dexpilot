package com.dexpilot.infrastructure.dex

import com.dexpilot.domain.dex.model.DexHeader
import com.dexpilot.domain.dex.model.DexProtoData
import com.dexpilot.domain.dex.model.DexProtoSummary
import com.dexpilot.domain.dex.model.DexStringSummary
import com.dexpilot.domain.dex.model.DexTypeSummary
import com.dexpilot.domain.dex.validation.DexValidationCode
import java.nio.ByteBuffer
import java.nio.ByteOrder

private const val PROTO_ID_ITEM_SIZE = 12
private const val SAMPLE_LIMIT = 10

class DexProtoIdsParser {

    fun parse(
        bytes: ByteArray,
        header: DexHeader,
        stringSummary: DexStringSummary?,
        typeSummary: DexTypeSummary?,
    ): DexProtoSummary {
        val declaredCount = header.protoIdsSize.toInt()

        if (declaredCount == 0) {
            return DexProtoSummary(declaredCount = 0, sample = emptyList())
        }

        val off = header.protoIdsOff.toInt()

        if (off <= 0 || off >= bytes.size) {
            throw DexParseException(
                code = DexValidationCode.INVALID_PROTO_IDS,
                message = "proto_ids_off=$off fora dos limites do arquivo (size=${bytes.size})",
            )
        }

        val tableEnd = off.toLong() + declaredCount.toLong() * PROTO_ID_ITEM_SIZE
        if (tableEnd > bytes.size) {
            throw DexParseException(
                code = DexValidationCode.INVALID_PROTO_IDS,
                message = "proto_ids tabela excede o arquivo: off=$off count=$declaredCount",
            )
        }

        val buf = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN)
        val sampleSize = minOf(declaredCount, SAMPLE_LIMIT)
        val sample = mutableListOf<DexProtoData>()

        for (i in 0 until sampleSize) {
            val itemOff    = off + i * PROTO_ID_ITEM_SIZE
            val shortyIdx      = buf.getInt(itemOff)
            val returnTypeIdx  = buf.getInt(itemOff + 4)
            val parametersOff  = buf.getInt(itemOff + 8)

            if (shortyIdx < 0 || shortyIdx >= header.stringIdsSize.toInt()) {
                throw DexParseException(
                    code = DexValidationCode.INVALID_PROTO_IDS,
                    message = "proto[$i].shorty_idx=$shortyIdx fora do range de string_ids (size=${header.stringIdsSize})",
                )
            }

            if (returnTypeIdx < 0 || returnTypeIdx >= header.typeIdsSize.toInt()) {
                throw DexParseException(
                    code = DexValidationCode.INVALID_PROTO_IDS,
                    message = "proto[$i].return_type_idx=$returnTypeIdx fora do range de type_ids (size=${header.typeIdsSize})",
                )
            }

            val shortyDescriptor = stringSummary?.sample
                ?.find { it.index == shortyIdx }
                ?.value
                ?: "<string_not_sampled:$shortyIdx>"

            val returnTypeDescriptor = typeSummary?.sample
                ?.find { it.index == returnTypeIdx }
                ?.descriptor
                ?: "<type_not_sampled:$returnTypeIdx>"

            sample += DexProtoData(
                index = i,
                shortyIndex = shortyIdx,
                returnTypeIndex = returnTypeIdx,
                parametersOffset = parametersOff,
                shortyDescriptor = shortyDescriptor,
                returnTypeDescriptor = returnTypeDescriptor,
            )
        }

        return DexProtoSummary(
            declaredCount = declaredCount,
            sample = sample,
        )
    }
}
