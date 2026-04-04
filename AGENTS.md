# AGENTS Guide

This repository is a Gradle multi-module Java library for Minecraft Bedrock protocol support.

Use this file as the default operating guide for coding agents working in `C:\Users\user\git\Protocol`.

## Repository Shape

- Build system: Gradle Kotlin DSL via wrapper (`./gradlew`)
- Language: Java
- Java toolchain: 8
- Test framework: JUnit 5 (`useJUnitPlatform()`)
- Root modules: `adventure`, `bedrock-codec`, `bedrock-connection`, `common`
- CI PR build command: `./gradlew build`
- Encoding: UTF-8 for Java compilation
- Compiler flag: `-parameters`
- Lombok is enabled across subprojects

## Rule Files

- No `.cursorrules` file exists in this repository.
- No `.cursor/rules/` directory exists in this repository.
- No `.github/copilot-instructions.md` file exists in this repository.
- If any of those files are added later, treat them as higher-priority repository instructions.

## Working Directory

- Run commands from the repository root unless there is a strong reason not to.
- Prefer the Gradle wrapper instead of a system Gradle install.
- In bash environments use `./gradlew ...`.
- In `cmd.exe` or PowerShell use `gradlew.bat ...`.

## Build Commands

- Full build: `./gradlew build`
- Compile only: `./gradlew classes`
- Run all checks: `./gradlew check`
- Clean build outputs: `./gradlew clean`
- Build one module: `./gradlew :bedrock-codec:build`
- Compile one module: `./gradlew :bedrock-connection:classes`
- Generate Javadocs for one module: `./gradlew :common:javadoc`
- Publish to local Maven: `./gradlew publishToMavenLocal`

## Test Commands

- Run all tests: `./gradlew test`
- Run tests in one module: `./gradlew :adventure:test`
- Run a single test class: `./gradlew :bedrock-codec:test --tests "org.cloudburstmc.protocol.bedrock.test.FlagSerializationTest"`
- Run a single test method: `./gradlew :adventure:test --tests "org.cloudburstmc.protocol.bedrock.test.TextSerializationTest.testLegacyTranslationSerialization"`
- Re-run tests even if up to date: `./gradlew :bedrock-codec:test --tests "org.cloudburstmc.protocol.bedrock.test.FlagSerializationTest" --rerun`
- Stop after first failure: `./gradlew test --fail-fast`
- Dry-run test selection: `./gradlew :adventure:test --tests "org.cloudburstmc.protocol.bedrock.test.TextSerializationTest" --test-dry-run`

## Lint And Formatting

- There is currently no dedicated lint task such as Checkstyle, SpotBugs, PMD, Spotless, or ktlint.
- There is currently no repository `.editorconfig`.
- `./gradlew check` currently means Gradle verification tasks, which in this repo is primarily test execution.
- For CI parity, prefer `./gradlew build` before finishing a substantial change.
- When changing only one module, at minimum run that module's `test` task or a targeted `--tests` command.

## Safe Default Validation

- Small code change in one module: run that module's focused test task.
- Serializer or codec change: run the relevant module tests and any affected single-test reproducer.
- Cross-module API change: run `./gradlew build`.
- Dependency or build logic change: run `./gradlew build` from the root.

## Source Layout Conventions

- Production sources live under `*/src/main/java/...`.
- Tests live under `*/src/test/java/...`.
- Package names are all-lowercase and rooted at `org.cloudburstmc.protocol`.
- Core protocol packet models live under `bedrock-codec/.../packet`.
- Version-specific codec implementations live under paths like `codec/v776`, `codec/v898`, etc.
- Common reusable utilities live under `common`.

## Naming Conventions

- Types use standard Java PascalCase.
- Methods and fields use camelCase.
- Constants use `UPPER_SNAKE_CASE`.
- Versioned protocol classes use suffixes like `_v776`, `_v898`.
- Serializer classes are typically named `SomethingSerializer_vNNN`.
- Packet types are typically named `SomethingPacket`.
- Enums use PascalCase type names and `UPPER_SNAKE_CASE` members.
- Prefer descriptive names tied to protocol concepts over generic helper names.

## Imports

