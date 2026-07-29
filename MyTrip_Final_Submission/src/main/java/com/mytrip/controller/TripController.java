package com.mytrip.controller;

import com.mytrip.exception.TripNotFoundException;
import com.mytrip.model.Trip;
import com.mytrip.service.TripService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trips")
public class TripController {

    private final TripService tripService;

    @Autowired
    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @PostMapping
    public ResponseEntity<Trip> createTrip(@Valid @RequestBody Trip trip) {
        Trip saved = tripService.createTrip(trip);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<Trip>> getTrips(
            @RequestParam(required = false) String ownerEmail) {
        List<Trip> trips = (ownerEmail != null && !ownerEmail.isBlank())
                ? tripService.getTripsForOwner(ownerEmail)
                : tripService.getAllTrips();
        return ResponseEntity.ok(trips);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Trip> getTripById(@PathVariable Long id) {
        return ResponseEntity.ok(tripService.getTripById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Trip> updateTrip(@PathVariable Long id, @Valid @RequestBody Trip trip) {
        return ResponseEntity.ok(tripService.updateTrip(id, trip));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrip(@PathVariable Long id) {
        tripService.deleteTrip(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Converts a missing trip into a 404 instead of a 500. This is what makes
     * getMissingIdReturns404 in TripControllerTest pass - without it, an
     * uncaught TripNotFoundException would bubble up as a 500 Internal Server Error.
     */
    @ExceptionHandler(TripNotFoundException.class)
    public ResponseEntity<String> handleNotFound(TripNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
