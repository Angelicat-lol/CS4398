package com.mytrip.controller;

import com.mytrip.model.Trip;
import com.mytrip.service.TripService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;

import java.net.URI;
import java.util.List;

/**
 * Provides HTTP endpoints for creating, retrieving,
 * and deleting trips.
 */
@RestController
@RequestMapping("/api/trips")
public class TripController {

    private final TripService tripService;

    /**
     * Constructs a TripController using the supplied service.
     *
     * @param tripService service responsible for trip operations
     */
    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    /**
     * Creates and saves a trip.
     *
     * @param trip trip information received from the website
     * @return the saved trip and its generated database ID
     */
    @PostMapping
    public ResponseEntity<Trip> createTrip(
            @Valid @RequestBody Trip trip) {

        Trip savedTrip = tripService.createTrip(trip);

        URI location = URI.create(
                "/api/trips/" + savedTrip.getId()
        );

        return ResponseEntity
                .created(location)
                .body(savedTrip);
    }

    /**
     * Retrieves all saved trips.
     *
     * @return all trips currently stored in the database
     */
    @GetMapping
    public List<Trip> getTrips(
            @RequestParam(required = false)
            String ownerEmail) {

        if(ownerEmail == null || ownerEmail.isBlank()) {
            return tripService.getAllTrips();
        }

        return tripService.getTripsForOwner(ownerEmail);
    }

    /**
     * Retrieves one trip by ID.
     *
     * @param id database ID of the requested trip
     * @return matching trip
     */
    @GetMapping("/{id}")
    public Trip getTripById(@PathVariable Long id) {
        return tripService.getTripById(id);
    }

    /**
     * Updates an existing saved trip.
     *
     * @param id database ID of the trip being updated
     * @param updatedTrip replacement trip information
     * @return the updated trip
     */
    @PutMapping("/{id}")
    public Trip updateTrip(
            @PathVariable Long id,
            @Valid @RequestBody Trip updatedTrip) {

        return tripService.updateTrip(id, updatedTrip);
    }

    /**
     * Deletes one trip by ID.
     *
     * @param id database ID of the trip to delete
     * @return an empty 204 No Content response
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrip(
            @PathVariable Long id) {

        tripService.deleteTrip(id);

        return ResponseEntity.noContent().build();
    }
}