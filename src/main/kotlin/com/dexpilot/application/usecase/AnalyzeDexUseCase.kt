package com.dexpilot.application.usecase

import com.dexpilot.application.ports.DexBinaryReader
import com.dexpilot.application.ports.LoggerPort
import com.dexpilot.domain.dex.validation.DexHeaderValidator
import com.dexpilot.infrastructure.dex.DexHeaderParser
import com.dexpilot.infrastructure.dex.DexParseException
import java.nio.file.Path

class AnalyzeDexUseCase(
    private val reader: DexBinaryReader,
    private val parser: DexHeaderParser,
    private val validator: DexHeaderValidator,
    private val logger: LoggerPort
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
            DexAnalysisResult.Completed(
                DexFileSummary(
                    path = path.toString(),
                    actualFileSize = bytes.size.toLong(),
                    header = header,
                    validation = validation
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
