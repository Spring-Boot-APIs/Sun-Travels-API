package com.sun_travels.sun_travels_api.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SearchAvailabilityRequest {

    private String city;
    private LocalDate checkInDate;
    private int noOfNights;
    private List<SearchRoomRequest> combinations;
}
