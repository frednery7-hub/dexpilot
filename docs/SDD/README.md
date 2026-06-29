# DexPilot — Software Design Document

This Software Design Document defines the technical direction, constraints, architecture, and implementation strategy for DexPilot.

## MVP 1 — DEX Inspector

The first MVP focuses on reading and validating the DEX header. It does not modify, optimize, or rewrite DEX files.

## Core Decisions

- Language: Kotlin/JVM.
- Execution model: local CLI.
- Build system: Gradle Kotlin DSL.
- Testing: JUnit 5 + Kotest Assertions.
- Logging: minimal ConsoleLogger through LoggerPort.
- Architecture: Clean Architecture with SOLID constraints.
- MVP 1 output: terminal report and optional JSON report.
- Development location: `/Volumes/KINGSTON/dexpilot`.

## Milestone Policy

DexPilot uses four gated milestones:

```text
25%  - Documentation and structure
50%  - Kotlin CLI foundation
75%  - DEX header parser and validator
100% - MVP 1 final report, fixtures, and evidence
```

Each milestone requires an audit before commit.
