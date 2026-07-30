# MyTrip — AI Build Context & Requirements Specification

## How to use this document
Paste this entire file as the first message to an AI coding assistant (Claude,
ChatGPT, Claude Code, Cursor, etc.). It contains everything the assistant needs:
the product requirements, the exact tech stack and dependency versions, the data
model and API contract, setup/run instructions, and a recommended build order.
Work through **Section 9 (Suggested AI Build Order)** one slice at a time —
generate a layer, run the tests, then move to the next layer — instead of asking
for the whole app in one shot. Keep this file open/pasted in every session so
field names, formulas, and endpoint paths never drift between files.

This spec mirrors and expands the original one-page "MyTrip Project
Requirements" brief (Project Goal / Required Inputs / Required Features /
Simulated Features / Main Entity) into something an AI assistant can build
directly against.

---

## 1. Project Goal

Build a web-based **budget road-trip planning prototype**. A traveler enters a
route (start, destination, optional waypoint), a target budget, vehicle info,
and lodging/activity selections. The app estimates fuel cost, drive time, and
total trip cost, compares that total against the traveler's budget, and lets
the traveler save, reopen, and delete trip plans. It is a class-project-grade
**prototype**, not a production travel-booking platform — real bookings,
payments, and vendor data are explicitly out of scope and are simulated.

## 2. User Roles

| Role | Access |
|---|---|
| Guest | Can plan a route and see cost estimates; cannot save trips |
| Traveler (demo login) | Guest capabilities + save/reopen/delete their own trips, filtered by email |
| Admin / vendor portal (stretch, optional) | Simulated back-office view for approving mock vendor/traveler verifications — not in original scope, but a reasonable stretch feature if you want to mirror a fuller reference build |

## 3. Required Inputs

| Input | Type | Notes |
|---|---|---|
| Starting location | text | free text, e.g. "Austin, TX" |
| Destination | text | required |
| Waypoint | text | **optional**, no validation if blank |
| Target budget | decimal | ≥ 0 |
| Number of travelers | integer | used for display/cost-per-person math, not persisted validation |
| Route distance | decimal (miles) | > 0, manually entered |
| Vehicle MPG | decimal | > 0 |
| Fuel price per gallon | decimal | ≥ 0 |
| Selected lodging | selection from simulated list | drives lodging cost |
| Selected activities | multi-select from simulated list | drives activity cost |

## 4. Required Features

- Demonstration traveler login (email-only, no real password/auth backend)
- Google Maps route display (embedded map + external "open in Google Maps" link)
- Manual route-distance entry (no live routing API call required)
- Fuel-cost calculation
- Estimated drive time calculation
- Total-cost calculation (fuel + lodging + activities)
- Budget comparison (over/under budget, with the dollar difference)
- Save a trip
- Reopen a saved trip
- Delete a trip
- Filter saved trips by traveler email
- Store trips in an H2 database
- Validate mileage and budget values (client-side UX validation **and** server-side enforcement)
- Prevent logged-out (guest) users from saving a trip

## 5. Simulated Features (explicitly mocked, not real integrations)

- Lodging information (a small in-memory list of hotel-style options with price/rating)
- Attraction/activity information (a small in-memory list of paid activities)
- Gas-station information (a small in-memory list with illustrative prices)
- Reservations (a "book now" action against the mock lodging/activity data)
- Booking confirmations (a generated reference code, no real inventory system)
- Payments (a fake card-number field; nothing is charged or transmitted)

None of these should call a real third-party booking, payments, or inventory
API. They exist to make the UI feel complete without adding real integration
risk to a class project timeline.

## 6. Main Entity: `Trip`

A trip stores traveler info, route info, cost estimates, budget info, and
timestamps.

| Field | Type | Validation | Notes |
|---|---|---|---|
| `id` | Long | generated (IDENTITY) | primary key |
| `tripName` | String | `@NotBlank` | |
| `ownerEmail` | String | `@NotBlank` | acts as the traveler's identity key for filtering — no password is checked against it, by design (demo auth) |
| `startLocation` | String | `@NotBlank` | |
| `waypoint` | String | none (optional) | |
| `destination` | String | `@NotBlank` | |
| `distanceMiles` | BigDecimal | `@NotNull`, `> 0` | |
| `vehicleMpg` | BigDecimal | `@NotNull`, `> 0` | |
| `fuelPrice` | BigDecimal | `@NotNull`, `>= 0` | |
| `budget` | BigDecimal | `@NotNull`, `>= 0` | |
| `lodgingCost` | BigDecimal | `@NotNull`, `>= 0` | |
| `activityCost` | BigDecimal | `@NotNull`, `>= 0` | |

