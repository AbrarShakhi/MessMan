# Non-Functional Requirements

How well the app must work, and the constraints it is built under. What the app does is in `docs/FUNCTIONAL_REQUIREMENTS.md`, whose assumption IDs (A1, A2, …) are referenced here.
Targets and limits that nobody has decided yet are marked N1, N2, … and listed in section 10 for review.

## 1. Platform and technology (NFR-PLAT)

These are decided.

- **NFR-PLAT-01** Android only, on Android 8.0 (API 26) or newer, built against API 37.
- **NFR-PLAT-02** Kotlin, Jetpack Compose with Material 3, Koin for dependency injection, and Navigation 3.
- **NFR-PLAT-03** Gradle modules follow `:app` → `:feature:*` → `:core`. Features never depend on each other (see `CLAUDE.md`).
- **NFR-PLAT-04** The backend is Supabase: Auth (email and password), the Postgres database, and Realtime. The app talks to Supabase directly; there is no separate server.
- **NFR-PLAT-05** Supabase has no push notification service, so push needs Firebase Cloud Messaging (or a similar service), triggered from Supabase.
- **NFR-PLAT-06** Time-based rules run on the server on a schedule and must not depend on any phone being online:
  - closing each month at midnight after its last day, and making it final 24 hours later
  - manager handovers and leaving members, which take effect on the 1st
  - late fines and due-date reminders

## 2. Security and privacy (NFR-SEC)

- **NFR-SEC-01** The database itself (Row Level Security and database functions) enforces every rule about who can see or change what, not just the app. This covers:
  - the permission table in FR section 3
  - locks and the grace period
  - one mess per user
  - the balance check for leaving

  Supabase lets the app talk to the database directly, so a rule checked only in the app can be bypassed with a modified app.
- **NFR-SEC-02** A user can read data only from the mess they currently belong to. A member who leaves loses access immediately.
- **NFR-SEC-03** All network traffic uses HTTPS.
- **NFR-SEC-04** Only the Supabase project URL and the public anon key ship in the app. The service-role key never goes into the app or the repository. The keys stay out of git, e.g. in `local.properties`.
- **NFR-SEC-05** Only Supabase Auth handles passwords; the app never stores them. Session tokens stay in app-private storage and are never logged.
- **NFR-SEC-06** Join keys must be hard to guess:
  - at least 8 characters from a 32-character alphabet, about 1 trillion possible keys (N1)
  - wrong-key attempts limited to 5 per user per 15 minutes (N2)
- **NFR-SEC-07** The activity log is append-only. No user, including the owner, can change or delete its entries.
- **NFR-SEC-08** Deleting an account deletes the user's personal data (name, email, phone); past records show "Deleted user" (A3). Google Play requires an in-app way to delete an account, so the app must explain clearly when mess membership blocks deletion.
- **NFR-SEC-09** A privacy policy is published before the Play Store release, because the app stores names, email addresses and phone numbers.

## 3. Data integrity (NFR-DATA)

- **NFR-DATA-01** Money is stored as exact decimal values (e.g. Postgres `numeric`, Kotlin `BigDecimal`), never as `Float` or `Double`. Amounts have 2 decimal places.
- **NFR-DATA-02** The server calculates the meal rate, balances, charges and fines with one set of rules, so every phone shows the same numbers. Offline screens show the last synced numbers.
- **NFR-DATA-03** Time-based rules use the server's clock, not the phone's. This covers month boundaries, locks, the 24-hour grace period, due dates and fines. Days and months follow the mess's time zone (A6).
- **NFR-DATA-04** Uploading the same offline entry twice never creates two entries. Each entry gets its ID on the phone when it is created.
- **NFR-DATA-05** The calculation and date rules have automated tests, including:
  - the example in FR section 12
  - the manager-change example in FR-PAY-06
  - month boundaries: 28-, 29-, 30- and 31-day months, leap years, and midnight in the mess's time zone
