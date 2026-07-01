package com.dexpilot.domain.dex.model

/**
 * Representa um proto_id_item do formato DEX.
 *
 * Campos conforme especificação DEX:
 * - shortyIndex:       índice em string_ids para o shorty descriptor (ex: "V", "II")
 * - returnTypeIndex:   índice em type_ids para o tipo de retorno
 * - parametersOffset:  offset absoluto para a type_list de parâmetros (0 = sem parâmetros)
 *
 * Campos resolvidos (opcionais — dependem de string_ids e type_ids já parseados):
 * - shortyDescriptor:  string resolvida do shorty descriptor
 * - returnTypeDescriptor: string resolvida do tipo de retorno
 */
data class DexProtoData(
    val index: Int,
    val shortyIndex: Int,
    val returnTypeIndex: Int,
    val parametersOffset: Int,
    val shortyDescriptor: String?,
    val returnTypeDescriptor: String?,
)
