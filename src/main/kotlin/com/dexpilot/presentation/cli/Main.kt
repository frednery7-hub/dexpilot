package com.dexpilot.presentation.cli

import picocli.CommandLine
import picocli.CommandLine.Command
import java.util.concurrent.Callable
import kotlin.system.exitProcess

@Command(
    name = "dexpilot",
    mixinStandardHelpOptions = true,
    version = ["DexPilot 0.1.0-SNAPSHOT"],
    description = ["Experimental Kotlin/JVM CLI tool for inspecting Android DEX files."]
)
class DexPilotCommand : Callable<Int> {
    override fun call(): Int {
        CommandLine(this).usage(System.out)
        return 0
    }
}

fun main(args: Array<String>) {
    val exitCode = CommandLine(DexPilotCommand()).execute(*args)
    exitProcess(exitCode)
}
