# MyTrip Requirements Context

## Purpose of This File

This file summarizes the intended requirements for MyTrip and clarifies which
requirements are implemented directly, simulated for demonstration, or require
verification against the current source code.

The current source code is the final authority when older documentation
conflicts with the integrated application.

## Functional Requirements

### FR-01: Create a Road Trip

The system shall allow a traveler to create a new road-trip plan.

### FR-02: Manage Road Trips

The system shall allow a traveler to view and manage existing road-trip plans.

### FR-03: Enter Trip Locations

The system shall allow a traveler to enter:

- A starting location
- An optional waypoint
- A destination

### FR-04: Validate Trip Locations

The system shall require valid starting and destination values before
completing the main trip-planning workflow.

### FR-05: Display Travel Route

The system shall display a visual route between the selected locations.

Google Maps is used for visual route display. The mileage used in application
calculations is entered manually by the traveler.

### FR-06: Display Hotel Options

The system shall display affordable hotel options related to the destination or
route.

The current hotel listings are simulated. The application does not perform a
live commercial hotel search.

### FR-07: Select Lodging

The traveler shall be able to review or select a lodging option and include its
cost in the trip estimate.

### FR-08: Display Gas-Station Options

The system shall display gas-station options associated with the planned route.

The current gas-station listings are simulated.

### FR-09: Display Activity Options

The system shall display activity or attraction options that can be included in
the trip estimate.

The current activity listings are simulated.

### FR-10: Estimate Fuel Cost

The system shall estimate fuel cost using:

- Route distance
- Vehicle miles per gallon
- Fuel price per gallon

The calculation is:

Gallons Required = Route Distance / Vehicle MPG

Estimated Fuel Cost = Gallons Required × Fuel Price Per Gallon

Distance and MPG must be greater than zero.

### FR-11: Enter a Trip Budget

The system shall allow the traveler to enter a target budget.

The budget must be a valid numeric value greater than zero.

### FR-12: Calculate Total Trip Cost

The system shall calculate:

Total Estimated Cost =
Fuel Cost + Lodging Cost + Activity Cost

### FR-13: Compare Cost With Budget

The system shall calculate:

Budget Difference =
Budget − Total Estimated Cost

### FR-14: Display Budget Status

The system shall identify the trip as within budget when the budget difference
is zero or positive.

The system shall identify the trip as over budget when the budget difference is
negative.

### FR-15: Estimate Drive Time

The system shall estimate drive time using manually entered route distance and
an assumed average speed.

The current planning estimate uses approximately 55 miles per hour:

Estimated Drive Time =
Route Distance / 55 MPH

The result does not account for traffic, construction, weather, rest stops, or
other live conditions.

### FR-16: Split Estimated Cost

The system may display a per-person estimate by dividing the total estimated
cost by a valid traveler count.

The traveler count must be greater than zero.

### FR-17: Save a Trip Plan

A logged-in demonstration traveler shall be able to save a valid trip.

A guest user shall not be allowed to save a trip and shall be instructed to
sign in.

### FR-18: Persist Saved Trips

Saved trips shall be stored in the file-based H2 database.

A saved trip shall remain available after a browser refresh and may remain
after the application restarts, provided the same local database file is used.

### FR-19: Open a Saved Trip

A traveler shall be able to open a previously saved trip and restore its stored
information for review.

Restored information may include:

- Starting location
- Waypoint
- Destination
- Route mileage
- Vehicle MPG
- Fuel price
- Budget
- Lodging cost
- Activity cost
- Estimated total
- Budget status
- Other stored trip fields

### FR-20: Edit and Update a Saved Trip

The intended requirement is for a traveler to edit a saved trip and save the
updated information.

An older system report states that this workflow was incomplete at the time the
report was created. The current source code must be inspected to determine the
final status.

Do not assume editing is absent without reviewing:

- The frontend editingTripId behavior
- POST versus PUT request selection
- TripController update endpoints
- TripService update behavior
- Current tests
- Manual application behavior

### FR-21: Delete a Saved Trip

