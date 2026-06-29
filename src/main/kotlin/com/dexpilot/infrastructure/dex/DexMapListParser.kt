package com.dexpilot.infrastructure.dex

import com.dexpilot.domain.dex.model.DexMapItem
import com.dexpilot.domain.dex.model.DexMapItemType
import com.dexpilot.domain.dex.model.DexMapList
import java.nio.ByteBuffer
import java.nio.ByteOrder

class DexMapListParser {
    fun parse(bytes: ByteArray, mapOff: Long): DexMapList? {
        if (mapOff == 0L) return null

        if (mapOff < 0 || mapOff > bytes.size - MAP_SIZE_BYTES) {
            throw DexParseException(
                DexParseErrorCode.INVALID_MAP_LIST,
                "map_off $mapOff does not point to a readable map_list size."
            )
        }

        val buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN)
        buffer.position(mapOff.toInt())

        val declaredSize = buffer.readUInt()
        val mapItemsStart = mapOff + MAP_SIZE_BYTES
        val byteCount = declaredSize * MAP_ITEM_SIZE_BYTES
        val endExclusive = mapItemsStart + byteCount

        if (declaredSize > MAX_REASONABLE_MAP_ITEMS) {
            throw DexParseException(
                DexParseErrorCode.INVALID_MAP_LIST,
                "map_list declared size $declaredSize exceeds MVP 1 safety limit."
            )
        }

        if (endExclusive > bytes.size || endExclusive < mapItemsStart) {
            throw DexParseException(
                DexParseErrorCode.INVALID_MAP_LIST,
                "map_list range [$mapItemsStart, $endExclusive) exceeds file size ${bytes.size}."
            )
        }

        val items = buildList {
            repeat(declaredSize.toInt()) {
                val typeCode = buffer.short.toInt() and 0xffff
                buffer.short
                val itemSize = buffer.readUInt()
                val itemOffset = buffer.readUInt()

                add(
                    DexMapItem(
                        typeCode = typeCode,
                        type = DexMapItemType.fromCode(typeCode),
                        size = itemSize,
                        offset = itemOffset
                    )
                )
            }
        }

        return DexMapList(
            declaredSize = declaredSize,
            items = items
        )
    }

    private fun ByteBuffer.readUInt(): Long =
        Integer.toUnsignedLong(int)

    private companion object {
        const val MAP_SIZE_BYTES: Long = 4
        const val MAP_ITEM_SIZE_BYTES: Long = 12
        const val MAX_REASONABLE_MAP_ITEMS: Long = 4096
    }
}
