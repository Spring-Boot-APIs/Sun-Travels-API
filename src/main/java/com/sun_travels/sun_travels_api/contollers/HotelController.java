package com.sun_travels.sun_travels_api.contollers;

import com.sun_travels.sun_travels_api.models.Hotel;
import com.sun_travels.sun_travels_api.services.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api.sun-travels.com/hotel")
public class HotelController {
    private final HotelService hotelService;

    @Autowired
    public HotelController( HotelService hotelService ) { this.hotelService = hotelService; }

    @PostMapping()
    public ResponseEntity<Hotel> addEmployee( @RequestBody Hotel hotel ) {
        Hotel newHotel = hotelService.addHotel( hotel );
        return new ResponseEntity<>( newHotel, HttpStatus.CREATED);
    }

    @GetMapping("/hotels")
    public ResponseEntity<List<Hotel>> getHotels() {
        List<Hotel> hotels = hotelService.getHotels();
        return new ResponseEntity<>(hotels, HttpStatus.OK);
    }

    @GetMapping("/cities")
    public ResponseEntity<List<String>> getCities() {
        List<String> cities = hotelService.getCities();
        return new ResponseEntity<>(cities, HttpStatus.OK);
    }
}
