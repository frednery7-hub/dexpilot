# DexPilot v0.3.0 Plan — Type IDs + Type Names

## Goal

DexPilot v0.3.0 extends v0.2.0 by parsing DEX `type_ids` and resolving each type descriptor through the existing `string_ids` parsing foundation.

The goal is to add basic semantic inspection while preserving DexPilot as a safe read-only DEX inspection tool.

## Scope

Included in v0.3.0:

- Parse `type_ids` from `type_ids_off`.
- Represent type IDs as domain models.
- Resolve `type_id.descriptor_idx` into a string descriptor.
- Add a safe type summary to text reports.
- Add `typeSummary` to JSON reports.
- Add controlled synthetic fixtures and unit tests.
- Preserve bounded parsing behavior.

Excluded from v0.3.0:

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

## Proposed domain models

- DexTypeData
- DexTypeSummary

## Safety rules

v0.3.0 should remain read-only and bounded.

Rules:

- Do not dump unbounded strings.
- Do not parse classes or methods yet.
- Do not execute bytecode.
- Do not rewrite DEX content.
- Reject invalid type descriptor indexes.
- Keep reports deterministic.
- Keep logs on stderr and reports on stdout.

## Validation rules

v0.3.0 should validate:

- `type_ids_size` zero means no type summary.
- If `type_ids_size` is non-zero, `type_ids_off` must point inside file bounds.
- `type_ids_size * 4` must not overflow or exceed file bounds.
- Each `descriptor_idx` must be within `string_ids_size`.
- Type descriptor resolution must use parsed string samples only when available and safe.
- Controlled fixtures must remain synthetic and minimal.

## Execution stages

The 25%, 50%, 75%, and 100% gates were used for the overall DexPilot MVP 1 project timeline.

For v0.3.0, execution is organized into technical stages, not percentage gates.

Stage A — Planning

- Create this planning document.
- Keep code unchanged.
- Run test/build.
- Commit documentation only.

Stage B — Type IDs parser

- Add type ID models.
- Add type IDs parser.
- Add validation for descriptor indexes.
- Add unit tests.

Stage C — Reporting and fixture

- Extend text report.
- Extend JSON report.
- Add controlled fixture with string_ids + type_ids.
- Validate CLI text and JSON.
- Update README and evidence.
- Run final audit.
- Merge to main after review.
- Tag and release v0.3.0 only if CI passes.

## Branch

Current branch:

- feature/v0.3-type-ids

## Release criteria

v0.3.0 can be released only when:

- Local test passes.
- Local build passes.
- CLI text report passes.
- CLI JSON report passes.
- GitHub Actions passes.
- No generated reports are committed.
- No APK/AAB/env/local.properties files are committed.
