package com.dexpilot.application.usecase

import com.dexpilot.domain.dex.model.DexHeader
import com.dexpilot.domain.dex.model.DexMapList
import com.dexpilot.domain.dex.model.DexProtoSummary
import com.dexpilot.domain.dex.model.DexStringSummary
import com.dexpilot.domain.dex.model.DexTypeSummary
import com.dexpilot.domain.dex.validation.DexValidationResult

data class DexFileSummary(
    val path: String,
    val actualFileSize: Long,
    val header: DexHeader,
    val validation: DexValidationResult,
    val mapList: DexMapList? = null,
    val stringSummary: DexStringSummary? = null,
    val typeSummary: DexTypeSummary? = null,
    val protoSummary: DexProtoSummary? = null,
)
