package com.dexpilot.application.ports

import java.nio.file.Path

interface DexBinaryReader {
    fun readAllBytes(path: Path): ByteArray
}
