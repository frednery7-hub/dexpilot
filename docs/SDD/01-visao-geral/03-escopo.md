# 03 — Escopo

## Included in MVP 1

- Kotlin/JVM project foundation.
- CLI entrypoint.
- DEX file loading.
- DEX header parser.
- Basic validation.
- Console report.
- Optional JSON report.
- JUnit 5 and Kotest Assertions.
- Minimal logging through LoggerPort and ConsoleLogger.

## Excluded from MVP 1

- DEX optimization.
- DEX writer.
- CFG.
- Call graph.
- Tree-shaking.
- Inlining.
- Obfuscation.
- Android app frontend.
- Web frontend.
- Docker requirement for local execution.

## Supported DEX Versions in MVP 1

The MVP 1 validator supports the following DEX versions:

```text
035
037
038
039
040
```

DEX version `041` is recognized but rejected with:

```text
UNSUPPORTED_EXPERIMENTAL_DEX_VERSION
```

The MVP 1 does not attempt to parse DEX `041` as a traditional DEX header.
