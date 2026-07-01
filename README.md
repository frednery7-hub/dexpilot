# DexPilot

DexPilot is an experimental Kotlin/JVM command-line tool for inspecting Android DEX files.

The current v0.3.0 scope extends the original MVP 1 header inspector with controlled parsing of DEX `map_list`, `string_ids`, bounded `string_data_item` samples, and `type_ids` resolved into type descriptors.

DexPilot is not a DEX optimizer, bytecode rewriter, shrinker, obfuscator, deobfuscator, APK repackager, Gradle plugin, or bytecode execution tool.

## Current status

Current release line: v0.3.0 — Type IDs + Type Names.

Implemented:

- Kotlin/JVM CLI.
- DEX header parser.
- DEX header validator.
- Supported DEX versions: 035, 037, 038, 039, 040.
- Recognized but rejected version: 041.
- `map_list` parser from `map_off`.
- Known DEX map item type recognition.
- `string_ids` parser.
- Bounded ULEB128 decoder for string data.
- Safe sample extraction from `string_data_item`.
- `type_ids` parser.
- Type descriptor resolution through `string_ids`.
- Text report output.
- JSON report output through `--json`.
- Minimal logging through `LoggerPort`.
- Logs written to stderr.
- Reports written to stdout.
- Tests with JUnit 5 and Kotest Assertions.
- Controlled fixtures under `src/test/resources/fixtures/valid/`.

Controlled fixtures:

- `minimal-header.dex`
- `minimal-map-list.dex`
- `minimal-string-ids.dex`
- `minimal-type-ids.dex`

## Requirements

- JDK 17.
- Gradle Wrapper included in the repository.

## Commands

Run tests:

    ./gradlew test

Build:

    ./gradlew build

Show version:

    ./gradlew run --args="--version"

Show help:

    ./gradlew run --args="--help"

Inspect a DEX header:

    ./gradlew run --args="header src/test/resources/fixtures/valid/minimal-header.dex"

Analyze a DEX file:

    ./gradlew run --args="analyze src/test/resources/fixtures/valid/minimal-type-ids.dex"

Generate JSON report:

    ./gradlew run --args="analyze --json src/test/resources/fixtures/valid/minimal-type-ids.dex"

Generate clean JSON into a file:

    ./gradlew -q run --args="analyze --json src/test/resources/fixtures/valid/minimal-type-ids.dex" > report.json

Validate generated JSON locally:

    python3 -m json.tool report.json

## Architecture

DexPilot follows a simple Clean Architecture split:

    presentation/cli       CLI entrypoint and commands
    application/ports      Ports such as binary reader and logger
    application/usecase    Analysis use case and result types
    domain/dex             DEX model and validation rules
    infrastructure/dex     File reader, header parser, map parser, string parser
    infrastructure/report  Text and JSON report writers
    infrastructure/logging Console logger

## Validation policy

DexPilot currently validates:

- DEX magic prefix.
- DEX version support.
- Declared file size against actual file size.
- Header size for supported versions.
- Endian tag.
- Basic section offset ranges.
- `map_off` readability.
- `map_list` size boundaries.
- `string_ids` range boundaries.
- `string_data_off` file bounds.
- ULEB128 decoding with a fixed safety limit.
- `type_ids` range boundaries.
- `type_id.descriptor_idx` references against `string_ids_size`.

DexPilot does not validate full DEX semantic correctness, checksum correctness, SHA-1 signature correctness, class definitions, method bodies, bytecode instructions, control-flow graphs, call graphs, or APK-level integrity.

## Version policy

Supported:

- 035
- 037
- 038
- 039
- 040

Recognized but rejected:

- 041

Version 041 is intentionally not treated as supported yet because it requires different/container-aware handling that belongs to a future module.

## Security and safety posture

DexPilot does not execute DEX bytecode. It only reads bytes and parses metadata.

Default behavior avoids dumping every string, class name, method name, bytecode block, or raw binary payload.

String extraction is bounded:

- The report includes only a sample.
- Long strings are truncated.
- Control characters are escaped.
- ULEB128 decoding is limited.
- Missing null terminators are rejected within a fixed safety range.

Logs are written to stderr. Reports are written to stdout. This keeps JSON output redirectable.

## Development policy

The overall MVP 1 project was developed through four gated milestones:

- 25%: Documentation and structure audit.
- 50%: Kotlin/Gradle/CLI foundation audit.
- 75%: DEX header parser and validation audit.
- 100%: MVP 1 final audit.

After MVP 1, new versions are handled through technical stages rather than restarting the percentage gates.

v0.2.0 stages:

- Stage A: Planning.
- Stage B: Map List parser.
- Stage C: String IDs parser.
- Stage D: Documentation, evidence, audit, merge, tag, and release.

Commits are created only after each stage audit passes.

## Release history

- v0.1.0: MVP 1 DEX header inspector.
- v0.2.0: Map List + String IDs.
- v0.3.0: Type IDs + Type Names.

## License

Proprietary / source-available during early development.

## Applied portfolio use cases

DexPilot is designed as a defensive Android DEX inspection tool.

In the broader portfolio, it complements mobile applications such as Movia and Vaultia by providing a read-only inspection layer for Android artifacts generated during build and release workflows.

- Movia demonstrates applied mobile product engineering.
- Vaultia demonstrates privacy-first and security-first mobile architecture.
- DexPilot demonstrates Android DEX artifact inspection.

See:

- `docs/use-cases/MOBILE_ARTIFACT_INSPECTION_PORTFOLIO.md`
