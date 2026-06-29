package com.dexpilot.domain.dex.validation

object DexVersionPolicy {
    val supportedVersions: Set<String> = setOf("035", "037", "038", "039", "040")
    val recognizedUnsupportedVersions: Set<String> = setOf("041")

    fun isSupported(version: String): Boolean = version in supportedVersions

    fun isRecognizedButUnsupported(version: String): Boolean =
        version in recognizedUnsupportedVersions
}
