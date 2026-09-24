# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project state

MessMan is an Android app for running a "mess" (shared living: meal tracking, shared expenses, monthly settlement); `README.md` has the product vision. The user decides what gets built and will say which features to add. Don't start features on your own from the README or from the old implementation in git history.

The code today is only an architecture scaffold: Koin DI, a Navigation 3 app shell, and theming. The single route (`HOME`) renders nothing.

## Commands

```bash
./gradlew :app:assembleDebug          # build debug APK
./gradlew :app:installDebug           # build + install on every connected device/emulator
./gradlew testDebugUnitTest           # JVM unit tests in all modules (src/test)
./gradlew :core:testDebugUnitTest --tests "com.abrarshakhi.messman.core.ExampleUnitTest.addition_isCorrect"   # single test
./gradlew :app:connectedDebugAndroidTest   # instrumented tests (src/androidTest), needs a device
./gradlew :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.abrarshakhi.messman.ExampleInstrumentedTest#useAppContext
./gradlew lintDebug                   # Android lint in all modules
```

No formatter or static analysis (ktlint/detekt/spotless) is configured; `kotlin.code.style=official`.

## Toolchain and build configuration

AGP 9.4 / Gradle 9.6. Several conventions differ from older Android templates:

- **JDK 25 runs Gradle**: `mise.toml` pins `temurin-25`, and `gradle/gradle-daemon-jvm.properties` pins the daemon JVM (auto-provisioned via foojay). Modules compile to Java 17.
- **Built-in Kotlin**: AGP 9 compiles Kotlin itself, so don't apply `org.jetbrains.kotlin.android`. AGP 9.4.1 bundles KGP 2.2.10. The Kotlin plugins declared in the root `build.gradle.kts` raise it to the catalog's `kotlin` / `jetbrainsKotlinJvm` versions, so keep those two equal. Modules apply only `org.jetbrains.kotlin.plugin.compose`, plus `…plugin.serialization` in `:core`.
- **New DSL**: `compileSdk { version = release(37) }`. R8 is toggled with `optimization { enable = … }`, not `isMinifyEnabled`. Keep rules go in `src/main/keepRules/*.keep` (merged automatically); there is no `proguard-rules.pro`.
- **Configuration cache is on**, so custom build logic must be compatible with it.
- **Dependencies** are declared only in `gradle/libs.versions.toml` (`libs.*`). Compose and Koin artifacts are versionless and resolve through their BOMs. Add the BOMs with `platform(...)`: `platform(libs.androidx.compose.bom)`, `platform(libs.koin.bom)`.
- **No Material icons library**: Material3 1.4 no longer pulls in `material-icons-core`, so icons are vector drawables in each module's `res/drawable` (e.g. `ic_home.xml` in `:feature:home`).
- minSdk 26, targetSdk/compileSdk 37.

## Architecture

Dependencies may only point one way: `:app` → `:feature:*` → `:core`. `:core` must never depend on a feature. Features build their UI from core's types (`ScreenChrome`, `BarItemData`, `AppRouteKey`), so a core → feature dependency creates a cycle. Features don't depend on each other either; `:app` connects them. Kotlin sources live in `src/main/kotlin`.

- `:app` (`com.abrarshakhi.messman`) is the entry point and the only module that sees every feature.
  - `MessManApp` calls `startKoin` with `appModules`.
  - `MainActivity` does `setContent { App(messManNavGraph) }`.
  - `navigation/MessManNavGraph.kt` plugs the features into the shell.
- `:core` (`com.abrarshakhi.messman.core`) holds the app shell, navigation, theme, snackbars and `coreModule`. It exposes `navigation3-runtime` as `api`.
- `:feature:<name>` (`com.abrarshakhi.messman.<name>`, e.g. `:feature:home`) holds one feature's screens, ViewModels and Koin module. It also provides the chrome function and tab item that `:app` plugs in (e.g. `homeScreenChrome()`, `homeBottomNavItem`). A feature module needs the `kotlin.compose` plugin, `buildFeatures { compose = true }` and `implementation(project(":core"))`.

