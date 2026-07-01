package com.dexpilot.application.usecase

import com.dexpilot.application.ports.DexBinaryReader
import com.dexpilot.application.ports.LoggerPort
import com.dexpilot.domain.dex.validation.DexHeaderValidator
import com.dexpilot.infrastructure.dex.DexHeaderParser
import com.dexpilot.infrastructure.dex.DexMapListParser
import com.dexpilot.infrastructure.dex.DexParseException
import com.dexpilot.infrastructure.dex.DexProtoIdsParser
import com.dexpilot.infrastructure.dex.DexStringIdsParser
import com.dexpilot.infrastructure.dex.DexTypeIdsParser
import java.nio.file.Path

class AnalyzeDexUseCase(
    private val reader: DexBinaryReader,
    private val parser: DexHeaderParser,
    private val validator: DexHeaderValidator,
    private val logger: LoggerPort,
    private val mapListParser: DexMapListParser = DexMapListParser(),
    private val stringIdsParser: DexStringIdsParser = DexStringIdsParser(),
    private val typeIdsParser: DexTypeIdsParser = DexTypeIdsParser(),
    private val protoIdsParser: DexProtoIdsParser = DexProtoIdsParser(),
) {
    fun execute(path: Path): DexAnalysisResult {
        return try {
            logger.info("DEX_ANALYSIS_STARTED", "path=$path")
            val bytes = reader.readAllBytes(path)
            logger.info("DEX_FILE_LOADED", "size=${bytes.size}")
            logger.info("DEX_HEADER_PARSE_STARTED", "path=$path")
            val header = parser.parse(bytes)
            logger.info("DEX_HEADER_PARSED", "version=${header.version}")
            val validation = validator.validate(header, actualFileSize = bytes.size.toLong())
            logger.info("DEX_VALIDATION_COMPLETED", "status=${validation.status}")

            val mapList = mapListParser.parse(bytes, header.mapOff)
            logger.info("DEX_MAP_LIST_PARSED", "items=${mapList?.items?.size ?: 0}")

            val stringSummary = stringIdsParser.parse(
                bytes = bytes,
                stringIdsSize = header.stringIdsSize,
                stringIdsOff = header.stringIdsOff,
            )
            logger.info("DEX_STRING_IDS_PARSED", "sample=${stringSummary?.sample?.size ?: 0}")

            val typeSummary = typeIdsParser.parse(
                bytes = bytes,
                typeIdsSize = header.typeIdsSize,
                typeIdsOff = header.typeIdsOff,
                stringIdsSize = header.stringIdsSize,
                stringSummary = stringSummary,
            )
            logger.info("DEX_TYPE_IDS_PARSED", "sample=${typeSummary?.sample?.size ?: 0}")

            val protoSummary = protoIdsParser.parse(
                bytes = bytes,
                header = header,
                stringSummary = stringSummary,
                typeSummary = typeSummary,
            )
            logger.info("DEX_PROTO_IDS_PARSED", "sample=${protoSummary.sample.size}")

            DexAnalysisResult.Completed(
                DexFileSummary(
                    path = path.toString(),
                    actualFileSize = bytes.size.toLong(),
                    header = header,
                    validation = validation,
                    mapList = mapList,
                    stringSummary = stringSummary,
                    typeSummary = typeSummary,
                    protoSummary = protoSummary,
                )
            )
        } catch (error: DexParseException) {
            logger.error("DEX_ANALYSIS_FAILED", "reason=${error.code}")
            DexAnalysisResult.Failed(error.message ?: "DEX parsing failed.")
        } catch (error: Exception) {
            logger.error("DEX_ANALYSIS_FAILED", "reason=${error::class.simpleName}")
            DexAnalysisResult.Failed(error.message ?: "DEX analysis failed.")
        }
    }
}
