package com.mytrip.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class TripTest {

    private Trip standardTrip;
    private Trip overBudgetTrip;
    private Trip exactBudgetTrip;

    @BeforeEach
    void setUp() {
        // Standard Trip Setup (Under Budget)
        standardTrip = new Trip();
        standardTrip.setDistanceMiles(new BigDecimal("300"));
        standardTrip.setVehicleMpg(new BigDecimal("30"));
        standardTrip.setFuelPrice(new BigDecimal("3.00"));
        standardTrip.setLodgingCost(new BigDecimal("150.00"));
        standardTrip.setActivityCost(new BigDecimal("50.00"));
        standardTrip.setBudget(new BigDecimal("300.00"));

        // Over-budget Trip Setup
        overBudgetTrip = new Trip();
        overBudgetTrip.setDistanceMiles(new BigDecimal("300"));
        overBudgetTrip.setVehicleMpg(new BigDecimal("30"));
        overBudgetTrip.setFuelPrice(new BigDecimal("3.00"));
        overBudgetTrip.setLodgingCost(new BigDecimal("250.00"));
        overBudgetTrip.setActivityCost(new BigDecimal("50.00"));
        overBudgetTrip.setBudget(new BigDecimal("300.00"));

        // Exactly-on-budget Trip Setup
        exactBudgetTrip = new Trip();
        exactBudgetTrip.setDistanceMiles(new BigDecimal("300"));
        exactBudgetTrip.setVehicleMpg(new BigDecimal("30"));
        exactBudgetTrip.setFuelPrice(new BigDecimal("3.00"));
        exactBudgetTrip.setLodgingCost(new BigDecimal("220.00"));
        exactBudgetTrip.setActivityCost(new BigDecimal("50.00"));
        exactBudgetTrip.setBudget(new BigDecimal("300.00"));
    }

    @Test
    void calculateFuelCost_ReturnsCorrectResult() {
        assertEquals(new BigDecimal("30.00"), standardTrip.getFuelCost());
    }

    @Test
    void calculateTotalCost_ReturnsCorrectResult() {
        assertEquals(new BigDecimal("230.00"), standardTrip.getTotalCost());
    }

    @Test
    void calculateBudgetDifference_ReturnsCorrectResult() {
        assertEquals(new BigDecimal("70.00"), standardTrip.getBudgetDifference());
    }

    @Test
    void isUnderBudget_ReturnsTrueWhenUnderBudget() {
        assertTrue(standardTrip.isUnderBudget());
    }

    @Test
    void isUnderBudget_ReturnsFalseWhenOverBudget() {
        assertFalse(overBudgetTrip.isUnderBudget());
    }

    @Test
    void isUnderBudget_ReturnsTrueWhenExactlyOnBudget() {
        assertTrue(exactBudgetTrip.isUnderBudget());
        assertEquals(new BigDecimal("0.00"), exactBudgetTrip.getBudgetDifference());
    }

    @Test
    void zeroMpg_ThrowsException() {
        standardTrip.setVehicleMpg(BigDecimal.ZERO);
        assertThrows(IllegalArgumentException.class, () -> standardTrip.getFuelCost());
    }

    @Test
    void negativeMpg_ThrowsException() {
        standardTrip.setVehicleMpg(new BigDecimal("-10"));
        assertThrows(IllegalArgumentException.class, () -> standardTrip.getFuelCost());
    }

    @Test
    void negativeDistance_ThrowsException() {
        standardTrip.setDistanceMiles(new BigDecimal("-50"));
        assertThrows(IllegalArgumentException.class, () -> standardTrip.getFuelCost());
    }

    @Test
    void negativeFuelPrice_ThrowsException() {
        standardTrip.setFuelPrice(new BigDecimal("-3.00"));
        assertThrows(IllegalArgumentException.class, () -> standardTrip.getFuelCost());
    }
    
    @Test
    void nullCalculationData_ThrowsException() {
        Trip nullTrip = new Trip();
        assertThrows(NullPointerException.class, () -> nullTrip.getFuelCost());
    }
}