# DexPilot MVP 1 Implementation Notes

## Design decision

DexPilot MVP 1 is deliberately constrained to DEX header inspection.

The objective is to establish a reliable binary parsing foundation before any future work involving deeper DEX structures, map lists, class definitions, code items, control-flow graphs, APK containers, or optimization logic.

## Main packages

- com.dexpilot.presentation.cli
- com.dexpilot.application.ports
- com.dexpilot.application.usecase
- com.dexpilot.domain.dex.model
- com.dexpilot.domain.dex.validation
- com.dexpilot.infrastructure.dex
- com.dexpilot.infrastructure.logging
- com.dexpilot.infrastructure.report

## Error handling

Infrastructure parsing errors are represented by DexParseException.

The application layer exposes DexAnalysisResult so the CLI does not need to expose internal exceptions or raw stack traces by default.

## Logging

MVP 1 uses LoggerPort and ConsoleLogger.

Logs are written to stderr. Reports are written to stdout. This keeps JSON output redirectable.

## Reports

Text report:

- Human-readable terminal output.
- Escaped DEX magic rendering.

JSON report:

- Deterministic schema.
- No external JSON dependency in MVP 1.
- Values manually escaped through ReportEscaper.
- Clean JSON can be generated with Gradle quiet mode.

## Test approach

Tests use:

- JUnit 5.
- Kotest Assertions.
- Controlled in-memory DEX header bytes.
- One minimal .dex fixture under src/test/resources/fixtures/valid/.

No external APK or third-party DEX sample is required for MVP 1.

## Known MVP 1 limits

- No full DEX semantic validation.
- No checksum verification.
- No SHA-1 signature verification.
- No map list consistency validation.
- No class data parsing.
- No code item parsing.
- No bytecode instruction parsing.
- No APK container inspection.

These limits are intentional and should be addressed only in future milestones.
