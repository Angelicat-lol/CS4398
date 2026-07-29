package com.mytrip.service;

import com.mytrip.exception.TripNotFoundException;
import com.mytrip.model.Trip;
import com.mytrip.repository.TripRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TripServiceTest {

    @Mock
    private TripRepository repository;

    @InjectMocks
    private TripService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTripCallsRepositorySave() {
        Trip trip = new Trip();
        when(repository.save(any(Trip.class))).thenReturn(trip);
        service.createTrip(trip);
        verify(repository, times(1)).save(trip);
    }

    @Test
    void getAllTripsReturnsRepositoryResults() {
        when(repository.findAll()).thenReturn(Arrays.asList(new Trip(), new Trip()));
        List<Trip> result = service.getAllTrips();
        assertEquals(2, result.size());
    }

    @Test
    void getTripByIdReturnsExistingTrip() {
        Trip trip = new Trip();
        trip.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(trip));
        assertEquals(trip, service.getTripById(1L));
    }

    @Test
    void getTripByIdThrowsWhenMissing() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(TripNotFoundException.class, () -> service.getTripById(99L));
    }

    @Test
    void getTripsForOwnerUsesOwnerEmailQuery() {
        String email = "traveler@example.com";
        when(repository.findByOwnerEmail(email)).thenReturn(Arrays.asList(new Trip()));
        List<Trip> result = service.getTripsForOwner(email);
        assertEquals(1, result.size());
        verify(repository, times(1)).findByOwnerEmail(email);
    }

    @Test
    void updateTripChangesValuesAndCallsSave() {
        Trip existingTrip = new Trip();
        existingTrip.setId(1L);
        Trip updatedTrip = new Trip();
        updatedTrip.setTripName("New Name");

        when(repository.findById(1L)).thenReturn(Optional.of(existingTrip));
        when(repository.save(any(Trip.class))).thenReturn(existingTrip);

        Trip result = service.updateTrip(1L, updatedTrip);
        assertEquals(1L, result.getId());
        verify(repository, times(1)).save(existingTrip);
    }

    @Test
    void deleteTripDeletesExistingTrip() {
        when(repository.existsById(1L)).thenReturn(true);
        service.deleteTrip(1L);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void deleteTripThrowsWhenMissing() {
        when(repository.existsById(99L)).thenReturn(false);
        assertThrows(TripNotFoundException.class, () -> service.deleteTrip(99L));
    }

    @Test
    void nullTripIsRejectedOnCreate() {
        assertThrows(IllegalArgumentException.class, () -> service.createTrip(null));
    }
}