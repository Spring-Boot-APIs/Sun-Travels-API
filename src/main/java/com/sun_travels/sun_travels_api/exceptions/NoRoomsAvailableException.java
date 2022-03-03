package com.sun_travels.sun_travels_api.exceptions;

import com.sun_travels.sun_travels_api.Helper;
import com.sun_travels.sun_travels_api.dtos.SearchAvailabilityRequest;

import java.time.LocalDate;

public class NoRoomsAvailableException extends RuntimeException {
    public NoRoomsAvailableException( SearchAvailabilityRequest request, LocalDate checkOutDate ) {
        super("No rooms available in any hotel in " +
            Helper.capitalizeFirstLetterOfEveryWord(request.getCity()) +
            " from " + request.getCheckInDate() + " to " + checkOutDate);
    }
}
