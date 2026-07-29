package com.mytrip.repository;

import com.mytrip.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Long> {

    /**
     * Demonstration-level filtering by owner email (see instructions: no real
     * authentication/authorization backs this - the owner email is simply
     * whatever value the client sent in the trip payload).
     */
    List<Trip> findByOwnerEmail(String ownerEmail);
}
