# DexPilot

DexPilot is an experimental Kotlin/JVM command-line tool for inspecting Android DEX files.

The MVP 1 scope is intentionally narrow: DexPilot reads a .dex file, parses its header, validates basic structural fields, and prints a technical report in text or JSON format.

DexPilot is not a DEX optimizer, bytecode rewriter, shrinker, obfuscator, APK repackager, or Gradle plugin.

## Current status

MVP 1: DEX Inspector.

Implemented:

- Kotlin/JVM CLI.
- DEX header parser.
- DEX header validator.
- Supported DEX versions: 035, 037, 038, 039, 040.
- Recognized but rejected version: 041.
- Text report output.
- JSON report output through --json.
- Minimal logging through LoggerPort.
- Logs written to stderr.
- Reports written to stdout.
- Tests with JUnit 5 and Kotest Assertions.
- Controlled fixture at src/test/resources/fixtures/valid/minimal-header.dex.

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

    ./gradlew run --args="analyze src/test/resources/fixtures/valid/minimal-header.dex"

Generate JSON report:

    ./gradlew run --args="analyze --json src/test/resources/fixtures/valid/minimal-header.dex"

Generate clean JSON into a file:

    ./gradlew -q run --args="analyze --json src/test/resources/fixtures/valid/minimal-header.dex" > report.json

Validate generated JSON locally:

    python3 -m json.tool report.json

## Architecture

DexPilot follows a simple Clean Architecture split:

    presentation/cli       CLI entrypoint and commands
    application/ports      Ports such as binary reader and logger
    application/usecase    Analysis use case and result types
    domain/dex             DEX model and validation rules
    infrastructure/dex     File reader and header parser
    infrastructure/report  Text and JSON report writers
    infrastructure/logging Console logger

## Validation policy

MVP 1 validates:

- DEX magic prefix.
- DEX version support.
- Declared file size against actual file size.
- Header size for supported versions.
- Endian tag.
- Basic section offset ranges.

MVP 1 does not validate full DEX semantic correctness, map list consistency, checksum correctness, SHA-1 signature correctness, class data, code items, instructions, or APK-level integrity.

## Version policy

Supported in MVP 1:

- 035
- 037
- 038
- 039
- 040

Recognized but rejected in MVP 1:

- 041

Version 041 is intentionally not treated as supported in MVP 1 because it requires different/container-aware handling that belongs to a future module.

## Security and safety posture

DexPilot does not execute DEX bytecode. It only reads bytes and parses metadata.

Default behavior avoids dumping strings, class names, method names, bytecode, or raw binary payloads. MVP 1 focuses on header-level metadata.

Logs are written to stderr. Reports are written to stdout. This keeps JSON output redirectable.

## Development policy

The project is developed in four gated milestones:

- 25%: Documentation and structure audit.
- 50%: Kotlin/Gradle/CLI foundation audit.
- 75%: DEX header parser and validation audit.
- 100%: MVP 1 final audit.

Commits are created only after each milestone audit passes.

## License

Proprietary / source-available during early development.
