package com.dexpilot.infrastructure.report

object ReportEscaper {
    fun displayMagic(value: String): String =
        value.replace("\\u0000", "\\0")
            .replace("\n", "\\n")
            .replace("\u0000", "\\0")

    fun jsonString(value: String): String =
        buildString {
            append('"')
            value.forEach { char ->
                when (char) {
                    '\\' -> append("\\\\")
                    '"' -> append("\\\"")
                    '\n' -> append("\\n")
                    '\r' -> append("\\r")
                    '\t' -> append("\\t")
                    '\u0000' -> append("\\u0000")
                    else -> append(char)
                }
            }
            append('"')
        }
}