**Two-layer validation pattern** (recommended, and worth telling the AI
explicitly so it doesn't collapse this into one layer):
1. Numeric setters throw `IllegalArgumentException` immediately for values
   that can *never* be valid (e.g. a negative distance), so a half-built
   object can't silently hold nonsense.
2. Full "is this request completable" validation (blank names, missing
   required numeric fields) is enforced via Jakarta Bean Validation
   annotations, triggered only where it matters — at the controller boundary
   via `@Valid` — so unit tests can still build partial `Trip` objects freely.

**Calculated fields** (derived, not stored as separate columns — computed
on demand, all using `BigDecimal` for money-safe math):

```
gallonsRequired        = distanceMiles / vehicleMpg                      (scale 4, HALF_UP)
fuelCost                = gallonsRequired * fuelPrice                     (scale 2, HALF_UP)
totalCost                = fuelCost + lodgingCost + activityCost          (scale 2, HALF_UP)
budgetDifference        = budget - totalCost                              (scale 2, HALF_UP)
isUnderBudget            = budgetDifference >= 0
estimatedDriveTimeHours = distanceMiles / 55        // assumed avg speed 55 mph
estimatedMinutes        = round(estimatedDriveTimeHours * 60)
```

## 7. REST API Contract

Base path: `/api/trips`

| Method | Path | Body | Success | Failure | Notes |
|---|---|---|---|---|---|
| POST | `/api/trips` | `Trip` JSON | `201 Created` + saved Trip | `400 Bad Request` | validated via `@Valid` |
| GET | `/api/trips` | — | `200 OK` + `Trip[]` | — | all trips |
| GET | `/api/trips?ownerEmail=x@y.com` | — | `200 OK` + `Trip[]` | — | filtered by owner |
| GET | `/api/trips/{id}` | — | `200 OK` + Trip | `404 Not Found` | |
| PUT | `/api/trips/{id}` | `Trip` JSON | `200 OK` + updated Trip | `404 Not Found` | keeps original id |
| DELETE | `/api/trips/{id}` | — | `204 No Content` | `404 Not Found` | |

A single `@ExceptionHandler` for a custom `TripNotFoundException` should
translate "not found" into a `404` instead of letting it bubble up as a
`500`.

## 8. Tech Stack & Dependencies

### Backend
- **Language:** Java 21 (set via the Gradle `toolchain`)
- **Framework:** Spring Boot **3.3.0**
  - `org.springframework.boot:spring-boot-starter-web` — REST + embedded Tomcat
  - `org.springframework.boot:spring-boot-starter-data-jpa` — Hibernate/JPA
  - `org.springframework.boot:spring-boot-starter-validation` — Jakarta Bean Validation
  - `com.h2database:h2` (**runtime only**) — embedded/file-based database
  - `org.springframework.boot:spring-boot-starter-test` (**test only**) — JUnit 5, Mockito, MockMvc, AssertJ
  - `org.junit.platform:junit-platform-launcher` (**test runtime only**)
- **Build tool:** Gradle, via the committed wrapper (`gradlew` / `gradlew.bat`) —
  no local Gradle install required. `io.spring.dependency-management` plugin
  pins transitive versions to the Spring Boot BOM, so you generally don't
  need to hand-pick versions for the starters above.
- **Persistence:** H2 file-based database (`jdbc:h2:file:./data/mytrip`),
  `spring.jpa.hibernate.ddl-auto=update`, H2 web console enabled at
  `/h2-console`.

### Frontend
- Plain **HTML5 / CSS3 / vanilla JavaScript (ES6)** — no framework, no
  bundler, no `npm install`, no build step.
- **Google Fonts** CDN (e.g. DM Sans) for typography.
- **Google Maps without an API key**: use the free
  `https://maps.google.com/maps?saddr=...&daddr=...&output=embed` iframe for
  the route preview, plus a
  `https://www.google.com/maps/dir/?api=1&origin=...&destination=...` link
  that opens full directions in a new tab. This avoids registering a Google
  Cloud project or enabling billing, which the paid Maps JavaScript/Directions
  API would require.
- Suggested design tokens (CSS custom properties) for a clean
  travel/fintech look: a navy primary (`#1e395b`), a sky-blue accent
  (`#4aacd9`), an off-white app background, small border radii (2–6px), and
  soft box-shadows.

### Tooling
- Git + GitHub for version control.
- Any Java IDE (IntelliJ IDEA, VS Code + Java extensions).
- A modern browser (Chrome/Firefox/Edge) — no server required to preview the
  static frontend files.

## 9. Prerequisites (what to install locally)

1. **JDK 21** (e.g. Eclipse Temurin 21) — required by the Gradle toolchain.
2. **Git**.
3. A browser. Node/npm is **not** required for the frontend as specified
   (no framework, no bundler).
4. Gradle itself is **not** required — the wrapper (`gradlew`) downloads the
   correct Gradle version automatically on first run.

## 10. Getting the Project & Running It

```bash
# 1. Clone
git clone <your-repo-url>
cd <your-repo>

# 2. (macOS/Linux only) make the Gradle wrapper executable if needed
chmod +x gradlew

# 3. Run the backend (from the Spring Boot project directory)
./gradlew bootRun          # Windows: gradlew.bat bootRun
# Starts on http://localhost:8080 by default (no server.port override)

# 4. Run the backend test suite
./gradlew test

# 5. Inspect the H2 database while the app is running
# Open http://localhost:8080/h2-console in a browser
#   JDBC URL: jdbc:h2:file:./data/mytrip
#   User:     sa
#   Password: (blank)

# 6. Preview the frontend prototype
# Just open index.html directly in a browser, OR serve it statically:
npx serve .                # any static file server works — no build step
```

**Known integration gap to flag / fix if you build this out further:** if the
frontend prototype is developed with local in-memory JS mock arrays (as a UX
scaffold) before it's wired to the real API, remember to (a) replace the mock
arrays with `fetch()` calls to `http://localhost:8080/api/trips`, and
(b) add CORS support on the backend (`@CrossOrigin` on the controller, or a
`WebMvcConfigurer` bean) once the frontend and backend are served from
different origins/ports.

## 11. Suggested Project Structure

```
project-root/
├── backend/                          (Spring Boot / Gradle project)
│   ├── build.gradle
│   ├── settings.gradle
│   ├── gradlew, gradlew.bat, gradle/
│   ├── src/main/java/com/mytrip/
│   │   ├── MytripApplication.java
│   │   ├── model/Trip.java
│   │   ├── repository/TripRepository.java
│   │   ├── service/TripService.java
│   │   ├── controller/TripController.java
│   │   └── exception/TripNotFoundException.java
│   ├── src/main/resources/application.properties
│   └── src/test/java/com/mytrip/
│       ├── model/TripTest.java
│       ├── service/TripServiceTest.java
│       └── controller/TripControllerTest.java
├── frontend/
│   ├── index.html
│   ├── main.js
│   └── style.css
└── docs/                             (course deliverables, optional)
    ├── README.md
    ├── uml/                          (class diagram)
    └── javadoc/                      (generated via ./gradlew javadoc)
```

## 12. Non-Functional Requirements

- **Validation:** every write endpoint must reject invalid payloads with
  `400` and a useful message (see the two-layer pattern in Section 6).
- **Testing:** JUnit 5 across all three layers —
  - Model tests: calculation methods (fuel cost, total cost, budget
    difference, drive time) and setter-level guard clauses.
  - Service tests: CRUD behavior and the "not found" business rule, with the
    repository mocked (Mockito).
  - Controller tests: `@WebMvcTest` + `MockMvc`, one test per endpoint
    covering both the happy path and the relevant error status.
- **Security caveat (be explicit about this, don't quietly "fix" it):** this
  is a prototype. The demo login is intentionally not a real auth system —
  `ownerEmail` is just a value the client sends, not a verified identity.
  Don't imply real security guarantees anywhere in the UI copy or docs.

## 13. Frontend / UX Spec

- Single-page, tab-based navigation: **Home → Plan Route → Route Options →
  Summary → Account → (optional) Admin Portal**.
- **Demo login:** a simple email field logs a traveler in (no password
  check) plus one-click "demo traveler" / "demo vendor" buttons for fast
  grading/demo purposes. Logged-out (guest) users can plan and preview costs
  but the "Save Trip" action must be disabled/blocked.
- **Plan Route view:** start/destination/waypoint fields, budget, MPG,
  vehicle type, manual distance entry, live fuel-cost recalculation, and the
  embedded Google Maps preview described in Section 8.
- **Route Options view:** the mocked hotel, gas-station, and activity lists;
  selecting a hotel and toggling activities updates the running total.
- **Summary view:** fuel cost + lodging + activities = total, compared
  against budget, with a clear over/under-budget indicator.
- **Booking modal (simulated):** collects a name and a fake card number,
  "confirms" with a generated reference code — no real payment processing.

## 14. How AI Can Be Used to Build Every File in This Project

The project decomposes cleanly into vertical layers, and each layer is a
good unit of AI-assisted work: generate it, run the tests, then move on.
This mirrors how the reference build actually progressed (basic UI →
descriptive index page → full prototype site → working Spring Boot app →
tests → integration merge → final docs/UML/Javadoc → packaged submission).

**General method for every step below:** paste this whole context document
into the AI's context first, then ask for exactly one file/class (or one
tightly related pair, like a class + its test). Reviewing and running each
piece before asking for the next keeps AI-introduced drift (wrong field
names, invented endpoints, mismatched formulas) from compounding across
files.

1. **Scaffolding** — Prompt: *"Using the tech stack in Section 8, generate a
   Spring Boot 3.3.0 Gradle project skeleton: `build.gradle`,
   `settings.gradle`, `application.properties` configured for a file-based H2
   database with the console enabled, and the `@SpringBootApplication` main
   class."*
2. **Domain model** — Prompt: *"Using the field table and calculated-field
   formulas in Section 6, generate the JPA `Trip` entity with Jakarta
   Validation annotations and the calculation methods. Use `BigDecimal` for
   all monetary/distance math, with the exact rounding rules given."* Giving
   the AI the formulas verbatim (rather than describing them in prose) is
   what keeps the generated math from silently diverging from what the tests
   expect.
3. **Persistence layer** — Prompt: *"Generate a Spring Data JPA repository
   interface for `Trip` with a finder method for filtering trips by
   `ownerEmail`."*
4. **Service layer** — Prompt: *"Generate a `TripService` with create, get
   all, get by id (throwing a custom not-found exception), get-by-owner,
   update, and delete. Keep Jakarta Bean Validation out of this layer — that
   belongs at the controller boundary via `@Valid` — so the service stays
   usable directly in unit tests against partially-built `Trip` objects."*
5. **Web layer** — Prompt: *"Generate a `@RestController` implementing
   exactly the endpoint table in Section 7, including an
   `@ExceptionHandler` that converts the not-found exception into a 404."*
6. **Tests** — Prompt: *"Generate JUnit 5 tests for [model/service/controller
   — one at a time]: for the controller, use `@WebMvcTest` with `MockMvc` and
   cover every row in the Section 7 endpoint table, both success and error
   cases."* Re-pasting the endpoint table here specifically is what keeps
   generated tests from testing paths or status codes that don't actually
   match the controller.
