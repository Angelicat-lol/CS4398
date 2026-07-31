# MyTrip Architecture Context

## Architecture Overview

MyTrip uses a layered Spring Boot architecture.

The normal backend request flow is:

Web Browser
→ TripController
→ TripService
→ TripRepository
→ H2 Database

Responses return through the same layers to the browser.

The controller must not communicate directly with the database. Repository
access is coordinated through TripService.

## Technology Stack

- Java 21
- Spring Boot
- Gradle
- Spring Data JPA
- H2 file-based database
- Jakarta Validation
- HTML
- CSS
- JavaScript
- JUnit
- Mockito
- MockMvc

## Base Package

The backend is organized under:

com.mytrip

## Important Source Locations

### Application entry point

src/main/java/com/mytrip/MytripApplication.java

This class starts the Spring Boot application.

### Model layer

src/main/java/com/mytrip/model/Trip.java

### Controller layer

src/main/java/com/mytrip/controller/TripController.java

### Service layer

src/main/java/com/mytrip/service/TripService.java

### Repository layer

src/main/java/com/mytrip/repository/TripRepository.java

### Exception handling

src/main/java/com/mytrip/exception/TripNotFoundException.java

### Active frontend

src/main/resources/static/

Important frontend files include:

- index.html
- main.js
- style.css

Do not treat root-level legacy HTML, CSS, or JavaScript files as the active
Spring Boot frontend unless current source inspection proves otherwise.

### Application configuration

src/main/resources/application.properties

### Automated tests

src/test/java/

## Model Layer

The Trip entity represents one saved road-trip plan.

Representative fields include:

- id
- ownerEmail
- trip name
- starting location
- optional waypoint
- destination
- route distance
- vehicle MPG
- fuel price per gallon
- estimated drive time
- target budget
- lodging cost
- activity cost
- estimated fuel cost
- total estimated cost
- budget difference
- under-budget status
- created timestamp
- updated timestamp

The exact field names and persistence annotations must be verified from
Trip.java.

Some values may be stored directly, while others may be exposed as calculated
or transient JSON properties.

## Controller Layer

TripController is the HTTP boundary between the frontend and backend.

Controller responsibilities include:

- Receiving REST requests
- Accepting Trip request data
- Returning Trip response data
- Retrieving trips
- Retrieving a trip by ID
- Retrieving trips filtered by owner email
- Creating a trip
- Updating a trip when supported
- Deleting a trip
- Returning validation or missing-record responses

The controller should delegate calculations, ownership handling, validation,
and persistence decisions to TripService.

## Service Layer

TripService contains the central business logic.

Service responsibilities include:

- Validating important trip values
- Calculating gallons required
- Calculating estimated fuel cost
- Calculating total estimated cost
- Calculating budget difference
- Determining whether the trip is within budget
- Estimating drive time
- Associating a trip with ownerEmail
- Finding trips by ID
- Finding trips by traveler email
- Creating or saving trips
- Updating trips when supported
- Deleting trips
- Throwing TripNotFoundException for missing records

Do not move business logic into TripRepository.

## Repository Layer

TripRepository is a Spring Data JPA repository for Trip entities.

It extends JpaRepository and provides standard persistence operations.

The owner-specific query is:

findByOwnerEmailIgnoreCaseOrderByIdDesc

This query returns trips associated with one email address, ignoring email
case and ordering newer IDs first.

TripService should call this exact repository operation unless the current
source has intentionally changed it.

## Database Layer

MyTrip uses a file-based H2 database.

Expected configuration:

spring.datasource.url=jdbc:h2:file:./data/mytrip
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

Application URL:

http://localhost:8080

H2 console URL:

http://localhost:8080/h2-console

H2 console connection values:

- JDBC URL: jdbc:h2:file:./data/mytrip
- User Name: sa
- Password: blank

Useful SQL:

SELECT * FROM TRIPS;

The generated data directory is local and should normally be excluded from Git
and the final clean source package.

Each installation uses its own H2 file. Saved records are not automatically
shared across computers.

## Frontend Architecture

The presentation layer uses:

- HTML for page structure
- CSS for layout and visual appearance
- JavaScript for interaction, validation, calculations, local session state,
  route display, and backend requests

The interface contains sections or views for:

- Home
- Plan Route
- Route Options
- Summary
- Account
- Vendor Portal
- Admin Portal

The application may implement these as sections within one HTML application
rather than separate physical pages.

## Frontend State

The demonstration user and role may be stored in localStorage.

The current editing operation uses an editing-trip identifier such as:

editingTripId

Expected behavior:

- editingTripId is null when creating a new trip
- A POST request is used for a new trip
- editingTripId is set when editing a saved trip
- A PUT request is used when updating an existing trip
- The value is cleared after completing or cancelling the edit

Inspect main.js before changing this behavior.

## Route Display

Google Maps is used for visual route display.

The application does not rely on a real commercial routing backend to populate
all calculated fields. The traveler manually enters route mileage.

The manually entered mileage is used for fuel and drive-time calculations.

## Fuel-Cost Algorithm

Inputs:

- Route distance
- Vehicle MPG
- Fuel price per gallon

Calculations:

Gallons Required =
Route Distance / Vehicle MPG

Estimated Fuel Cost =
Gallons Required × Fuel Price Per Gallon

Distance and MPG must be greater than zero.

## Total-Cost Algorithm

Activity Cost =
Sum of selected activity prices

Total Estimated Cost =
Fuel Cost + Lodging Cost + Activity Cost

## Budget Algorithm

Budget Difference =
Target Budget − Total Estimated Cost

If Budget Difference is greater than or equal to zero:

- The trip is within budget.

If Budget Difference is negative:

- The trip is over budget.

## Drive-Time Algorithm

Estimated Drive Time =
Route Distance / Average Speed

The current average-speed assumption is approximately 55 MPH.

The frontend may convert this value into hours and minutes for display.

This is not a live traffic estimate.

## Per-Person Cost

Per-Person Cost =
Total Estimated Cost / Traveler Count

Traveler Count must be a valid value greater than zero.

## CRUD Behavior

Expected backend operations include:

- Create a Trip
- Read all Trips when appropriate
- Read one Trip by ID
- Read Trips by ownerEmail
- Update a Trip when supported by current source
- Delete a Trip

Opening a saved trip restores stored information to the frontend.

## Error Handling

Important invalid conditions include:

- Invalid distance
- Invalid MPG
- Invalid fuel price
- Invalid budget
- Missing required locations
- Missing owner email when saving
- Missing trip ID
- Requested Trip does not exist

TripNotFoundException is used for a missing Trip.

The frontend should display understandable validation or error messages rather
than silently failing.

## External and Simulated Data

### External

- Google Maps visual route display

### Simulated

- Lodging options
- Attraction options
- Fuel-station options
- Booking behavior
- Reservation confirmation
- Payment behavior
- Vendor transactions
- Administrative actions
- Demonstration login

Do not add claims that these simulated features are connected to production
services.

## Architectural Constraints

- Preserve Controller → Service → Repository separation.
- Do not access H2 directly from TripController.
- Do not place business rules inside TripRepository.
- Do not duplicate the active frontend outside the static resources folder.
- Preserve ownerEmail filtering.
- Preserve validation for invalid numeric values.
- Make minimal changes.
- Do not add API keys or secrets.
- Do not replace working code merely to match an outdated document.
- Explain every changed file.