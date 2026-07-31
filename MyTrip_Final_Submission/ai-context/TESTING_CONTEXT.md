# MyTrip Testing Context

## Testing Purpose

The MyTrip test suite verifies the model, service, controller, application
startup, validation rules, CRUD behavior, ownership filtering, and important
error conditions.

Testing includes both:

- Automated tests
- Manual acceptance tests

## Automated Testing Technologies

- JUnit
- Mockito
- MockMvc
- Spring Boot Test
- Gradle test task

## Test Locations

Automated tests are located under:

src/test/java/

Important test areas include:

- Trip model tests
- TripService tests
- TripController tests
- Spring Boot application-context test

## Expected Automated Test Result

The final integrated project previously completed:

- 24 tests
- 24 passed
- 0 failed

A fresh agent must not assume this result without running the test suite.

The required verification command is:

.\gradlew.bat clean test

The agent must report the actual current result produced by the command.

## Expected Gradle Tasks

A real test run should include tasks similar to:

- compileJava
- processResources
- classes
- compileTestJava
- processTestResources
- testClasses
- test

If Gradle reports success without compiling Java or executing the expected test
suite, inspect whether the source folder has been copied incorrectly or nested
under the wrong path.

## Model Validation Expectations

Trip should reject invalid numeric values.

Important validation behavior includes:

- Negative route distance is rejected
- Zero or negative MPG is rejected
- Negative fuel price is rejected
- Invalid budget is rejected where enforced
- Required textual fields are validated where enforced
- Valid values are accepted

Tests that expect an exception must place the setter or method call inside the
assertThrows block.

Correct pattern:

assertThrows(
    IllegalArgumentException.class,
    () -> trip.setDistanceMiles(-1)
);

Incorrect pattern:

trip.setDistanceMiles(-1);
assertThrows(...);

## Service Test Expectations

TripService tests should use complete, valid Trip objects unless the test is
specifically testing invalid input.

Service tests should verify:

- Saving a valid trip
- Retrieving all trips when supported
- Retrieving a trip by ID
- Throwing TripNotFoundException for a missing ID
- Deleting an existing trip
- Rejecting deletion of a missing trip
- Retrieving trips by owner email
- Calling the correct repository operation
- Updating a trip when supported by the current source

The owner-email repository method is expected to be:

findByOwnerEmailIgnoreCaseOrderByIdDesc

Mocks and verify statements should use that exact method unless the current
repository intentionally differs.

## Controller Test Expectations

TripController tests use MockMvc.

Controller tests should verify relevant HTTP operations such as:

- GET trips
- GET trip by ID
- GET trips by ownerEmail
- POST a trip
- PUT a trip when supported
- DELETE a trip
- Missing-trip response
- Invalid-input response

Mocked service methods must return realistic valid Trip objects when the test
expects a successful response.

## Spring Boot Test Configuration

The project uses a current Spring Boot test configuration.

Depending on the installed Spring Boot version, controller tests may use:

- WebMvcTest
- MockitoBean
- MockMvc

Use the imports and annotations already proven to compile in the current
project.

Do not replace working Spring Boot 4 test annotations with older Boot 3
annotations merely because an online example uses them.

## Manual Acceptance Tests

The acceptance report documents ten final manual scenarios.

### AT-01: Traveler Logs In

Expected result:

- Demonstration traveler logs in
- Account information is displayed
- User is no longer identified as Guest

Status in report: Pass

### AT-02: Traveler Creates and Calculates a Trip

Expected result:

- Valid trip data is accepted
- Route is displayed
- Fuel cost is calculated
- Drive time is estimated
- Total cost is calculated
- Budget result is displayed

Status in report: Pass

### AT-03: Traveler Saves a Trip

Expected result:

- Logged-in traveler saves a valid trip
- Trip appears in saved-trip list
- Stored data includes route, budget, and cost information

Status in report: Pass