7. **Frontend prototype** — Prompt: *"Generate a single-page HTML/CSS/JS
   prototype implementing the views in Section 13 and the simulated features
   in Section 5, using in-memory JS arrays for hotels/gas stations/
   activities and the no-API-key Google Maps embed approach from Section
   8."* Building the UI against mock data first lets you validate the UX
   before wiring up real HTTP calls.
8. **Wiring pass (once the prototype UX is approved)** — Prompt: *"Replace
   the mock arrays' trip-saving logic with `fetch()` calls to the `/api/
   trips` endpoints, and add CORS configuration to the backend so the
   frontend (served from a different port) can reach it."*
9. **Deliverables/docs** — Prompt: *"Generate a README covering the setup
   steps in Sections 9–10, and describe a UML class diagram for the
   `model` / `repository` / `service` / `controller` / `exception`
   classes."* Javadoc itself doesn't need an AI prompt — once the classes
   have proper `/** ... */` comments (which you can ask the AI to add while
   generating each class), `./gradlew javadoc` generates it directly.

**Team-project tip:** if more than one person is prompting an AI in
parallel, keep this document as the single shared source of truth and have
one person merge divergent AI output back against it — this is exactly what
an "integration" branch/PR is for when two AI-assisted workstreams
(e.g., backend and frontend) need to be reconciled.

**Academic-integrity tip:** if your course requires disclosing AI usage,
keep your prompts and the AI-generated diffs — your commit history, if you
commit each AI-generated slice separately with a descriptive message, doubles
as that log.

## 15. Definition of Done

- [ ] All Section 4 "Required Features" work end-to-end through the UI
- [ ] All Section 7 endpoints return the documented status codes for both
      valid and invalid input
- [ ] `./gradlew test` passes with coverage across model, service, and
      controller layers
- [ ] Guest users cannot save a trip; logging in (even via demo login)
      unlocks saving
- [ ] Trips persist in the H2 file database across a backend restart
- [ ] Saved trips can be filtered by `ownerEmail`
- [ ] Negative/zero mileage, MPG, or budget values are rejected server-side,
      not just hidden by client-side form constraints
- [ ] No simulated feature (Section 5) makes a real external booking/
      payment call
