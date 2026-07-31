# MyTrip Project Overview

## Project Name

MyTrip – Budget Road Trip Planner

## Project Purpose

MyTrip is a web-based academic road-trip planning application. It helps a
traveler organize a road trip, estimate its cost, compare that cost with a
target budget, and save the completed plan for later review.

The project addresses the problem of travelers needing to use several separate
websites or tools for route information, fuel estimates, hotel options,
activities, gas stations, and budget calculations. MyTrip combines these
planning activities into one prototype application.

## Course Context

- Course: CS 4398 – Software Engineering Project
- Institution: Texas State University
- Term: Summer 2026
- Project type: Academic software-engineering prototype

## Primary Users

### Traveler

The traveler is the main user of the application. A traveler can:

- Use a demonstration account
- Enter a starting location
- Enter an optional waypoint
- Enter a destination
- Enter route mileage
- Enter or select vehicle fuel efficiency
- Enter a fuel price
- Enter a target trip budget
- Display a route
- Estimate fuel cost
- Estimate drive time
- Review lodging, activity, and fuel-station options
- Review a trip summary
- Save a trip
- Open a saved trip
- Edit or update a saved trip when supported by the current code
- Delete a saved trip
- View only trips associated with the traveler’s email

### Administrator

The interface contains demonstration-level administrator functionality.
Administrative user-management behavior is part of the project design, but it
is not a production account-management system.

### Vendor

The interface contains demonstration-level vendor functionality. Vendor-facing
behavior is simulated and is not connected to a real commercial vendor system.

## Main Application Workflow

The primary workflow is:

1. The traveler signs in using a demonstration account.
2. The traveler enters trip information.
3. The system validates the entered values.
4. The system displays the route.
5. The system estimates fuel cost and drive time.
6. The traveler reviews simulated lodging, activity, and gas-station options.
7. The system calculates the total estimated trip cost.
8. The system compares the estimate with the target budget.
9. The traveler reviews the trip summary.
10. The traveler saves the trip.
11. The saved trip is stored in the H2 database.
12. The traveler can later open, edit, update, or delete the saved trip,
    according to the behavior supported by the current source code.

## Core Features

The integrated application is intended to demonstrate:

- Road-trip creation
- Location entry
- Route display
- Manual route-mileage entry
- Fuel-cost estimation
- Drive-time estimation
- Lodging-cost selection
- Activity-cost selection
- Total-cost calculation
- Budget comparison
- Per-person cost splitting
- Demonstration login
- Trip persistence
- Saved-trip ownership by traveler email
- Saved-trip viewing
- Saved-trip deletion
- Saved-trip editing and updating where supported by the current source
- Role-specific traveler, vendor, and administrator interface behavior

## Technology Stack

- Java 21
- Spring Boot
- Gradle
- Spring Data JPA
- H2 embedded file-based database
- Jakarta Validation
- HTML
- CSS
- JavaScript
- JUnit
- Mockito
- MockMvc
- Google Maps embedded route display

## Real Functionality

The following behavior is implemented through the application itself:

- Spring Boot application startup
- REST request handling
- Trip validation
- Trip calculations
- H2 database persistence
- Trip ownership filtering by email
- Trip creation
- Trip retrieval
- Trip updating where supported by the current source
- Trip deletion
- Frontend-to-backend communication
- Fuel-cost calculation
- Total-cost calculation
- Budget comparison
- Automated model, service, controller, and application tests

## Simulated or Demonstration Functionality

The following features are simulated for the academic prototype:

- Hotel listings
- Attraction listings
- Fuel-station listings
- Hotel reservations
- Activity bookings
- Payment processing
- Reservation confirmations
- Vendor transactions
- Administrator account-management actions
- Demonstration login and role selection
- Changing demonstration gas-price values

MyTrip does not make real hotel reservations, real activity bookings, real
payments, or real vendor transactions.

## External Functionality

Google Maps is used to display a route visually.

The application does not automatically retrieve a guaranteed route distance
from Google Maps. The traveler manually enters the route mileage used by the
cost and drive-time calculations.

## Data Ownership

Each saved Trip is associated with an ownerEmail value.

The account page retrieves trips filtered by that email. This prevents one
demonstration traveler from seeing trips saved under another demonstration
traveler’s email.

This is demonstration-level separation and is not equivalent to production
authentication or authorization.

## Important Source-of-Truth Rule

The source code in the current workspace is the final authority for current
application behavior.

Some written project documents were prepared before the last integration and
may contain outdated statements. In particular, an older system report states
that the Edit Trip workflow was incomplete. The current source, automated
tests, and manual verification must be inspected before deciding whether
editing and updating are currently functional.

Do not remove a working feature merely because an older document does not
describe it accurately.

## Project Constraints

- Preserve the existing Spring Boot architecture.
- Do not introduce paid services.
- Do not introduce real booking or payment claims.
- Do not add secrets or API credentials.
- Do not replace the H2 database without explicit instruction.
- Do not remove working tests to obtain a successful build.
- Keep corrections minimal and focused.
- Clearly distinguish real functionality from simulated functionality.