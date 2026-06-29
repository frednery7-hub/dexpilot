package com.dexpilot.domain.dex.model

data class DexHeader(
    val magicText: String,
    val version: String,
    val checksum: Long,
    val signatureHex: String,
    val fileSize: Long,
    val headerSize: Long,
    val endianTag: Long,
    val linkSize: Long,
    val linkOff: Long,
    val mapOff: Long,
    val stringIdsSize: Long,
    val stringIdsOff: Long,
    val typeIdsSize: Long,
    val typeIdsOff: Long,
    val protoIdsSize: Long,
    val protoIdsOff: Long,
    val fieldIdsSize: Long,
    val fieldIdsOff: Long,
    val methodIdsSize: Long,
    val methodIdsOff: Long,
    val classDefsSize: Long,
    val classDefsOff: Long,
    val dataSize: Long,
    val dataOff: Long
) {
    companion object {
        const val STANDARD_HEADER_SIZE_BYTES: Long = 0x70
        const val REVERSE_ENDIAN_CONSTANT: Long = 0x78563412
        const val STANDARD_ENDIAN_CONSTANT: Long = 0x12345678
    }
}
