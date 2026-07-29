package com.mytrip.repository;

import com.mytrip.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Provides database operations for Trip objects.
 */
public interface TripRepository extends JpaRepository<Trip, Long> {

    /**
     * Retreives trips belonging to one traveler.
     *
     * @param ownerEmail email of travler
     * @return the traveler's trips, newest first
     */
    List<Trip> findByOwnerEmailIgnoreCaseOrderByIdDesc(
            String ownerEmail
    );
}