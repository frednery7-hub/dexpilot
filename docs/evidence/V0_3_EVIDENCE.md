# DexPilot v0.3.0 Evidence

## Scope

v0.3.0 extends DexPilot by adding controlled parsing of DEX `type_ids` and resolving type descriptors through the string table foundation introduced in v0.2.0.

Included:

- `DexTypeIdsParser`.
- `DexTypeData`.
- `DexTypeSummary`.
- `INVALID_TYPE_IDS` parse error classification.
- Type descriptor resolution through parsed string samples.
- Text report support for `Types`.
- JSON report support for `typeSummary`.
- Controlled fixture: `minimal-type-ids.dex`.

Excluded:

- Proto IDs parsing.
- Field IDs parsing.
- Method IDs parsing.
- Class definitions parsing.
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

Type IDs text report:

    ./gradlew run --args="analyze src/test/resources/fixtures/valid/minimal-type-ids.dex"

Type IDs JSON report:

    ./gradlew run --args="analyze --json src/test/resources/fixtures/valid/minimal-type-ids.dex"

Clean JSON validation:

    ./gradlew -q run --args="analyze --json src/test/resources/fixtures/valid/minimal-type-ids.dex" > report.json
    python3 -m json.tool report.json

## Expected type_ids output markers

The `minimal-type-ids.dex` fixture should report:

    Status: VALID_DEX
    Type IDs: 1
    Types:
    Declared types: 1
    Sample size: 1
    - #0 descriptor_idx=0: Ljava/lang/String;

The JSON report should include:

    "typeSummary"
    "declaredCount": 1
    "descriptor": "Ljava/lang/String;"

## Safety evidence

v0.3.0 keeps DexPilot inside a bounded read-only inspection model:

- No bytecode execution.
- No DEX rewriting.
- No APK repackaging.
- Type descriptor indexes are validated against `string_ids_size`.
- Reports remain deterministic.
- Logs remain on stderr.
- Reports remain on stdout.

## Audit status

Stage A passed with planning documentation.

Stage B passed with type ID parser, domain models, use case integration, tests, and build.

Stage C passed with text report output, JSON report output, controlled fixture, CLI validation, JSON validation, tests, and build.

Final release requires PR CI, main CI, tag, and GitHub Release.
