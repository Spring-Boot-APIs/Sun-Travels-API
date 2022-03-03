package com.sun_travels.sun_travels_api.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AddRoomRequest {

    private String roomType;
    private double price;
    private int noOfRooms;
    private int maxAdults;
}
