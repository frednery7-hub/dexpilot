# 01 — Contexto

DexPilot is an experimental engineering project focused on Android DEX file inspection.

The project exists to study and implement low-level parsing of Android bytecode artifacts using Kotlin/JVM, disciplined architecture, automated tests, and safe reporting practices.

The first implementation target is a local CLI capable of reading a `.dex` file, parsing its header, validating basic structural rules, and producing a technical report.

DexPilot must be positioned as a DEX inspector first, not as an R8 or ProGuard replacement.