### AT-04: Saved Trip Remains After Page Refresh

Expected result:

- Saved trip remains visible after browser refresh
- Stored route and cost information remain unchanged

Status in report: Pass

### AT-05: Traveler Reopens a Saved Trip

Expected result:

- Saved trip can be opened
- Stored route and cost information are restored

Status in report: Pass

### AT-06: Traveler Deletes a Trip

Expected result:

- Selected trip is removed
- Deleted trip no longer appears in the account list

Status in report: Pass

### AT-07: Invalid Mileage Is Rejected

Tested invalid value:

- -10

Expected result:

- Calculation does not proceed
- A clear validation message is displayed

Observed message:

“Enter a valid route distance greater than zero.”

Status in report: Pass

### AT-08: Invalid Budget Is Rejected

Tested invalid value:

- -50

Expected result:

- Calculation does not proceed
- A clear validation message is displayed

Observed message:

“Error: Invalid target budget.”

Status in report: Pass

### AT-09: Logged-Out User Cannot Save

Expected result:

- Guest cannot save a trip
- User is instructed to sign in
- No account trip is created

Status in report: Pass

### AT-10: Trips Are Filtered by Traveler Email

Expected result:

- Trips saved under one email are not displayed under a different email
- Account-specific filtering is preserved

Status in report: Pass

## Manual Test Accounts

Demonstration testing may use:

- traveler@example.com
- traveler2@example.com

These are demonstration identities and not production authentication accounts.

## Additional Manual Verification

After automated tests pass, launch the application with:

.\gradlew.bat bootRun

Open:

http://localhost:8080

Verify at minimum:

1. The home page loads.
2. A demonstration traveler can log in.
3. A valid trip can be calculated.
4. The route appears.
5. Fuel cost appears.
6. Total cost appears.
7. Budget status appears.
8. A logged-in traveler can save a trip.
9. The trip remains after refresh.
10. The trip can be opened.
11. The trip can be edited and updated if supported by current source.
12. The trip can be deleted.
13. A guest cannot save.
14. Different email accounts do not share saved-trip lists.
15. Invalid mileage is rejected.
16. Invalid budget is rejected.
17. No unexpected browser-console errors occur during the tested workflow.

Stop the application with Ctrl+C.

## H2 Verification

H2 console URL:

http://localhost:8080/h2-console

Connection values:

- JDBC URL: jdbc:h2:file:./data/mytrip
- User Name: sa
- Password: blank

Useful verification query:

SELECT * FROM TRIPS;

Verify that saved records appear when expected and deleted records are removed.

## Testing Rules for the AI Agent

- Run tests before modifying code.
- Record the initial result.
- Do not delete tests.
- Do not disable tests.
- Do not weaken meaningful assertions.
- Do not bypass validation.
- Do not change expected behavior without repository evidence.
- Do not alter production code merely to satisfy an incorrect test.
- Do not alter a correct test merely to hide a production defect.
- Trace each failure to its cause.
- Make minimal corrections.
- Run the full test suite after corrections.
- Report every changed file.
- Report every command executed.
- Report unresolved limitations honestly.
- Do not claim that tests passed unless the final Gradle command actually
  completed successfully.

## Known Documentation Discrepancy

An older system report states that the Edit Trip workflow was not fully
functional.

The current integrated application may now support edit/update behavior.

Testing must determine the current truth by inspecting:

- main.js
- editingTripId behavior
- POST and PUT requests
- TripController
- TripService
- TripRepository
- Current automated tests
- Manual application behavior

Report the discrepancy instead of automatically treating either document or
memory as authoritative.

## Final Evidence to Preserve

For the IDE-integrated AI workflow, preserve:

- The prompt file used
- The context files attached
- The exported AI chat session
- Screenshots of the IDE agent reading the context
- Screenshots of proposed or applied changes
- Terminal output from the test command
- Final test report
- Git diff showing changes
- Git commit containing the verified work