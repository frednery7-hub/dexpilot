package com.dexpilot.domain.dex.model

data class DexMapItem(
    val typeCode: Int,
    val type: DexMapItemType,
    val size: Long,
    val offset: Long
) {
    val typeName: String
        get() = if (type == DexMapItemType.UNKNOWN) {
            "unknown_0x" + typeCode.toString(16).padStart(4, '0')
        } else {
            type.displayName
        }
}
