package com.dexpilot.infrastructure.dex

import com.dexpilot.domain.dex.model.DexHeader
import java.nio.ByteBuffer
import java.nio.ByteOrder

class DexHeaderParser {
    fun parse(bytes: ByteArray): DexHeader {
        if (bytes.size < DexHeader.STANDARD_HEADER_SIZE_BYTES) {
            throw DexParseException(
                DexParseErrorCode.FILE_TOO_SMALL,
                "A DEX header requires at least 112 bytes."
            )
        }

        val buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN)
        val magicBytes = ByteArray(8)
        buffer.get(magicBytes)

        return DexHeader(
            magicText = magicBytes.toString(Charsets.US_ASCII),
            version = extractVersion(magicBytes),
            checksum = buffer.readUInt(),
            signatureHex = buffer.readSignatureHex(),
            fileSize = buffer.readUInt(),
            headerSize = buffer.readUInt(),
            endianTag = buffer.readUInt(),
            linkSize = buffer.readUInt(),
            linkOff = buffer.readUInt(),
            mapOff = buffer.readUInt(),
            stringIdsSize = buffer.readUInt(),
            stringIdsOff = buffer.readUInt(),
            typeIdsSize = buffer.readUInt(),
            typeIdsOff = buffer.readUInt(),
            protoIdsSize = buffer.readUInt(),
            protoIdsOff = buffer.readUInt(),
            fieldIdsSize = buffer.readUInt(),
            fieldIdsOff = buffer.readUInt(),
            methodIdsSize = buffer.readUInt(),
            methodIdsOff = buffer.readUInt(),
            classDefsSize = buffer.readUInt(),
            classDefsOff = buffer.readUInt(),
            dataSize = buffer.readUInt(),
            dataOff = buffer.readUInt()
        )
    }

    private fun extractVersion(magicBytes: ByteArray): String {
        if (magicBytes.size != 8) return ""
        if (magicBytes[0] != 'd'.code.toByte()) return ""
        if (magicBytes[1] != 'e'.code.toByte()) return ""
        if (magicBytes[2] != 'x'.code.toByte()) return ""
        if (magicBytes[3] != '\n'.code.toByte()) return ""
        if (magicBytes[7] != 0.toByte()) return ""

        return magicBytes.copyOfRange(4, 7).toString(Charsets.US_ASCII)
    }

    private fun ByteBuffer.readUInt(): Long =
        Integer.toUnsignedLong(int)

    private fun ByteBuffer.readSignatureHex(): String {
        val signature = ByteArray(20)
        get(signature)
        return signature.joinToString(separator = "") { "%02x".format(it) }
    }
}
