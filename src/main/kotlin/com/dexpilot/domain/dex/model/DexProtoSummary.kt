package com.dexpilot.domain.dex.model

/**
 * Resumo da tabela proto_ids de um arquivo DEX.
 *
 * - declaredCount: valor de proto_ids_size declarado no header
 * - sample:        amostra dos primeiros protos resolvidos
 */
data class DexProtoSummary(
    val declaredCount: Int,
    val sample: List<DexProtoData>,
)
