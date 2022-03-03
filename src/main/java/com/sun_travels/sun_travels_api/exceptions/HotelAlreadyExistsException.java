package com.sun_travels.sun_travels_api.exceptions;

import com.sun_travels.sun_travels_api.Helper;
import com.sun_travels.sun_travels_api.models.Hotel;

public class HotelAlreadyExistsException extends RuntimeException {
    public HotelAlreadyExistsException( Hotel hotel ) {
        super( Helper.capitalizeFirstLetterOfEveryWord(hotel.getHotelName())+
            " hotel in " + Helper.capitalizeFirstLetterOfEveryWord(hotel.getCity())+
            " already exists in the system with the ID: " + hotel.getHotelId());
    }
}
