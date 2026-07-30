package com.mytrip.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when a requested trip cannot be found.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class TripNotFoundException extends RuntimeException {

    /**
     * Constructs a TripNotFoundException with a descriptive message.
     *
     * @param message explanation of why the trip could not be found
     */
    public TripNotFoundException(String message) {
        super(message);
    }
}