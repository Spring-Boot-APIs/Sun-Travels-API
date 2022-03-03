package com.sun_travels.sun_travels_api.services;

import com.sun_travels.sun_travels_api.exceptions.HotelAlreadyExistsException;
import com.sun_travels.sun_travels_api.models.Hotel;
import com.sun_travels.sun_travels_api.repositories.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HotelService {
    private final HotelRepository hotelRepository;

    @Autowired
    public HotelService( HotelRepository hotelRepository ) { this.hotelRepository = hotelRepository; }

    public Hotel addHotel( Hotel hotel ) {
        validateUserInputs( hotel );
        return hotelRepository.save( hotel );   // Add hotel to the database
    }

    public List<Hotel> getHotels() { return hotelRepository.findAll(); }

    public List<String> getCities() { return hotelRepository.findAllCities(); }

    private void validateUserInputs( Hotel hotel ) {
        hotel.setHotelName(hotel.getHotelName().trim().toLowerCase());
        hotel.setCity(hotel.getCity().trim().toLowerCase());
        Optional<Hotel> hotelOptional = hotelRepository.findHotelByHotelNameAndCity(hotel.getHotelName(), hotel.getCity());
        if (hotelOptional.isPresent()) {    // Hotel already exists in the database
            throw new HotelAlreadyExistsException(hotelOptional.get());
        }
    }
}