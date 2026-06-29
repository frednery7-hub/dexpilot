package com.dexpilot.application.ports

interface LoggerPort {
    fun info(event: String, message: String)
    fun warn(event: String, message: String)
    fun error(event: String, message: String)
}
