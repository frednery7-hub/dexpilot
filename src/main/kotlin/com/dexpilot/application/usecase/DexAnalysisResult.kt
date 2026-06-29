package com.dexpilot.application.usecase

sealed interface DexAnalysisResult {
    data class Completed(val summary: DexFileSummary) : DexAnalysisResult
    data class Failed(val message: String) : DexAnalysisResult
}
