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

IDE-INTEGRATED AI DEVELOPMENT EVIDENCE

The final development, correction, and verification phase used GitHub Copilot
Agent directly inside Visual Studio Code.

Repository instructions:
.github/copilot-instructions.md

Reusable prompt files:
.github/prompts/

Project context files:
ai-context/

Exported Copilot sessions and screenshots:
ai-evidence/

Verified Copilot-assisted work included:
- Identifying and correcting zero-distance and zero-budget validation
- Adding regression tests for verified defects
- Correcting Spring request-parameter binding using the Java -parameters option
- Generating and revising the Trip lifecycle statechart
- Correcting the as-built UML class diagram
- Correcting the system-structure diagram
- Running and analyzing all 29 automated tests
- Verifying successful Spring Boot startup on port 8080

The AI usage log distinguishes IDE-integrated Copilot work from earlier work
completed using external conversational AI:
ai-evidence/AI_USAGE_LOG.md