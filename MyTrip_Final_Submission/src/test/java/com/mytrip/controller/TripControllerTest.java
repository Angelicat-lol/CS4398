package com.mytrip.controller;

import com.mytrip.exception.TripNotFoundException;
import com.mytrip.model.Trip;
import com.mytrip.service.TripService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Collections;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WebMvcTest(TripController.class)
class TripControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TripService tripService;

    private final String validJson = "{\"tripName\":\"Dallas Weekend\",\"ownerEmail\":\"traveler@mytrip.com\",\"startLocation\":\"San Marcos, TX\",\"destination\":\"Dallas, TX\",\"distanceMiles\":225,\"vehicleMpg\":30,\"fuelPrice\":3.20,\"budget\":500,\"lodgingCost\":150,\"activityCost\":50}";

    @Test
    void postValidTripReturns201() throws Exception {
        when(tripService.createTrip(any(Trip.class))).thenReturn(new Trip());
        mockMvc.perform(post("/api/trips")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validJson))
                .andExpect(status().isCreated());
    }

    @Test
    void postInvalidTripReturns400() throws Exception {
        mockMvc.perform(post("/api/trips")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllTripsReturns200() throws Exception {
        when(tripService.getAllTrips()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/trips"))
                .andExpect(status().isOk());
    }

    @Test
    void getOwnerSpecificTripsReturns200() throws Exception {
        when(tripService.getTripsForOwner("traveler@mytrip.com")).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/trips?ownerEmail=traveler@mytrip.com"))
                .andExpect(status().isOk());
    }

    @Test
    void getExistingIdReturns200() throws Exception {
        when(tripService.getTripById(1L)).thenReturn(new Trip());
        mockMvc.perform(get("/api/trips/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getMissingIdReturns404() throws Exception {
        when(tripService.getTripById(99L)).thenThrow(new TripNotFoundException("Not found"));
        mockMvc.perform(get("/api/trips/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void putValidTripReturns200() throws Exception {
        when(tripService.updateTrip(eq(1L), any(Trip.class))).thenReturn(new Trip());
        mockMvc.perform(put("/api/trips/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validJson))
                .andExpect(status().isOk());
    }

    @Test
    void deleteExistingTripReturns204() throws Exception {
        doNothing().when(tripService).deleteTrip(1L);
        mockMvc.perform(delete("/api/trips/1"))
                .andExpect(status().isNoContent());
    }
}