- **NFR-DATA-06** Database changes are made through versioned migration files kept in the repository.

## 4. Performance (NFR-PERF)

Measured on a mid-range phone on a 4G connection (N3).

- **NFR-PERF-01** Cold start to a usable screen takes under 2 seconds.
- **NFR-PERF-02** A change made by one member appears on other online members' screens within 3 seconds.
- **NFR-PERF-03** Saving a meal count or a grocery entry responds within 1 second, or shows progress. The UI never freezes during network work.
- **NFR-PERF-04** A monthly report for a 50-member mess loads within 2 seconds.

## 5. Capacity (NFR-CAP)

- **NFR-CAP-01** A mess supports up to 50 members (N4) and 4 meal times a day.
- **NFR-CAP-02** A mess keeps its full history for as long as the mess exists.
- **NFR-CAP-03** The first release targets Supabase's free plan. Before launch, check its limits, such as database size and pausing inactive projects, and upgrade the plan if needed.

## 6. Offline and reliability (NFR-REL)

- **NFR-REL-01** Without a connection, the app still opens and shows the last synced data (FR-SYNC-02).
- **NFR-REL-02** Grocery entries waiting to sync survive app restarts and phone reboots. They retry automatically when the network returns.
- **NFR-REL-03** The app shows clearly:
  - when it is offline
  - which entries are still waiting to sync
  - when its data was last updated
- **NFR-REL-04** No change the server has accepted is ever lost. The database is backed up at least daily; check what the Supabase plan provides.
- **NFR-REL-05** Live updates run only while the app is open. In the background, the app only uploads waiting grocery entries and receives push notifications, to save battery and data.

## 7. Usability and accessibility (NFR-UX)

- **NFR-UX-01** Changing one's own meal count for a meal time takes at most two taps from the home screen (N5).
- **NFR-UX-02** Adding a grocery entry fits on a single screen.
- **NFR-UX-03** The app uses Material 3 design with light and dark themes, and dynamic color on Android 12 and newer.
- **NFR-UX-04** Accessibility:
  - every icon has a content description
  - touch targets are at least 48 dp
  - text follows the system font size
  - colors meet WCAG AA contrast
- **NFR-UX-05** When the server rejects an action, the app explains why in plain words, for example:
  - a locked meal time
  - a closed month
  - an ended grace period
  - an outstanding balance that blocks leaving

## 8. Localization (NFR-L10N)

- **NFR-L10N-01** English and Bangla are available from the first release. All user-visible text lives in string resources, with none hard-coded in Kotlin.
- **NFR-L10N-02** Dates, numbers and money are formatted for the chosen language. Amounts show the mess's currency.
- **NFR-L10N-03** The owner picks the mess currency from a list of ISO 4217 currencies, with BDT as the default.
- **NFR-L10N-04** Layouts handle the longer Bangla text without clipping.

## 9. Maintainability and testing (NFR-MAINT)

- **NFR-MAINT-01** Feature code follows the module and screen structure described in `CLAUDE.md`: Route, Screen, ViewModel, UiState and Action.
- **NFR-MAINT-02** Automated tests prove the access rules. For example, a member can't read or change another mess's data, and a non-manager can't lock a meal time.
- **NFR-MAINT-03** The calculation rules are unit-tested (NFR-DATA-05).

## 10. Assumptions to confirm

| ID | Assumption | Used in |
|---|---|---|
| N1 | Join keys are at least 8 characters from a 32-character alphabet. | NFR-SEC-06 |
| N2 | Wrong join-key attempts are limited to 5 per user per 15 minutes. | NFR-SEC-06 |
| N3 | Performance is measured on a mid-range phone on 4G. | NFR-PERF |
| N4 | A mess has at most 50 members. | NFR-CAP-01 |
| N5 | Changing a meal count takes at most two taps from the home screen. | NFR-UX-01 |
