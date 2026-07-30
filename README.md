# MyTrip – Budget Road Trip Planner

MyTrip is a web-based road trip planning project created for CS 4398 at Texas State University.

The application helps travelers plan a route, estimate travel costs, compare the total cost with a budget, and save trip plans.

## Team Members

- Mustafa Al Azzawi
- Angelica Alvarado
- William Parker

## Technologies Used

- Java 21
- Spring Boot
- Gradle
- H2 Database
- HTML
- CSS
- JavaScript
- Google Maps

## Main Features

- Traveler demonstration login
- Route display using Google Maps
- Optional waypoint
- Manual route mileage entry
- Fuel cost calculation
- Estimated drive time
- Budget comparison
- Group cost splitting
- Save trip
- Open saved trip
- Delete saved trip
- Trip filtering by traveler email
- H2 database persistence
- Validation for invalid mileage and budget

## Simulated Features

The following features use demonstration or mock data:

- Lodging options
- Gas station information
- Attraction options
- Booking actions
- Reservation confirmations
- Payment behavior

## Known Limitations

- Route mileage is entered manually.
- Drive time is estimated using an average speed.
- Hotel, attraction, and gas station information is simulated.
- Authentication is demonstration-level.
- Reservations and payments are simulated.
- The Edit Trip feature is not fully functional.
- Each computer uses its own local H2 database.

## Requirements

Before running the project, install:

- Git
- Java 21 JDK

Check the installed versions:

```bash
git --version
java --version