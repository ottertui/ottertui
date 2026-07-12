# Contributing to OtterTUI

Thank you for your interest in contributing.

## Getting Started

### Prerequisites

- **JDK 21+** (JDK 23+ recommended; FFM backend requires JDK 22+)
- **Git**

The Gradle wrapper (`gradlew`) bootstraps everything else — no local Gradle installation needed.

### Setup

```bash
git clone https://github.com/ottertui/ottertui.git
cd ottertui
```

On first `./gradlew` invocation, the git pre-commit hook is auto-configured — no manual step required.

### Build

```bash
# Full build with tests, coverage, and style checks
./gradlew build checkstyleMain checkstyleTest

# Run only tests
./gradlew test

# Run tests with coverage report
./gradlew test jacocoTestReport
```

Coverage reports are generated per module at `build/reports/jacoco/test/html/index.html`.

## Project Structure

```
ottertui/
├── buildSrc/                       ← Convention plugins
│   └── src/main/kotlin/
│       ├── ottertui.java-conventions.gradle.kts
│       └── ottertui.jacoco-conventions.gradle.kts
├── ottertui-core/                  ← Buffer, Style, Widget, Layout, Text, InputEvent
├── ottertui-widgets/               ← Block, Paragraph, List, Table, Sparkline, Gauge, Image, ...
├── ottertui-tui/                   ← Component, TuiRunner, KeyBindings, BackendSelector
├── ottertui-toolkit/               ← Fluent DSL, StyleSheet, ThemeManager
├── ottertui-backend-jline/         ← JLine3 backend (recommended)
├── ottertui-backend-aesh/          ← Aesh backend (zero-dependency)
├── ottertui-backend-lanterna/      ← Lanterna backend (JDK 8+ compatible)
├── ottertui-backend-ffm/           ← FFM API backend (JDK 22+)
├── ottertui-examples/              ← Demo applications
├── ottertui-integration-tests/     ← Cross-module integration tests
├── .githooks/                      ← Pre-commit hooks (auto-configured)
├── config/checkstyle/              ← Checkstyle rules
└── gradle/                         ← Version catalog & wrapper
```

## Code Style

OtterTUI uses **Checkstyle** for consistent code formatting. The pre-commit hook runs `checkstyleMain` automatically on every commit. Violations block the commit.

To check manually:

```bash
./gradlew checkstyleMain checkstyleTest
```

Notable rules:
- No star imports
- Import ordering enforced
- K&R braces required
- No trailing whitespace
- One statement per line

## Testing

- **Test framework:** JUnit 5 (Jupiter)
- **Coverage target:** 70% minimum instruction coverage for library modules (core, widgets, tui, toolkit)
- **Backend modules** (jline, aesh, lanterna, ffm) are excluded from coverage requirements

Write tests under `src/test/java/` in the relevant module. Run a single module's tests with:

```bash
./gradlew :ottertui-widgets:test
```

## Pull Request Process

1. **Fork** the repository and create a feature branch from `main`.
2. **Implement** your change, following the code style.
3. **Add tests** for new functionality.
4. **Build and verify**: `./gradlew build checkstyleMain checkstyleTest` must pass.
5. **Submit a PR** to the `main` branch. Fill in the PR template.

The PR template will ask for:
- A summary of the change
- The type (bug fix, new feature, refactoring, documentation, CI/build, dependency update)
- Confirmation that code style, tests, and coverage requirements are met

## Module Conventions

| Module type | Convention plugin |
|---|---|
| Library (core, widgets, tui, toolkit) | `id("ottertui.java-conventions")` + `id("ottertui.jacoco-conventions")` |
| Backend (jline, aesh, lanterna, ffm) | `id("ottertui.java-conventions")` only |
| Examples / integration-tests | `id("ottertui.java-conventions")` only |

When adding a new module, pick the appropriate convention plugin(s). Backend modules do not apply the jacoco plugin — they are excluded from coverage enforcement.

## FFM Backend Note

The `ottertui-backend-ffm` module requires JDK 22+ (`java.lang.foreign` API). It is always included in the build but silently skips compilation on JDK < 22. Its published artifact declares JVM 8 runtime compatibility via `TargetJvmVersion`, so downstream consumers can bundle it with other backends regardless of their JDK version.

## Questions?

Open a [GitHub Discussion](https://github.com/ottertui/ottertui/discussions) or an issue with the `question` label.
