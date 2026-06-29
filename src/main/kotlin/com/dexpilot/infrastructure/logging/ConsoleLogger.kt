package com.dexpilot.infrastructure.logging

import com.dexpilot.application.ports.LoggerPort

class ConsoleLogger : LoggerPort {
    override fun info(event: String, message: String) {
        println("INFO  $event $message")
    }

    override fun warn(event: String, message: String) {
        System.err.println("WARN  $event $message")
    }

    override fun error(event: String, message: String) {
        System.err.println("ERROR $event $message")
    }
}
