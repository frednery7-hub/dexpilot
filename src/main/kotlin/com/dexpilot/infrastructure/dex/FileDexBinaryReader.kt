package com.dexpilot.infrastructure.dex

import com.dexpilot.application.ports.DexBinaryReader
import java.nio.file.Files
import java.nio.file.Path

class FileDexBinaryReader : DexBinaryReader {
    override fun readAllBytes(path: Path): ByteArray = Files.readAllBytes(path)
}