- Follow the existing file-local import style instead of forcing a new style.
- The common observed order is:
- `package` declaration
- non-static imports
- `java` and `javax` imports grouped with other non-static imports, usually separated by a blank line from project/library imports
- static imports last, separated by a blank line
- Wildcard imports already exist in this repository and are accepted in some files.
- Do not churn imports just to normalize style unless you are already editing the file meaningfully.

## Formatting Conventions

- Match the surrounding file exactly.
- Use 4-space indentation.
- Keep braces and wrapping in the existing Java style used by the touched file.
- Preserve blank-line rhythm around fields, methods, and logical sections.
- Avoid large-scale formatting-only diffs.
- Keep comments minimal and useful.
- Prefer ASCII unless the file already requires non-ASCII content.

## Type And API Conventions

- Maintain Java 8 compatibility.
- Prefer explicit concrete types when they improve protocol readability.
- Use generics carefully; this codebase relies on typed packet/serializer APIs.
- Preserve existing public APIs unless the change explicitly requires an API break.
- Keep versioned codec behavior isolated to the relevant `vNNN` classes when possible.
- Reuse existing abstractions such as `BedrockCodec`, `BedrockCodecHelper`, `BedrockPacketSerializer`, and `PacketSerializer` instead of inventing parallel ones.

## Lombok Usage

- Lombok is standard in this repository.
- Existing code commonly uses `@Data`, `@Value`, `@Getter`, `@Setter`, `@RequiredArgsConstructor`, and `@UtilityClass`.
- Many packet/data classes also use `@EqualsAndHashCode(doNotUseGetters = true)` and `@ToString(doNotUseGetters = true)`.
- When editing a Lombok-backed class, preserve the current Lombok approach unless there is a concrete reason to change it.

## Nullability And Preconditions

- This repo uses Checker Framework nullness annotations such as `@NonNull`, `@Nullable`, and `@NonNegative`.
- Continue annotating APIs when the surrounding code already does so.
- Existing code uses both `Objects.requireNonNull(...)` and `Preconditions.check...(...)`.
- Prefer the style already used in the file you are touching.
- Use `checkArgument` for invalid caller input.
- Use `checkState` or `IllegalStateException` for invalid object state.
- Return `null` only where the existing API already models absence that way.

## Error Handling

- Throw specific exceptions with concrete messages.
- Preserve existing exception semantics in protocol code.
- Serialization/deserialization code often wraps failures in `PacketSerializeException` at higher layers.
- Unsupported protocol branches commonly throw `UnsupportedOperationException`.
- Assertion-style failures are used in a few unreachable paths such as impossible clone failures; do not introduce them casually.
- Do not swallow exceptions silently.
- When logging, keep messages protocol-specific and concise.

## Protocol And Versioning Guidance

- Be careful with wire compatibility; tiny field-order changes can break the protocol.
- Prefer minimal diffs in serializers and codec helpers.
- When adding support for a new protocol version, mirror the existing `vNNN` directory pattern.
- When behavior differs by version, keep the difference local to the versioned codec/serializer layer.
- Avoid changing older version implementations unless the fix truly applies there too.

## Testing Guidance

- Add or update focused tests near the affected module.
- Prefer a targeted regression test when fixing serializer or packet behavior.
- Existing tests are straightforward JUnit 5 tests; follow that style.
- Use real codec helpers and serializers where practical instead of over-mocking.
- For protocol round-trip issues, prefer serialize/deserialize round-trip assertions.

## Change Management

- Keep changes minimal and localized.
- Avoid broad refactors unless the task explicitly calls for them.
- Do not edit unrelated modules just because you notice style inconsistencies.
- If there is no lint task enforcing a rule, treat observed source patterns as the source of truth.
- Before concluding substantial work, run the narrowest convincing validation first, then widen to `./gradlew build` when the scope warrants it.

## Good Agent Defaults

- Read the touched module's build file and nearby code before editing.
- Prefer extending existing patterns over introducing new abstractions.
- Mention the exact Gradle command you used when reporting validation.
- If validation is skipped, state that explicitly.
- If a task affects public protocol behavior, call out compatibility risk in your summary.
