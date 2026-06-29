# DexPilot v0.2.0 Evidence

## Scope

v0.2.0 extends DexPilot beyond header-level inspection by adding controlled parsing of:

- DEX `map_list`.
- DEX `string_ids`.
- DEX `string_data_item` samples.

Included:

- `DexMapListParser`.
- `DexMapItem`.
- `DexMapItemType`.
- `DexMapList`.
- `DexStringIdsParser`.
- `Uleb128Decoder`.
- `DexStringData`.
- `DexStringSummary`.
- Text report support for map list and string samples.
- JSON report support for `mapList` and `stringSummary`.
- Controlled test fixtures.

Excluded:

- Class definitions parsing.
- Type IDs semantic expansion.
- Proto IDs parsing.
- Field IDs parsing.
- Method IDs parsing.
- Code item parsing.
- Bytecode instruction parsing.
- CFG construction.
- Call graph construction.
- DEX writing or rewriting.
- APK-level analysis.

## Evidence commands

Test suite:

    ./gradlew test

Build:

    ./gradlew build

Map list text report:

    ./gradlew run --args="analyze src/test/resources/fixtures/valid/minimal-map-list.dex"

Map list JSON report:

    ./gradlew run --args="analyze --json src/test/resources/fixtures/valid/minimal-map-list.dex"

String IDs text report:

    ./gradlew run --args="analyze src/test/resources/fixtures/valid/minimal-string-ids.dex"

String IDs JSON report:

    ./gradlew run --args="analyze --json src/test/resources/fixtures/valid/minimal-string-ids.dex"

Clean JSON validation:

    ./gradlew -q run --args="analyze --json src/test/resources/fixtures/valid/minimal-string-ids.dex" > report.json
    python3 -m json.tool report.json

## Expected map_list output markers

The `minimal-map-list.dex` fixture should report:

    Status: VALID_DEX
    Map List:
    Declared items: 2
    - header_item: size=1, offset=0
    - map_list: size=1, offset=112

The JSON report should include:

    "mapList"
    "declaredSize": 2
    "typeName": "header_item"
    "typeName": "map_list"

## Expected string_ids output markers

The `minimal-string-ids.dex` fixture should report:

    Status: VALID_DEX
    String IDs: 1
    Strings:
    Declared strings: 1
    Sample size: 1
    - #0 @116: hello

The JSON report should include:

    "stringSummary"
    "declaredCount": 1
    "value": "hello"

## Safety evidence

v0.2.0 includes bounded parsing behavior:

- `MAX_STRING_SAMPLE_COUNT = 10`.
- `MAX_STRING_DISPLAY_LENGTH = 120`.
- `MAX_ULEB128_BYTES = 5`.
- `MAX_STRING_RAW_BYTES = 4096`.
- No bytecode execution.
- No DEX rewriting.
- No APK repackaging.

## Audit status

Stage A passed with planning documentation.

Stage B passed with map list parser, fixture, text report, JSON report, tests, build, and artifact audit.

Stage C passed with string IDs parser, ULEB128 decoder, fixture, text report, JSON report, tests, build, and artifact audit.

Stage D requires final documentation audit, local validation, push, CI, merge, tag, and GitHub release.
