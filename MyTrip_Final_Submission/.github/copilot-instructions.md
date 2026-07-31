# MyTrip AI Development Context

## Project purpose

MyTrip is a budget road-trip planning application. It allows a traveler to
enter trip information, estimate expenses, compare the estimated total against
a budget, and save, open, edit, update, and delete trips.

## Technology stack

- Java 21
- Spring Boot
- Gradle
- Spring Data JPA
- H2 embedded database
- Jakarta Validation
- HTML, CSS, and JavaScript
- JUnit, Mockito, and MockMvc

Use the dependency versions already defined in build.gradle. Do not upgrade or
replace dependencies unless a verified build failure requires it.

## Application architecture

The backend follows this flow:

TripController -> TripService -> TripRepository -> H2 database

Important classes include:

- com.mytrip.MytripApplication
- com.mytrip.model.Trip
- com.mytrip.controller.TripController
- com.mytrip.service.TripService
- com.mytrip.repository.TripRepository
- com.mytrip.exception.TripNotFoundException

The active frontend files are under:

src/main/resources/static/

Do not treat root-level legacy HTML, CSS, or JavaScript files as the active
Spring Boot frontend.

## Required behavior

The application must support:

- Creating a trip
- Saving a trip
- Listing trips belonging to an ownerEmail
- Opening a saved trip
- Editing and updating a saved trip
- Deleting a saved trip
- Fuel-cost calculation
- Total-cost calculation
- Budget comparison
- Route and map display
- Role-specific interface behavior

External hotel, activity, gas, reservation, payment, vendor, and administrator
services may be simulated for demonstration purposes.

## Implementation rules

- Preserve the existing controller-service-repository architecture.
- Make minimal, focused changes.
- Do not invent external API integrations.
- Do not add secrets, API keys, or credentials.
- Preserve ownerEmail filtering.
- Preserve existing REST endpoint behavior unless a defect requires correction.
- Validate invalid numeric values.
- Do not remove working tests merely to obtain a passing build.
- Explain every file changed.

## Verification

Before declaring a task complete:

1. Run:
   .\gradlew.bat clean test

2. Confirm all automated tests pass.

3. For application changes, run:
   .\gradlew.bat bootRun

4. Report:
   - Files inspected
   - Files changed
   - Commands executed
   - Test results
   - Any remaining limitations