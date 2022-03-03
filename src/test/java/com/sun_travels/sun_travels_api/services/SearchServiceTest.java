package com.sun_travels.sun_travels_api.services;

import com.sun_travels.sun_travels_api.Helper;
import com.sun_travels.sun_travels_api.dtos.SearchAvailabilityQuery;
import com.sun_travels.sun_travels_api.dtos.SearchAvailabilityRequest;
import com.sun_travels.sun_travels_api.dtos.SearchRoomRequest;
import com.sun_travels.sun_travels_api.enums.ContractType;
import com.sun_travels.sun_travels_api.exceptions.NoRoomsAvailableException;
import com.sun_travels.sun_travels_api.repositories.ContractRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {

    @Mock private ContractRepository contractRepository;
    private SearchService searchService;
    private SearchAvailabilityRequest request;

    @BeforeEach
    void setUp() {
        searchService = new SearchService(contractRepository);
        request = new SearchAvailabilityRequest("colombo", LocalDate.parse("2022-04-05"), 2,
                new ArrayList<>(Arrays.asList(new SearchRoomRequest( 1, 1), new SearchRoomRequest(2,2))));
    }

    @Test
    void canSearchAvailability() {
        given(contractRepository.findAvailableRooms(request.getCity(), request.getCheckInDate(), LocalDate.parse("2022-04-07"), 3, 2))
                .willReturn(new ArrayList<>(Collections.singletonList(getMockResponse())));
        searchService.searchAvailability(request);
        ArgumentCaptor<String> cityArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<LocalDate> checkInDateArgumentCaptor = ArgumentCaptor.forClass(LocalDate.class);
        ArgumentCaptor<LocalDate> checkOutDateArgumentCaptor = ArgumentCaptor.forClass(LocalDate.class);
        ArgumentCaptor<Integer> roomCountArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> maxNoOfAdultsArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(contractRepository).findAvailableRooms(
            cityArgumentCaptor.capture(),
            checkInDateArgumentCaptor.capture(),
            checkOutDateArgumentCaptor.capture(),
            roomCountArgumentCaptor.capture(),
            maxNoOfAdultsArgumentCaptor.capture()
        );
        assertThat(cityArgumentCaptor.getValue()).isEqualTo("colombo");
        assertThat(checkInDateArgumentCaptor.getValue()).isEqualTo("2022-04-05");
        assertThat(checkOutDateArgumentCaptor.getValue()).isEqualTo("2022-04-07");
        assertThat(roomCountArgumentCaptor.getValue()).isEqualTo(3);
        assertThat(maxNoOfAdultsArgumentCaptor.getValue()).isEqualTo(2);
    }

    @Test
    void willThrowWhenNoRoomsAvailable() {
        given(contractRepository.findAvailableRooms(request.getCity(), request.getCheckInDate(), LocalDate.parse("2022-04-07"), 3, 2))
                .willReturn(new ArrayList<>());
        assertThatThrownBy(() -> searchService.searchAvailability(request))
                .isInstanceOfAny( NoRoomsAvailableException.class)
                .hasMessageContaining( "No rooms available in any hotel in " +
                    Helper.capitalizeFirstLetterOfEveryWord(request.getCity()) +
                    " from " + request.getCheckInDate() + " to 2022-04-07");
    }

    private SearchAvailabilityQuery getMockResponse() {
        return new SearchAvailabilityQuery() {
            @Override public Long getHotelId() { return null; }
            @Override public String getHotelName() { return null; }
            @Override public String getCity() { return null; }
            @Override public ContractType getContractType() { return null; }
            @Override public double getMarkup() { return 0; }
            @Override public String getRoomType() { return null; }
            @Override public double getPrice() { return 0; }
        };
    }
}