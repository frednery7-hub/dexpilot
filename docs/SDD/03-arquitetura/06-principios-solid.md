# 06 — Princípios SOLID

DexPilot must apply SOLID principles as mandatory architectural constraints.

## Single Responsibility Principle

Each class must have one clear reason to change.

Expected examples:

- `DexHeaderParser`: reads DEX header fields.
- `DexHeaderValidator`: validates DEX header rules.
- `AnalyzeDexUseCase`: orchestrates analysis.
- `HeaderCommand`: adapts CLI input to the application layer.
- `ConsoleLogger`: emits simple console events.

Generic classes accumulating parsing, validation, logging, CLI handling, and output formatting must be avoided.

## Open/Closed Principle

The system must be extensible without aggressive modification of existing code.

DEX version policy must be centralized instead of scattered across parser logic.

MVP 1 supported versions:

```text
035
037
038
039
040
```

Version `041` is recognized but rejected with:

```text
UNSUPPORTED_EXPERIMENTAL_DEX_VERSION
```

Real support for DEX `041` will be specified in a future dedicated module.

## Liskov Substitution Principle

Implementations of the same port must be interchangeable without breaking expected behavior.

Example:

```text
DexBinaryReader
```

may have file-based, memory-based, and test-fixture implementations, provided all respect the same safe reading and error handling contract.

## Interface Segregation Principle

Large generic interfaces must be avoided.

Preferred ports:

```text
DexBinaryReader
DexReportWriter
LoggerPort
HeaderOutputFormatter
```

## Dependency Inversion Principle

High-level layers must not depend directly on infrastructure details.

Expected flow:

```text
presentation/cli -> application/usecase -> domain
                              |
                            ports
                              |
                        infrastructure
```

The domain layer must not know about CLI, files, loggers, JSON, or system exits.
