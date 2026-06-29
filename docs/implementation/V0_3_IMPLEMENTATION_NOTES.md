# DexPilot v0.3.0 Implementation Notes

## Design decision

v0.3.0 adds type table inspection without expanding into methods, fields, class definitions, or bytecode.

In DEX, `type_ids` reference descriptors stored in `string_ids`. Since v0.2.0 already introduced bounded string parsing, v0.3.0 reuses that foundation to resolve type descriptors safely.

## Main files

- `DexTypeData.kt`
- `DexTypeSummary.kt`
- `DexTypeIdsParser.kt`
- `DexTypeIdsParserTest.kt`
- `minimal-type-ids.dex`

Updated files:

- `AnalyzeDexUseCase.kt`
- `DexFileSummary.kt`
- `DexParseException.kt`
- `TextReportWriter.kt`
- `JsonReportWriter.kt`

## Parser behavior

The type parser reads:

- `type_ids_size`.
- `type_ids_off`.
- Each `type_id_item.descriptor_idx`.

For each sampled type ID, the parser validates:

- The type ID table is readable.
- The table range does not exceed file bounds.
- `descriptor_idx` is lower than `string_ids_size`.

The parser resolves descriptors through `DexStringSummary`.

If a descriptor index is valid but not available in the current bounded string sample, the descriptor is represented as:

    <string_not_sampled:N>

## Reporting

Text reports now include:

    Types:
    Declared types: N
    Sample size: N
    - #0 descriptor_idx=0: Ljava/lang/String;

JSON reports now include:

    "typeSummary": {
      "declaredCount": 1,
      "sample": [
        {
          "index": 0,
          "descriptorIndex": 0,
          "descriptor": "Ljava/lang/String;"
        }
      ]
    }

## Fixture

`minimal-type-ids.dex` is a controlled synthetic DEX fixture containing:

- Valid DEX header.
- One string ID.
- One string data item: `Ljava/lang/String;`.
- One type ID pointing to descriptor index `0`.
- A map list with `header_item`, `string_id_item`, `type_id_item`, and `map_list`.

No external APK or third-party DEX sample is required.

## Known v0.3.0 limits

- No proto IDs parsing.
- No field IDs parsing.
- No method IDs parsing.
- No class definitions parsing.
- No code item parsing.
- No instruction parsing.
- No control-flow graph.
- No call graph.
- No APK container inspection.
- No DEX writing or rewriting.

These limits are intentional and belong to future versions.
