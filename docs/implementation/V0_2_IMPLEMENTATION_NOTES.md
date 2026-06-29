# DexPilot v0.2.0 Implementation Notes

## Design decision

v0.2.0 adds two deeper DEX inspection capabilities while keeping the project inside a safe read-only boundary:

- `map_list` parsing.
- `string_ids` and bounded string sample parsing.

The version still avoids class parsing, method parsing, bytecode parsing, DEX rewriting, APK repackaging, and bytecode execution.

## Map list implementation

Main files:

- `DexMapItemType.kt`
- `DexMapItem.kt`
- `DexMapList.kt`
- `DexMapListParser.kt`
- `DexMapListParserTest.kt`
- `minimal-map-list.dex`

The map parser reads:

- `map_off` from the DEX header.
- `map_list.size`.
- Each `map_item`: type, unused field, size, and offset.

Known map item type codes are mapped to display names. Unknown type codes are preserved and rendered as `unknown_0xNNNN`.

## String IDs implementation

Main files:

- `DexStringData.kt`
- `DexStringSummary.kt`
- `Uleb128Decoder.kt`
- `DexStringIdsParser.kt`
- `Uleb128DecoderTest.kt`
- `DexStringIdsParserTest.kt`
- `minimal-string-ids.dex`

The string parser reads:

- `string_ids_size`.
- `string_ids_off`.
- Each sampled `string_id_item.string_data_off`.
- `string_data_item` ULEB128 declared UTF-16 size.
- UTF-8 string bytes until null terminator, within a fixed safety window.

## Safety limits

Current limits:

- Maximum map items: 4096.
- Maximum string IDs accepted: 1,000,000.
- Maximum reported string sample count: 10.
- Maximum displayed string length: 120.
- Maximum ULEB128 bytes: 5.
- Maximum raw bytes searched for a string terminator: 4096.

## Reporting

Text reports include:

- Header summary.
- Optional map list summary.
- Optional string summary.

JSON reports include:

- `header`.
- `mapList`.
- `stringSummary`.
- `validationErrors`.

Logs remain on stderr. Reports remain on stdout.

## Fixtures

v0.2.0 uses controlled synthetic DEX fixtures:

- `minimal-map-list.dex`: valid DEX header with a two-item map list.
- `minimal-string-ids.dex`: valid DEX header with one string ID and string value `hello`.

No external APK or third-party DEX sample is required.

## Known v0.2.0 limits

- No checksum verification.
- No SHA-1 signature verification.
- No class definitions parsing.
- No type table semantic expansion.
- No proto, field, or method parsing.
- No code item parsing.
- No instruction parsing.
- No control-flow graph.
- No call graph.
- No APK container inspection.
- No DEX writing or rewriting.

These limits are intentional and should be handled only in future versions.
