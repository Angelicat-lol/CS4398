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
import java.math.BigDecimal;
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

    private Trip createValidTrip() {
        Trip trip = new Trip();

        trip.setTripName("Dallas Weekend");
        trip.setOwnerEmail("traveler@mytrip.com");
        trip.setStartLocation("San Marcos, TX");
        trip.setDestination("Dallas, TX");
        trip.setDistanceMiles(new BigDecimal("225"));
        trip.setVehicleMpg(new BigDecimal("30"));
        trip.setFuelPrice(new BigDecimal("3.20"));
        trip.setBudget(new BigDecimal("500"));
        trip.setLodgingCost(new BigDecimal("150"));
        trip.setActivityCost(new BigDecimal("50"));

        return trip;
    }

    @Test
    void createTripCallsRepositorySave() {
        Trip trip = createValidTrip();

        when(repository.save(any(Trip.class)))
                .thenReturn(trip);

        Trip result = service.createTrip(trip);

        assertSame(trip, result);
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

        when(
                repository.findByOwnerEmailIgnoreCaseOrderByIdDesc(email)
        ).thenReturn(Arrays.asList(new Trip()));

        List<Trip> result =
                service.getTripsForOwner(email);

        assertEquals(1, result.size());

        verify(repository, times(1))
                .findByOwnerEmailIgnoreCaseOrderByIdDesc(email);
    }

    @Test
    void updateTripChangesValuesAndCallsSave() {
        Trip existingTrip = createValidTrip();
        existingTrip.setId(1L);

        Trip updatedTrip = createValidTrip();
        updatedTrip.setTripName("New Name");

        when(repository.findById(1L))
                .thenReturn(Optional.of(existingTrip));

        when(repository.save(any(Trip.class)))
                .thenReturn(existingTrip);

        Trip result = service.updateTrip(1L, updatedTrip);

        assertEquals(1L, result.getId());
        assertEquals("New Name", result.getTripName());

        verify(repository, times(1))
                .save(existingTrip);
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