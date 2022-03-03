package com.sun_travels.sun_travels_api.services;

import com.sun_travels.sun_travels_api.Helper;
import com.sun_travels.sun_travels_api.exceptions.HotelAlreadyExistsException;
import com.sun_travels.sun_travels_api.models.Hotel;
import com.sun_travels.sun_travels_api.repositories.HotelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class HotelServiceTest {

    @Mock private HotelRepository hotelRepository;
    private HotelService hotelService;
    private Hotel hotel;

    @BeforeEach
    void setUp() {
        hotelService = new HotelService(hotelRepository);
        hotel = new Hotel( "marino beach", "colombo");
    }

    @Test
    void canAddHotel() {
        hotelService.addHotel(hotel);
        ArgumentCaptor<Hotel> hotelArgumentCaptor = ArgumentCaptor.forClass(Hotel.class);
        // Verify that the repository was invoked with the save method
        // Capture the Hotel object which is passed inside the save method
        verify(hotelRepository).save(hotelArgumentCaptor.capture());
        Hotel capturedHotel = hotelArgumentCaptor.getValue();
        assertThat(capturedHotel).isEqualTo(hotel);
    }

    @Test
    void willThrowWhenHotelExists() {
        given(hotelRepository.findHotelByHotelNameAndCity(hotel.getHotelName(), hotel.getCity()))
            .willReturn(java.util.Optional.of(hotel));
        assertThatThrownBy(() -> hotelService.addHotel(hotel))
            .isInstanceOfAny(HotelAlreadyExistsException.class)
            .hasMessageContaining(Helper.capitalizeFirstLetterOfEveryWord(hotel.getHotelName())+
                " hotel in " + Helper.capitalizeFirstLetterOfEveryWord(hotel.getCity())+
                " already exists in the system with the ID: " + hotel.getHotelId());
        verify(hotelRepository, never()).save(any(Hotel.class));
    }

    @Test
    void canGetHotels() {
        hotelService.getHotels();
        verify(hotelRepository).findAll();
    }

    @Test
    void canGetCities() {
        hotelService.getCities();
        verify(hotelRepository).findAllCities();
    }
}