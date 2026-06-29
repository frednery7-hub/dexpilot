package com.dexpilot.domain.dex.model

enum class DexMapItemType(
    val code: Int,
    val displayName: String
) {
    HEADER_ITEM(0x0000, "header_item"),
    STRING_ID_ITEM(0x0001, "string_id_item"),
    TYPE_ID_ITEM(0x0002, "type_id_item"),
    PROTO_ID_ITEM(0x0003, "proto_id_item"),
    FIELD_ID_ITEM(0x0004, "field_id_item"),
    METHOD_ID_ITEM(0x0005, "method_id_item"),
    CLASS_DEF_ITEM(0x0006, "class_def_item"),
    CALL_SITE_ID_ITEM(0x0007, "call_site_id_item"),
    METHOD_HANDLE_ITEM(0x0008, "method_handle_item"),
    MAP_LIST(0x1000, "map_list"),
    TYPE_LIST(0x1001, "type_list"),
    ANNOTATION_SET_REF_LIST(0x1002, "annotation_set_ref_list"),
    ANNOTATION_SET_ITEM(0x1003, "annotation_set_item"),
    CLASS_DATA_ITEM(0x2000, "class_data_item"),
    CODE_ITEM(0x2001, "code_item"),
    STRING_DATA_ITEM(0x2002, "string_data_item"),
    DEBUG_INFO_ITEM(0x2003, "debug_info_item"),
    ANNOTATION_ITEM(0x2004, "annotation_item"),
    ENCODED_ARRAY_ITEM(0x2005, "encoded_array_item"),
    ANNOTATIONS_DIRECTORY_ITEM(0x2006, "annotations_directory_item"),
    UNKNOWN(-1, "unknown");

    companion object {
        fun fromCode(code: Int): DexMapItemType =
            entries.firstOrNull { it.code == code } ?: UNKNOWN
    }
}
