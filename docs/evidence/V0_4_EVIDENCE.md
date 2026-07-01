# V0.4.0 — Evidence: Proto IDs

## Release
v0.4.0 — Proto IDs

## Files Added
- src/main/kotlin/com/dexpilot/domain/dex/model/DexProtoData.kt
- src/main/kotlin/com/dexpilot/domain/dex/model/DexProtoSummary.kt
- src/main/kotlin/com/dexpilot/infrastructure/dex/DexProtoIdsParser.kt
- src/test/kotlin/com/dexpilot/infrastructure/dex/DexProtoIdsParserTest.kt
- src/test/resources/fixtures/valid/minimal-proto-ids.dex
- docs/implementation/V0_4_PLAN.md
- docs/evidence/V0_4_EVIDENCE.md

## Files Modified
- DexValidationCode.kt — added INVALID_PROTO_IDS
- DexFileSummary.kt — added protoSummary
- AnalyzeDexUseCase.kt — integrated DexProtoIdsParser
- TextReportWriter.kt — added Protos section
- JsonReportWriter.kt — added protoSummary
- build.gradle.kts — version bumped to 0.4.0-SNAPSHOT

## Test Results
All tests passing. See CI for full run.

## Sample Output — Text

  Protos:
  Declared protos: 1
  Sample size: 1
  - #0 shorty=V return=Ljava/lang/Object; params_off=0

## Sample Output — JSON

  protoSummary: {
    declaredCount: 1,
    sample: [
      {
        index: 0,
        shortyIndex: 0,
        shortyDescriptor: V,
        returnTypeIndex: 0,
        returnTypeDescriptor: Ljava/lang/Object;,
        parametersOffset: 0
      }
    ]
  }