The traveler shall be able to delete a selected saved trip.

After successful deletion, the trip shall no longer appear in the saved-trip
list.

### FR-22: Filter Trips by Traveler Email

The system shall associate saved trips with ownerEmail.

When a traveler is logged in, the account page shall request and display only
trips belonging to that traveler’s email.

This is prototype-level data separation. It does not replace production
authentication and authorization.

### FR-23: Display a Trip Summary

The system shall display a summary containing relevant trip information,
including:

- Starting location
- Destination
- Route information
- Fuel cost
- Lodging cost
- Activity cost
- Total estimated cost
- Target budget
- Budget difference
- Budget status
- Per-person estimate when applicable

### FR-24: Handle Invalid Input

The system shall reject invalid or missing required values.

Important validation cases include:

- Missing starting location
- Missing destination
- Missing mileage
- Zero mileage
- Negative mileage
- Nonnumeric mileage
- Zero MPG
- Negative MPG
- Invalid fuel price
- Missing budget
- Zero budget
- Negative budget
- Nonnumeric budget
- Invalid traveler count
- Attempting to save while logged out

Observed messages include:

- “Enter a valid route distance greater than zero.”
- “Error: Invalid target budget.”

### FR-25: Handle Missing Trips

When a requested trip does not exist, the backend shall raise or return an
appropriate missing-trip error.

TripNotFoundException is the project-specific exception used for missing Trip
records.

## Demonstration Account Requirements

The application uses demonstration accounts rather than production
authentication.

Acceptance testing used accounts such as:

- traveler@example.com
- traveler2@example.com

Account and role behavior may be stored in browser localStorage and represented
through frontend controls.

Do not describe this as secure authentication.

## Non-Functional Requirements

### Reliability

- Valid trip data should produce repeatable calculations.
- Saved trips should persist through browser refreshes.
- The application should not crash on common invalid input.
- Missing records should produce understandable errors.

### Robustness

- Required fields must be validated.
- Invalid numeric values must be rejected.
- The application should provide clear user-facing messages.
- Tests must not be removed or bypassed.

### Performance

- The local application should start and respond within a reasonable time.
- Route display may depend on internet access and Google Maps availability.
- Cost calculations are performed locally and should complete immediately.

### Maintainability

- Preserve separation between model, controller, service, and repository.
- Keep frontend resources under src/main/resources/static/.
- Keep business rules out of the repository.
- Keep database access out of the controller.
- Use Git for version control.
- Maintain Javadocs for important public classes and methods.

### Usability

- Inputs and buttons should be clearly labeled.
- Error messages should explain what must be corrected.
- A first-time user should be able to build and review a basic trip plan.
- The interface should make the budget result understandable.

### Portability

- The application should run with Java 21 and the included Gradle wrapper.
- The frontend should be usable through a modern browser.
- A user should not need a separate production database installation.

### Security and Privacy Limitations

- Demonstration login is not production authentication.
- ownerEmail filtering is not complete server-side authorization.
- No secrets, tokens, real payment data, or real account credentials should be
  committed.
- A production version would require secure identity verification and access
  controls.

## Acceptance Criteria

The acceptance report documents the following successful manual scenarios:

- AT-01: Traveler logs in
- AT-02: Traveler creates and calculates a trip
- AT-03: Traveler saves a trip
- AT-04: Saved trip remains after page refresh
- AT-05: Traveler reopens a saved trip
- AT-06: Traveler deletes a trip
- AT-07: Invalid mileage is rejected
- AT-08: Invalid budget is rejected
- AT-09: Logged-out user cannot save
- AT-10: Trips are filtered by traveler email

All ten documented acceptance scenarios are marked Pass.

## Requirement Interpretation Rule

When requirements and implementation differ:

1. Identify the written requirement.
2. Inspect the current source.
3. Inspect the automated tests.
4. Inspect the acceptance-test evidence.
5. Report the discrepancy.
6. Do not invent functionality.
7. Do not describe simulated functionality as a real external integration.
8. Do not delete functioning code only to match an older report.