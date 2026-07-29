package com.mytrip.exception;

/**
 * Thrown when a Trip cannot be found by the given ID.
 */
public class TripNotFoundException extends RuntimeException {

    public TripNotFoundException(String message) {
        super(message);
    }
}
