package com.mytrip.service;

import com.mytrip.exception.TripNotFoundException;
import com.mytrip.model.Trip;
import com.mytrip.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TripService {

    private final TripRepository repository;

    @Autowired
    public TripService(TripRepository repository) {
        this.repository = repository;
    }

    /**
     * Saves a new trip. Only checks for a null trip here - deeper field
     * validation (blank names, missing numeric fields, etc.) is handled by
     * Jakarta Bean Validation at the controller layer via @Valid, not here.
     * This keeps createTrip usable directly against bare/partial Trip objects
     * in unit tests without forcing every calculation field to be populated.
     */
    public Trip createTrip(Trip trip) {
        if (trip == null) {
            throw new IllegalArgumentException("Trip cannot be null");
        }
        return repository.save(trip);
    }

    public List<Trip> getAllTrips() {
        return repository.findAll();
    }

    public Trip getTripById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new TripNotFoundException("Trip not found with id: " + id));
    }

    public List<Trip> getTripsForOwner(String ownerEmail) {
        return repository.findByOwnerEmail(ownerEmail);
    }

    /**
     * Updates an existing trip's fields, keeping its original database ID.
     */
    public Trip updateTrip(Long id, Trip updatedTrip) {
        Trip existingTrip = getTripById(id);

        existingTrip.setTripName(updatedTrip.getTripName());
        existingTrip.setOwnerEmail(updatedTrip.getOwnerEmail());
        existingTrip.setStartLocation(updatedTrip.getStartLocation());
        existingTrip.setWaypoint(updatedTrip.getWaypoint());
        existingTrip.setDestination(updatedTrip.getDestination());
        existingTrip.setDistanceMiles(updatedTrip.getDistanceMiles());
        existingTrip.setVehicleMpg(updatedTrip.getVehicleMpg());
        existingTrip.setFuelPrice(updatedTrip.getFuelPrice());
        existingTrip.setBudget(updatedTrip.getBudget());
        existingTrip.setLodgingCost(updatedTrip.getLodgingCost());
        existingTrip.setActivityCost(updatedTrip.getActivityCost());

        return repository.save(existingTrip);
    }

    public void deleteTrip(Long id) {
        if (!repository.existsById(id)) {
            throw new TripNotFoundException("Trip not found with id: " + id);
        }
        repository.deleteById(id);
    }
}
