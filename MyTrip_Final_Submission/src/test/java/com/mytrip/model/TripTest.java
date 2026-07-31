package com.mytrip.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class TripTest {

    @Test
    void testStandardTripCalculations() {
        Trip trip = new Trip();
        trip.setDistanceMiles(new BigDecimal("300"));
        trip.setVehicleMpg(new BigDecimal("30"));
        trip.setFuelPrice(new BigDecimal("3.00"));
        trip.setLodgingCost(new BigDecimal("150.00"));
        trip.setActivityCost(new BigDecimal("50.00"));
        trip.setBudget(new BigDecimal("300.00"));

        assertEquals(new BigDecimal("30.00"), trip.calculateFuelCost());
        assertEquals(new BigDecimal("230.00"), trip.calculateTotalCost());
        assertEquals(new BigDecimal("70.00"), trip.calculateBudgetDifference());
        assertTrue(trip.isUnderBudget());
    }

    @Test
    void testOverBudgetTrip() {
        Trip trip = new Trip();
        trip.setDistanceMiles(new BigDecimal("300"));
        trip.setVehicleMpg(new BigDecimal("30"));
        trip.setFuelPrice(new BigDecimal("3.00"));
        trip.setLodgingCost(new BigDecimal("250.00"));
        trip.setActivityCost(new BigDecimal("50.00"));
        trip.setBudget(new BigDecimal("300.00"));

        assertEquals(new BigDecimal("330.00"), trip.calculateTotalCost());
        assertEquals(new BigDecimal("-30.00"), trip.calculateBudgetDifference());
        assertFalse(trip.isUnderBudget());
    }

    @Test
    void testExactlyOnBudgetTrip() {
        Trip trip = new Trip();
        trip.setDistanceMiles(new BigDecimal("300"));
        trip.setVehicleMpg(new BigDecimal("30"));
        trip.setFuelPrice(new BigDecimal("3.00"));
        trip.setLodgingCost(new BigDecimal("220.00"));
        trip.setActivityCost(new BigDecimal("50.00"));
        trip.setBudget(new BigDecimal("300.00"));

        assertEquals(new BigDecimal("300.00"), trip.calculateTotalCost());
        assertEquals(new BigDecimal("0.00"), trip.calculateBudgetDifference());
        assertTrue(trip.isUnderBudget());
    }

    @Test
    void testZeroMpgThrowsException() {
        Trip trip = new Trip();

        assertThrows(
                IllegalArgumentException.class,
                () -> trip.setVehicleMpg(BigDecimal.ZERO)
        );
    }

    @Test
    void testZeroDistanceThrowsException() {
        Trip trip = new Trip();

        assertThrows(
                IllegalArgumentException.class,
                () -> trip.setDistanceMiles(BigDecimal.ZERO)
        );
    }

    @Test
    void testNegativeDistanceThrowsException() {
        Trip trip = new Trip();

        assertThrows(
                IllegalArgumentException.class,
                () -> trip.setDistanceMiles(
                        new BigDecimal("-50")
                )
        );
    }

    @Test
    void testZeroBudgetThrowsException() {
        Trip trip = new Trip();

        assertThrows(
                IllegalArgumentException.class,
                () -> trip.setBudget(BigDecimal.ZERO)
        );
    }

    @Test
    void testNegativeFuelPriceThrowsException() {
        Trip trip = new Trip();

        assertThrows(
                IllegalArgumentException.class,
                () -> trip.setFuelPrice(
                        new BigDecimal("-3.00")
                )
        );
    }

    @Test
    void testNegativeBudgetThrowsException() {
        Trip trip = new Trip();

        assertThrows(
                IllegalArgumentException.class,
                () -> trip.setBudget(
                        new BigDecimal("-10.00")
                )
        );
    }

    @Test
    void testNegativeLodgingCostThrowsException() {
        Trip trip = new Trip();

        assertThrows(
                IllegalArgumentException.class,
                () -> trip.setLodgingCost(
                        new BigDecimal("-15.00")
                )
        );
    }

    @Test
    void testNegativeActivityCostThrowsException() {
        Trip trip = new Trip();

        assertThrows(
                IllegalArgumentException.class,
                () -> trip.setActivityCost(
                        new BigDecimal("-20.00")
                )
        );
    }
}