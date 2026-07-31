MYTRIP – BUDGET ROAD TRIP PLANNER

REQUIREMENTS
- Java 21
- Internet browser
- Windows PowerShell or another supported terminal

RUN THE AUTOMATED TESTS
1. Open PowerShell in this folder.
2. Run:
   .\gradlew.bat clean test

RUN THE APPLICATION
1. Run:
   .\gradlew.bat bootRun

2. Open:
   http://localhost:8080

3. Stop the application with Ctrl+C.

ACTIVE FRONTEND
The active website files are located in:
src/main/resources/static/

DATABASE
MyTrip uses an embedded H2 database that is created automatically when the
application is first started.

MAIN FEATURES
- Plan a road trip
- Estimate fuel, lodging, activity, and total costs
- Compare projected costs against a budget
- Save, open, edit, update, and delete trips
- Display role-specific traveler, vendor, and administrator interface elements
- Persist saved trips through Spring Boot and H2

NOTES
Some third-party booking, payment, vendor, hotel, gas, and activity features
are simulated for demonstration purposes.