**DI (Koin).** `appModules` (`:app`, `di/AppModules.kt`) is the single list passed to `startKoin`. It holds `coreModule` plus each feature's module (e.g. `homeModule`). `coreModule` registers `AppViewModel` (`viewModelOf`) and `SnackbarDispatcher` (`singleOf`). A missing definition only fails at runtime, when `koinViewModel()` or `koinInject()` asks for it.

**App shell.** The call chain is:

1. `App(navGraph)` gets `AppViewModel`, which holds app-wide UI state (currently the `AppTheme` DARK/LIGHT/SYSTEM choice).
2. `MessManTheme(theme)` wraps everything.
3. `AppShell` owns the one `Scaffold` (top bar, bottom bar, FAB, snackbar host) and hosts `AppNavigation` inside it.

Features reach the shell only through core's `AppNavGraph`, which `:app` builds with three parts:
- `entries`: the `entry<Key> { … }` registrations. It receives the back stack, so `:app` can pass screens navigation callbacks instead of screens touching the stack.
- `chrome`: maps each route to a `ScreenChrome`. Its top-bar and FAB lambdas receive a `ChromeScope` (back stack, current route, shared `TopAppBarScrollBehavior`), so screens don't declare their own bars.
- `bottomBarItems`: the tabs.

A tab is itself a route: `BottomBarKey` is a sealed sub-interface of `AppRouteKey`. Core's `BottomBar` handles the tab click itself (`switchTabTo(item.key)`), so `BarItemData` has no `onClick`. `ScreenChrome.bottomBar` picks which tab is highlighted, and `null` hides the bar. `ScreenChrome.immersive` isn't read yet.

**Navigation 3.** The back stack is a plain `SnapshotStateList<AppRouteKey>` from `rememberAppBackStack()`, not `rememberNavBackStack`.
- A custom `listSaver` saves it by JSON-encoding each key with kotlinx.serialization. On restore, entries that fail to decode are dropped, and an empty result falls back to `HOME`.
- Change it through the helpers in `BackStackController.kt`: `navigateTo`, `back` (never pops the root), and `switchTabTo` (replaces the whole stack with one tab root).
- `AppNavigation` renders it with `NavDisplay` plus the saveable-state and ViewModel-store entry decorators. A `koinViewModel()` inside an entry is therefore scoped to that back-stack entry.

Adding a route touches three modules, in dependency order:
1. **Key (`:core`)**: a `@Serializable` object/class in the sealed `AppRouteKey`. Being sealed, it must live in the same package. Tabs also implement `BottomBarKey`. Every key must be serializable because the stack is saved whenever the activity saves state (backgrounding, rotation).
2. **UI (the feature)**: the screen composable, its chrome function, and a `BarItemData` if it's a tab.
3. **Wiring (`:app`, `MessManNavGraph.kt`)**:
   - an `entry<Key>` (`NavDisplay` throws at runtime for a key with no entry)
   - a branch in the `chrome` `when` (a compile error if forgotten)
   - the tab in `bottomBarItems`

A new feature module additionally needs `include(":feature:x")` in `settings.gradle.kts`, `implementation(project(":feature:x"))` in `:app`, and its Koin module in `appModules`.

**Snackbars.** Inject `SnackbarDispatcher` and call `show()` or `showError()` (long duration plus a dismiss action). `AppShell` collects and displays them, so screens don't hold a `SnackbarHostState`.

**Theme.** `MessManTheme` uses dynamic color by default on Android 12+, which overrides the palette in `Color.kt`. The XML `Theme.MessMan` in `:app` only styles the window before Compose draws.

## Previous implementation

Git history before commit `7ce1702` holds an earlier Java/XML Views/Firebase version of the app (package `com.github.abrarshakhi.mmap`). Consult it only when the user asks; it isn't the design basis for this rewrite.
