# DexPilot MVP 1 Evidence

## Scope

MVP 1 implements a DEX Inspector focused on DEX header parsing and validation.

Included:

- CLI with header and analyze commands.
- Text output.
- JSON output through --json.
- DEX header parser.
- DEX header validator.
- Version policy for DEX 035, 037, 038, 039, 040.
- Explicit rejection of recognized unsupported 041.
- Controlled fixture for tests and CLI validation.

Excluded:

- DEX rewriting.
- DEX optimization.
- DEX bytecode execution.
- CFG or call graph.
- APK repackaging.
- Gradle plugin.
- Full semantic validation of DEX internals.

## Evidence commands

Version:

    ./gradlew run --args="--version"

Help:

    ./gradlew run --args="--help"

Header help:

    ./gradlew run --args="header --help"

Analyze help:

    ./gradlew run --args="analyze --help"

Text report:

    ./gradlew run --args="analyze src/test/resources/fixtures/valid/minimal-header.dex"

JSON report:

    ./gradlew run --args="analyze --json src/test/resources/fixtures/valid/minimal-header.dex"

Clean JSON export:

    ./gradlew -q run --args="analyze --json src/test/resources/fixtures/valid/minimal-header.dex" > report.json
    python3 -m json.tool report.json

Test suite:

    ./gradlew test

Build:

    ./gradlew build

## Expected MVP 1 output markers

The valid fixture should report:

    Status: VALID_DEX
    Magic: dex\\n040\\0
    Version: 040
    File size declared: 112 bytes
    File size actual: 112 bytes
    Header size: 112 bytes
    Endian tag: 0x12345678

The JSON report should include:

    "tool": "DexPilot"
    "formatVersion": "1.0"
    "status": "VALID_DEX"

## Audit status

The 75% implementation audit passed before commit 006ab36.

The final MVP 1 audit must pass before the closing commit.
