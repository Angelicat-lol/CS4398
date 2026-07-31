---
description: Run the complete MyTrip automated test suite and correct only verified defects.
---

# Test and Fix MyTrip

Read `.github/copilot-instructions.md` and all files under `ai-context/`.

Run the complete automated suite:

.\gradlew.bat clean test

Inspect the generated JUnit XML reports and determine the exact number of:

- Tests
- Failures
- Errors
- Skipped tests

When a verified defect exists:

1. Trace the failure to its actual cause.
2. Make the smallest appropriate correction.
3. Preserve the Controller -> Service -> Repository architecture.
4. Do not delete, disable, bypass, or weaken tests.
5. Do not modify correct expected behavior merely to obtain a passing build.
6. Add regression coverage when correcting a production defect.
7. Re-run the complete suite after every correction.
8. Do not make unrelated formatting or refactoring changes.

Report:

- Initial test result
- Root cause of every verified failure
- Files changed
- Explanation of each change
- Final test totals by test class
- Final Gradle result
- Remaining limitations

If every test passes and no defect can be verified, make no source changes and
state that no changes were required.
