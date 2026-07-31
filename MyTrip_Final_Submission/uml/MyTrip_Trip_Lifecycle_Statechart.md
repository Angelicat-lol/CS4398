# MyTrip Trip Lifecycle Statechart

```mermaid
stateDiagram-v2
    direction TB

    state "Enter Trip Details" as EnterDetails
    state "Validate Input" as ValidateInput
    state "Correct Input" as CorrectInput
    state "Review Route and Costs" as ReviewTrip
    state "Saving Trip" as SavingTrip
    state "Save Error" as SaveError
    state "Saved Trips List" as SavedTripsList
    state "Viewing Saved Trip" as ViewingTrip
    state "Editing Saved Trip" as EditingTrip
    state "Updating Saved Trip" as UpdatingTrip
    state "Update Error" as UpdateError
    state "Deleting Saved Trip" as DeletingTrip
    state "Delete Error" as DeleteError

    [*] --> EnterDetails

    EnterDetails --> ValidateInput: Submit trip information
    ValidateInput --> CorrectInput: Input invalid
    CorrectInput --> EnterDetails: Correct values
    ValidateInput --> ReviewTrip: Input valid

    ReviewTrip --> EnterDetails: Edit details
    ReviewTrip --> SavingTrip: Save trip

    SavingTrip --> SavedTripsList: Save succeeds
    SavingTrip --> SaveError: Save fails
    SaveError --> ReviewTrip: Correct and retry

    SavedTripsList --> ViewingTrip: Open
    ViewingTrip --> SavedTripsList: Return
    ViewingTrip --> EditingTrip: Edit

    SavedTripsList --> EditingTrip: Edit
    EditingTrip --> UpdatingTrip: Submit update
    EditingTrip --> SavedTripsList: Cancel
    UpdatingTrip --> SavedTripsList: Update succeeds
    UpdatingTrip --> UpdateError: Update fails
    UpdateError --> EditingTrip: Correct and retry

    SavedTripsList --> DeletingTrip: Delete
    DeletingTrip --> SavedTripsList: Delete succeeds
    DeletingTrip --> DeleteError: Delete fails
    DeleteError --> SavedTripsList: Return

    SavedTripsList --> [*]: Exit
```

This revision keeps the same major behavior while reducing clutter by using a single top-to-bottom workflow, readable quoted state labels, and localized save, update, and delete error states.

Source files reviewed:
- [src/main/java/com/mytrip/controller/TripController.java](src/main/java/com/mytrip/controller/TripController.java)
- [src/main/java/com/mytrip/service/TripService.java](src/main/java/com/mytrip/service/TripService.java)
- [src/main/java/com/mytrip/model/Trip.java](src/main/java/com/mytrip/model/Trip.java)
- [src/main/resources/static/main.js](src/main/resources/static/main.js)