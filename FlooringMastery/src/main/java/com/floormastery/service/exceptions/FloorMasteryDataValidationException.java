package com.floormastery.service.exceptions;

public class FloorMasteryDataValidationException extends Exception {
    public FloorMasteryDataValidationException(String message) {
        super(message);
    }

    public FloorMasteryDataValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
