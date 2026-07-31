---
description: Perform a final read-only audit of the exact MyTrip submission workspace.
---

# Final MyTrip Submission Audit

Audit the exact `MyTrip_Final_Submission` workspace.

Do not modify production or test source code during this audit.

Verify:

1. Java and frontend source files are present.
2. Automated tests and Gradle configuration are present.
3. All four `.github/prompts/*.prompt.md` files exist and are not empty.
4. `.github/copilot-instructions.md` exists and is not empty.
5. All four `ai-context` files exist and are not empty.
6. The official Copilot chat exports exist.
7. The evidence screenshots exist.
8. `AI_USAGE_LOG.md` accurately matches the exported sessions.
9. Documentation, UML diagrams, Javadocs, and README are present.
10. Documentation claims agree with the current implementation.
11. Simulated features are clearly identified as simulated.
12. The class diagram represents only implemented classes, fields, methods,
    and relationships.
13. Generated build, Gradle-cache, runtime database, lock, and temporary files
    are excluded from the final package.
14. No API keys, credentials, or private secrets are present.

Run:

.\gradlew.bat clean test

Confirm the exact test totals from the generated JUnit XML reports.

Then run:

.\gradlew.bat bootRun

Confirm:

- Successful Spring Boot startup
- Tomcat starts on port 8080
- `MytripApplication` reports successful startup
- The process is terminated cleanly after verification

Provide PASS, FAIL, or NEEDS REVIEW for:

- Source code
- Build configuration
- Automated tests
- Runtime startup
- Documentation
- UML materials
- Javadocs
- IDE-integrated AI evidence
- Repository cleanliness
- Packaging readiness

Conclude by stating whether the workspace is ready to package.
