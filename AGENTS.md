# Repository Guidelines

## Project Structure & Module Organization
- Root modules: `app` (Android app), `domain` (business logic), `data` (network/DataStore), UI features (`feature-search`, `feature-bookmark`, `feature-settings`), and shared UI in `common-ui`.
- Standard Android folders per module: `src/main`, `src/test`, `src/androidTest`, plus `res` or `assets` where applicable.
- Namespaces: `com.jm.metrostationalert` (app) and `kr.jm.*` (libraries). Keep new code within the appropriate module and package.

## Build, Test, and Development Commands
- Build debug APK: `./gradlew :app:assembleDebug` (all modules compile).
- Install on device/emulator: `./gradlew :app:installDebug` (requires a running device).
- Run unit tests (all): `./gradlew testDebugUnitTest`.
- Run unit tests (module): `./gradlew :domain:testDebugUnitTest` (example per-module).
- Run instrumentation tests: `./gradlew :app:connectedDebugAndroidTest` (device/emulator required).
- Lint (Android Lint): `./gradlew :app:lintDebug` or `./gradlew lint` for all.

## Coding Style & Naming Conventions
- Language: Kotlin, Jetpack Compose for UI, Hilt for DI.
- Indentation: 4 spaces; line endings UTF-8; avoid wildcard imports.
- Names: classes/files PascalCase (`SearchViewModel.kt`), functions/vars lowerCamelCase, constants UPPER_SNAKE_CASE, packages all lowercase dot-separated.
- Suffixes: `...ViewModel`, `...Repository`, `...UseCase`. Compose previews end with `Preview`.
- Keep feature UI in `feature-*`, reusable UI in `common-ui`, business rules in `domain`, and IO/Retrofit/DataStore in `data`.

## Testing Guidelines
- Locations: unit tests in `src/test`, instrumentation in `src/androidTest`.
- Frameworks: JUnit4; MockK + kotlinx-coroutines-test for unit tests; Turbine for Flow; Espresso and Compose UI Test for UI.
- Naming: mirror source path; test classes end with `Test` (e.g., `SearchUseCaseTest`). Prefer small, deterministic tests.
- Run before PR: `./gradlew testDebugUnitTest :app:lintDebug` and, if UI changes, `:app:connectedDebugAndroidTest` on a device.

## Commit & Pull Request Guidelines
- Branches: `feature/<scope>`, `fix/<scope>`, `chore/<scope>`.
- Commits: imperative, concise (English or Korean ok), reference issues like `#12` when relevant.
- PRs: include purpose, affected modules, screenshots/GIFs for UI changes, steps to test, and linked issues. Ensure CI tasks above pass locally.

## Security & Configuration Tips
- Do not commit secrets. Keep keys in `local.properties`/`gradle.properties` and access via `BuildConfig` or DI.
- Project targets SDK 36 and Java 11; ensure local Android SDK matches. Use Android Studio for running and inspecting Compose previews.

