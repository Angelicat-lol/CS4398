---
description: Audit the complete MyTrip project against its source, tests, requirements, and submission materials.
---

# MyTrip Project Audit

Read `.github/copilot-instructions.md` and all files under `ai-context/`.

Inspect the current source code, automated tests, build files, frontend,
documentation, Javadocs, and UML materials. Treat the current source code and
fresh verification results as the source of truth when older documents
disagree.

Run:

.\gradlew.bat clean test

Then:

1. Summarize the implemented architecture.
2. Identify which required features are currently implemented.
3. Identify missing or inconsistent submission materials.
4. Identify defects that can be verified directly from the repository.
5. Apply only minimal corrections for verified defects.
6. Add or update regression tests when a production defect is corrected.
7. Do not weaken, delete, bypass, or disable meaningful tests.
8. Do not invent external integrations, authentication, or production security.
9. Clearly distinguish implemented features from simulated behavior.
10. Report the exact automated test totals from the generated test reports.

Report:

- Files inspected
- Files changed
- Commands executed
- Architecture summary
- Implemented features
- Verified defects and corrections
- Exact test totals
- Remaining limitations
