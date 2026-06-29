# 03 — Política de Logs

DexPilot adopts a minimal logging policy.

Logs must increase clarity, not volume. MVP 1 must not produce noisy logs, byte dumps, or sensitive information extracted from DEX files.

## Allowed Events in MVP 1

```text
DEX_ANALYSIS_STARTED
DEX_FILE_LOADED
DEX_HEADER_PARSE_STARTED
DEX_HEADER_PARSED
DEX_VALIDATION_COMPLETED
DEX_ANALYSIS_COMPLETED
DEX_ANALYSIS_FAILED
```

## Allowed Levels in MVP 1

```text
INFO
WARN
ERROR
```

`DEBUG` and `TRACE` are outside MVP 1.

## Security Rules

DexPilot must not log by default:

- internal strings extracted from the DEX file;
- byte dumps;
- tokens;
- internal URLs;
- private package names when not strictly necessary;
- raw stacktraces;
- full analyzed file content.

The `analyze --json` output is a product report, not an operational log.
