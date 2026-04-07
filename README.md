# CCT Calculation Core

A shared GitHub repo (`cct-calc-core`) under `@Health-Education-England` containing a JSON rule definition for CCT calculation types, plus thin runtime calculation engines in TypeScript and Java that consume those rules. Shared JSON test fixtures validate both implementations produce identical results.

## Design

**Runtime rule reading (not code generation)**: The calculation logic is small (~50 lines per language) and stable. Code generation would add build tool complexity for minimal benefit. A runtime approach keeps each engine simple and idiomatic.

**JSON (not YAML)**: JSON is natively supported by both TS and Java with zero extra parser dependencies.

---

## Repo Structure

```
cct-calc-core/
├── rules/
│   └── calculation-rules.json          # Single source of truth for types
├── fixtures/
│   └── calculation-test-cases.json     # Shared test inputs/expected outputs
├── ts/
│   ├── package.json
│   ├── tsconfig.json
│   ├── src/
│   │   ├── index.ts                    # Public exports
│   │   ├── engine.ts                   # performCalculation, helpers
│   │   └── types.ts                    # Pure input/output types
│   └── __tests__/
│       └── engine.test.ts              # Runs shared fixtures
├── java/
│   ├── build.gradle.kts
│   ├── settings.gradle.kts
│   ├── src/main/java/uk/nhs/tis/cctcalccore/
│   │   ├── CctCalculationEngine.java
│   │   ├── CalculationInput.java
│   │   └── CalculationResult.java
│   └── src/test/java/uk/nhs/tis/cctcalccore/
│       └── CctCalculationEngineTest.java
├── .github/workflows/
│   ├── ci.yml                          # Tests on push/PR
│   └── publish.yml                     # Publish on tag
└── README.md
```

---

## What's Shared vs. What Stays Local

| Shared library                                       | Stays in each consuming app              |
| ---------------------------------------------------- | ---------------------------------------- |
| `performCalculation()` — core date arithmetic        | UI state management (React state, forms) |
| `selectCalculationType()` — draft defaults           | Validation rules (date range checks)     |
| `removeLastCalculation()` — undo logic               | Display formatting                       |
| `resolveCalculationBaseDate()` — baseline resolution | Component/editing/draft logic            |
| Type definitions for inputs/outputs                  | Framework-specific code                  |

---

## Distribution

### TypeScript

Public npm package on GitHub Packages (no auth needed for consumers).

**Consumer `.npmrc`:**

```
@Health-Education-England:registry=https://npm.pkg.github.com
```

**Install:**

```sh
npm install @Health-Education-England/cct-calc-core@1.0.0
```

**Import:**

```ts
import { performCalculation } from "@Health-Education-England/cct-calc-core";
```

### Java

Public Maven package on GitHub Packages.

**Consumer `build.gradle.kts`:**

```kotlin
repositories {
    maven {
        url = uri("https://maven.pkg.github.com/Health-Education-England/cct-calc-core")
    }
}

dependencies {
    implementation("uk.nhs.tis:cct-calc-core:1.0.0")
}
```

---

## Testing

1. **TS tests**: `cd ts && npm test`
2. **Java tests**: `cd java && ./gradlew test`
3. **Cross-language parity**: Both produce identical `daysAdded` and `newCctDate` for every fixture
4. **CI**: GitHub Actions runs both TS and Java tests on every push/PR
5. **Publishing**: Tag a release → GitHub Actions publishes both packages

---

## Decisions

- **JSON over YAML** — no extra parser dependencies
- **Runtime over code generation** — rules are simple and stable
- **Public GitHub Packages under @Health-Education-England** — no consumer auth, proper semver for a big team
- **dayjs (peer dep) for TS, java.time.LocalDate for Java** — follows existing conventions
- **Scope boundary** — only pure calculation logic is shared; UI orchestration stays in consuming apps

## Versioning

Semver — bump minor for new calc types, major for formula changes. Publish via GitHub Actions on tagged releases.
