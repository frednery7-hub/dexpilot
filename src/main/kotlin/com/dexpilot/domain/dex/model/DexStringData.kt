package com.dexpilot.domain.dex.model

data class DexStringData(
    val index: Int,
    val offset: Long,
    val declaredUtf16Size: Long,
    val value: String
)
