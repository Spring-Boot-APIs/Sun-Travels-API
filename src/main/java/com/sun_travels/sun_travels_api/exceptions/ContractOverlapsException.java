package com.sun_travels.sun_travels_api.exceptions;

import com.sun_travels.sun_travels_api.Helper;
import com.sun_travels.sun_travels_api.enums.ContractType;
import com.sun_travels.sun_travels_api.models.Hotel;

public class ContractOverlapsException extends RuntimeException {
    public ContractOverlapsException( Hotel hotel, Long contractId, String roomType, ContractType type ) {
        super( Helper.capitalizeFirstLetterOfEveryWord(hotel.getHotelName()) +
            " hotel in " + Helper.capitalizeFirstLetterOfEveryWord(hotel.getCity()) +
            " has an existing " + type + " contract (ID = " + contractId + ") with the " +
            Helper.capitalizeFirstLetterOfEveryWord(roomType) + " room type which overlaps with the given time period");
    }
}
