package com.mytrip.service;

import com.mytrip.exception.TripNotFoundException;
import com.mytrip.model.Trip;
import com.mytrip.repository.TripRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Provides application operations for creating, retrieving,
 * and deleting trips.
 *
 * This class acts as an intermediary between the controller
 * layer and the TripRepository.
 */
@Service
public class TripService {

    private final TripRepository tripRepository;

    /**
     * Constructs a TripService using the supplied repository.
     *
     * @param tripRepository repository used to store and retrieve trips
     */
    public TripService(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    /**
     * Validates, calculates, and saves a new trip.
     *
     * @param trip trip information submitted by the traveler
     * @return the saved trip, including its generated database ID
     * @throws IllegalArgumentException if the trip object is null
     * @throws IllegalStateException if required calculation data is invalid
     */
    public Trip createTrip(Trip trip) {
        if (trip == null) {
            throw new IllegalArgumentException("Trip cannot be null.");
        }

        /*
         * Calling this method before saving confirms that the
         * distance, MPG, and fuel-price values are usable.
         */
        trip.calculateTotalCost();

        return tripRepository.save(trip);
    }

    /**
     * Retrieves trips belonging to one traveler.
     *
     * @param ownerEmail email address of the traveler
     * @return trips saved by that traveler
     */
    public List<Trip> getTripsForOwner(String ownerEmail) {
        if (ownerEmail == null || ownerEmail.isBlank()) {
            throw new IllegalArgumentException(
                    "Owner email is required."
            );
        }

        return tripRepository
                .findByOwnerEmailIgnoreCaseOrderByIdDesc(
                        ownerEmail
                );
    }

    /**
     * Retrieves every saved trip.
     *
     * @return list of all saved trips
     */
    public List<Trip> getAllTrips() {
        return tripRepository.findAll();
    }

    /**
     * Retrieves one trip by its database ID.
     *
     * @param id unique identifier of the requested trip
     * @return the matching trip
     * @throws IllegalArgumentException if the ID is null
     * @throws TripNotFoundException if no trip has the supplied ID
     */
    public Trip getTripById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException(
                    "Trip ID cannot be null."
            );
        }

        return tripRepository.findById(id)
                .orElseThrow(() -> new TripNotFoundException(
                        "Trip with ID " + id + " was not found."
                ));
    }

    /**
     * Updates an existing saved trip.
     *
     * @param id database ID of the trip being updated
     * @param updatedTrip new trip information
     * @return the updated and saved trip
     * @throws IllegalArgumentException if the updated trip is null
     * @throws TripNotFoundException if the trip does not exist
     * @throws IllegalStateException if calculation values are invalid
     */
    public Trip updateTrip(Long id, Trip updatedTrip) {
        if (updatedTrip == null) {
            throw new IllegalArgumentException(
                    "Updated trip cannot be null."
            );
        }

        Trip existingTrip = getTripById(id);

        existingTrip.setTripName(
                updatedTrip.getTripName()
        );

        existingTrip.setOwnerEmail(
                updatedTrip.getOwnerEmail()
        );

        existingTrip.setStartLocation(
                updatedTrip.getStartLocation()
        );

        existingTrip.setWaypoint(
                updatedTrip.getWaypoint()
        );

        existingTrip.setDestination(
                updatedTrip.getDestination()
        );

        existingTrip.setDistanceMiles(
                updatedTrip.getDistanceMiles()
        );

        existingTrip.setVehicleMpg(
                updatedTrip.getVehicleMpg()
        );

        existingTrip.setFuelPrice(
                updatedTrip.getFuelPrice()
        );

        existingTrip.setBudget(
                updatedTrip.getBudget()
        );

        existingTrip.setLodgingCost(
                updatedTrip.getLodgingCost()
        );

        existingTrip.setActivityCost(
                updatedTrip.getActivityCost()
        );

        /*
         * Run the calculations before saving so invalid values,
         * such as zero MPG, are rejected.
         */
        existingTrip.calculateTotalCost();

        return tripRepository.save(existingTrip);
    }

    /**
     * Deletes a saved trip.
     *
     * @param id unique identifier of the trip being deleted
     * @throws IllegalArgumentException if the ID is null
     * @throws TripNotFoundException if no trip has the supplied ID
     */
    public void deleteTrip(Long id) {
        if (id == null) {
            throw new IllegalArgumentException(
                    "Trip ID cannot be null."
            );
        }

        if (!tripRepository.existsById(id)) {
            throw new TripNotFoundException(
                    "Trip with ID " + id + " was not found."
            );
        }

        tripRepository.deleteById(id);
    }
}