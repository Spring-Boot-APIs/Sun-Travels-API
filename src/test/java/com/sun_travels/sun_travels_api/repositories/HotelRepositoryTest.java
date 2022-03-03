package com.sun_travels.sun_travels_api.repositories;

import com.sun_travels.sun_travels_api.models.Hotel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class HotelRepositoryTest {
    @Autowired private HotelRepository hotelRepository;
    private Hotel hotel;

    @BeforeEach
    void setUp() { hotel = new Hotel( "marino beach", "colombo"); }

    @AfterEach
    void tearDown() { hotelRepository.deleteAll(); }    // Clean state after each test

    @Test
    void canFindHotelByHotelNameAndCity() {
        hotelRepository.save(hotel);
        Optional<Hotel> hotelOptional = hotelRepository.findHotelByHotelNameAndCity(hotel.getHotelName(), hotel.getCity());
        assertThat(hotelOptional).isPresent();
        assertThat(hotelOptional).contains(hotel);
    }

    @Test
    void notPresentWhenHotelIsNotInDB() {
        Optional<Hotel> hotelOptional = hotelRepository.findHotelByHotelNameAndCity(hotel.getHotelName(), hotel.getCity());
        assertThat(hotelOptional).isNotPresent();
    }

    @Test
    void canFindAllCities() {
        hotelRepository.save(hotel);
        List<String> cities = hotelRepository.findAllCities();
        assertFalse(cities.isEmpty());
    }

    @Test
    void emptyWhenNoHotelsInDB() {
        List<String> cities = hotelRepository.findAllCities();
        assertTrue(cities.isEmpty());
    }
}