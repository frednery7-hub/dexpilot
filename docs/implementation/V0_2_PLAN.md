# DexPilot v0.2.0 Plan — Map List + String IDs

## Goal

DexPilot v0.2.0 extends MVP 1 beyond header-level inspection by adding controlled parsing of the DEX map_list and string_ids sections.

The objective is still inspection, not rewriting, optimization, obfuscation, deobfuscation, APK repackaging, or bytecode execution.

## Scope

Included in v0.2.0:

- Parse map_list from map_off.
- Represent map_list as domain models.
- Validate map_off and map_list size boundaries.
- Recognize common DEX map item types.
- Parse string_ids.
- Read string_data_item values through ULEB128 length prefix.
- Add a safe string summary to text and JSON reports.
- Add controlled synthetic fixtures and unit tests.

Excluded from v0.2.0:

- Class definitions parsing.
- Method IDs parsing.
- Field IDs parsing.
- Proto IDs parsing.
- Code item parsing.
- Bytecode instruction parsing.
- CFG construction.
- Call graph construction.
- DEX writing or rewriting.
- APK-level analysis.

## Proposed domain models

- DexMapList
- DexMapItem
- DexMapItemType
- DexStringId
- DexStringData
- DexStringSummary

## Safety limits

String parsing must be bounded.

Default limits:

- Do not dump every string by default.
- Show only a small sample in text reports.
- Prefer counts and offsets over full content.
- Truncate long strings in reports.
- Escape control characters.
- Avoid printing raw binary data.
- Never execute DEX content.

Suggested initial constants:

- MAX_STRING_SAMPLE_COUNT = 10
- MAX_STRING_DISPLAY_LENGTH = 120
- MAX_ULEB128_BYTES = 5

## Validation rules

v0.2.0 should validate:

- map_off is zero or inside file bounds.
- If map_off is non-zero, at least 4 bytes exist for map_size.
- map_size does not cause integer overflow.
- Every map_item fits inside file bounds.
- string_ids_off and string_ids_size describe a valid range.
- Every string_data_off points inside file bounds.
- ULEB128 decoding must stop after a bounded number of bytes.

## Execution stages

The 25%, 50%, 75%, and 100% gates were used for the overall DexPilot MVP 1 project timeline.

For v0.2.0, execution is organized into technical stages, not percentage gates.

Stage A — Planning

- Create this planning document.
- Keep code unchanged.
- Run test/build.
- Commit documentation only.

Stage B — Map List

- Add map_list models.
- Add map_list parser.
- Add map_list validation.
- Add tests with controlled bytes.
- Extend reports with map summary.

Stage C — String IDs

- Add string_ids parser.
- Add string_data_item reader.
- Add ULEB128 decoder.
- Add safe string summary.
- Add tests and a controlled fixture.

Stage D — v0.2.0 Finalization

- Update README.
- Update evidence docs.
- Run final audit.
- Merge to main after review.
- Tag and release v0.2.0 only if CI passes.

## Branch

Current branch:

- feature/v0.2-map-list-string-ids

## Release criteria

v0.2.0 can be released only when:

- Local test passes.
- Local build passes.
- CLI text report passes.
- CLI JSON report passes.
- GitHub Actions passes.
- No generated reports are committed.
- No APK/AAB/env/local.properties files are committed.
