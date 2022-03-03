package com.sun_travels.sun_travels_api.services;

import com.sun_travels.sun_travels_api.Helper;
import com.sun_travels.sun_travels_api.dtos.SearchAvailabilityQuery;
import com.sun_travels.sun_travels_api.dtos.SearchAvailabilityRequest;
import com.sun_travels.sun_travels_api.dtos.SearchAvailabilityResponse;
import com.sun_travels.sun_travels_api.dtos.SearchRoomRequest;
import com.sun_travels.sun_travels_api.exceptions.NoRoomsAvailableException;
import com.sun_travels.sun_travels_api.repositories.ContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class SearchService {
    private final ContractRepository contractRepository;

    @Autowired
    public SearchService( ContractRepository contractRepository ) { this.contractRepository = contractRepository; }

    public List<SearchAvailabilityResponse> searchAvailability( SearchAvailabilityRequest request ) {
        LocalDate checkOutDate = request.getCheckInDate().plusDays( request.getNoOfNights() );
        int roomCount = 0;
        int adultCount = 0;
        int maxNoOfAdults = 1;
        for( SearchRoomRequest combination: request.getCombinations()) {
            roomCount += combination.getNoOfRooms();
            adultCount += (combination.getNoOfAdults() * combination.getNoOfRooms());
            if (maxNoOfAdults < combination.getNoOfAdults()) { maxNoOfAdults = combination.getNoOfAdults(); }
            // Assumption : n adults can stay in room where maximum no. of adults of the room type is >= n
        }
        List<SearchAvailabilityQuery> options = contractRepository.findAvailableRooms(
                request.getCity(), request.getCheckInDate(), checkOutDate, roomCount, maxNoOfAdults
        );
        if ( options.isEmpty() ) { throw new NoRoomsAvailableException( request, checkOutDate ); }
        int generalIndex = Helper.getGeneralIndex(options);
        while(generalIndex != -1) {     // Exits from while loop when no similar room types from the same hotel exists
            options.remove(generalIndex);
            generalIndex = Helper.getGeneralIndex(options);
        }
        List<SearchAvailabilityResponse> results = new ArrayList<>();
        for(SearchAvailabilityQuery option: options) {
            results.add(new SearchAvailabilityResponse(
                option.getHotelName(), option.getCity(), option.getRoomType(),
                option.getPrice() * ((option.getMarkup()+100)/100) * request.getNoOfNights() * adultCount
            )); // Rates are considered as per person, per night. Therefore, price is calculated per person
        }
        return results;
    }
}
