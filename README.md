# DexPilot

DexPilot is an experimental Kotlin/JVM CLI tool for inspecting Android DEX files.

The initial objective is to parse and validate DEX headers safely, generate technical reports, and establish a disciplined foundation for future bytecode analysis.

## Status

MVP 1 — DEX Inspector: foundation phase.

## MVP 1 Scope

DexPilot MVP 1 includes:

- Kotlin/JVM CLI foundation.
- DEX file loading.
- DEX header parsing.
- Basic structural validation.
- Human-readable report output.
- Optional JSON report output.
- Clean Architecture.
- SOLID principles.
- Minimal event-based console logging.
- Tests with valid and corrupted fixtures.

## Out of Scope for MVP 1

DexPilot MVP 1 does not include:

- DEX rewriting.
- Bytecode optimization.
- Tree-shaking.
- Inlining.
- CFG construction.
- Call graph construction.
- APK repackaging.
- Gradle plugin integration.
- Web frontend.

## Project Location

This project is developed from an external USB drive:

```text
/Volumes/KINGSTON/dexpilot
```

## Development Policy

The project is developed in four gated milestones:

```text
25%  - Documentation and structure audit
50%  - Kotlin/Gradle/CLI foundation audit
75%  - DEX header parser and validation audit
100% - MVP 1 final audit
```

Commits are created only after each milestone audit passes.
