package com.sun_travels.sun_travels_api.dtos;

import com.sun_travels.sun_travels_api.Helper;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class SearchAvailabilityResponse {

    private String hotelName;
    private String city;
    private String roomType;
    private double markedUpPrice;

    public SearchAvailabilityResponse( String hotelName, String city, String roomType, double markedUpPrice ) {
        this.hotelName = hotelName;
        this.city = city;
        this.roomType = roomType;
        this.markedUpPrice = Helper.roundToTwoDecimals( markedUpPrice );
    }
}
