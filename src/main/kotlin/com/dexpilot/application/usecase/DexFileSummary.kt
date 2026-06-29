package com.dexpilot.application.usecase

import com.dexpilot.domain.dex.model.DexHeader
import com.dexpilot.domain.dex.model.DexMapList
import com.dexpilot.domain.dex.validation.DexValidationResult

data class DexFileSummary(
    val path: String,
    val actualFileSize: Long,
    val header: DexHeader,
    val validation: DexValidationResult,
    val mapList: DexMapList? = null
)
