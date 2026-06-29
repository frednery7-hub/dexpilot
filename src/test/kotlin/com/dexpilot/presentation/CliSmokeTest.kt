package com.dexpilot.presentation

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import picocli.CommandLine
import com.dexpilot.presentation.cli.DexPilotCommand

class CliSmokeTest {
    @Test
    fun `version command exits successfully`() {
        val exitCode = CommandLine(DexPilotCommand()).execute("--version")

        exitCode shouldBe 0
    }
}
