package com.floormastery.service.exceptions;

public class FloorMasteryDuplicateOrderNumberException extends RuntimeException {
    public FloorMasteryDuplicateOrderNumberException(String message) {
        super(message);
    }
}
