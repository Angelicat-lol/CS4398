---
description: Generate an as-built Mermaid statechart for the implemented MyTrip Trip lifecycle.
---

# Create MyTrip Trip Lifecycle Statechart

Read `.github/copilot-instructions.md`, all files under `ai-context/`, and the
current application source.

Inspect at minimum:

- `src/main/java/com/mytrip/controller/TripController.java`
- `src/main/java/com/mytrip/service/TripService.java`
- `src/main/java/com/mytrip/model/Trip.java`
- `src/main/resources/static/main.js`

Create or update only:

`uml/MyTrip_Trip_Lifecycle_Statechart.md`

Use Mermaid `stateDiagram-v2`.

Represent behavior verified from the current source, including:

- Entering trip information
- Validating input
- Correcting invalid input
- Reviewing the route and calculated costs
- Saving a new trip
- Viewing the saved-trip list
- Opening a saved trip
- Editing and updating a saved trip
- Deleting a saved trip
- Localized save, update, and delete failures
- Exiting the workflow

Requirements:

1. Use readable multi-word state labels with aliases.
2. Use only one initial state and one final state.
3. Keep failure states near their related operations.
4. Avoid unnecessary crossing arrows.
5. Do not represent simulated booking, payment, vendor, or administrative
   behavior as real external integrations.
6. Include a short explanation beneath the diagram.
7. Include a list of the source files reviewed.
8. Do not modify production or test source files.
9. Verify the rendered Markdown preview before reporting completion.
