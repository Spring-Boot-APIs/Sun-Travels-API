package com.sun_travels.sun_travels_api.repositories;

import com.sun_travels.sun_travels_api.models.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    /**
     * Returns an optional of the hotel if exists
     * @param hotel_name Name of the hotel
     * @param city City of the hotel
     * @return Optional hotel object
     */
    @Query("SELECT h FROM Hotel h WHERE h.hotelName = ?1 AND h.city = ?2")
    Optional<Hotel> findHotelByHotelNameAndCity(String hotel_name, String city);

    /**
     * Returns all the cities
     * @return List of cities(Strings)
     */
    @Query(value = "SELECT DISTINCT(city) FROM hotel", nativeQuery = true)
    List<String> findAllCities();
}
