package com.dexpilot.presentation.cli

import com.dexpilot.application.usecase.AnalyzeDexUseCase
import com.dexpilot.application.usecase.DexAnalysisResult
import com.dexpilot.domain.dex.validation.DexHeaderValidator
import com.dexpilot.infrastructure.dex.DexHeaderParser
import com.dexpilot.infrastructure.dex.FileDexBinaryReader
import com.dexpilot.infrastructure.logging.ConsoleLogger
import com.dexpilot.infrastructure.report.TextReportWriter
import picocli.CommandLine
import picocli.CommandLine.Command
import picocli.CommandLine.Parameters
import java.nio.file.Path
import java.util.concurrent.Callable
import kotlin.system.exitProcess

@Command(
    name = "dexpilot",
    mixinStandardHelpOptions = true,
    version = ["DexPilot 0.1.0-SNAPSHOT"],
    description = ["Experimental Kotlin/JVM CLI tool for inspecting Android DEX files."],
    subcommands = [
        HeaderCommand::class,
        AnalyzeCommand::class
    ]
)
class DexPilotCommand : Callable<Int> {
    override fun call(): Int {
        CommandLine(this).usage(System.out)
        return 0
    }
}

@Command(
    name = "header",
    mixinStandardHelpOptions = true,
    description = ["Parse and validate the header of a DEX file."]
)
class HeaderCommand : Callable<Int> {
    @Parameters(index = "0", description = ["Path to the .dex file."])
    lateinit var dexPath: Path

    override fun call(): Int = runAnalysis(dexPath)
}

@Command(
    name = "analyze",
    mixinStandardHelpOptions = true,
    description = ["Analyze a DEX file and print a technical summary."]
)
class AnalyzeCommand : Callable<Int> {
    @Parameters(index = "0", description = ["Path to the .dex file."])
    lateinit var dexPath: Path

    override fun call(): Int = runAnalysis(dexPath)
}

private fun runAnalysis(path: Path): Int {
    val useCase = AnalyzeDexUseCase(
        reader = FileDexBinaryReader(),
        parser = DexHeaderParser(),
        validator = DexHeaderValidator(),
        logger = ConsoleLogger()
    )

    return when (val result = useCase.execute(path)) {
        is DexAnalysisResult.Completed -> {
            println(TextReportWriter().write(result.summary))
            if (result.summary.validation.isValid) 0 else 2
        }
        is DexAnalysisResult.Failed -> {
            System.err.println("DexPilot failed: ${result.message}")
            1
        }
    }
}

fun main(args: Array<String>) {
    val exitCode = CommandLine(DexPilotCommand()).execute(*args)
    exitProcess(exitCode)
}
