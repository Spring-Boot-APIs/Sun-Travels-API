package com.sun_travels.sun_travels_api.exceptions;

public class InvalidHotelIdException extends RuntimeException {
    public InvalidHotelIdException() {
        super("Invalid hotel ID");
    }
}
