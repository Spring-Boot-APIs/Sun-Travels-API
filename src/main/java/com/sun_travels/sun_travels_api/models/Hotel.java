package com.sun_travels.sun_travels_api.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "hotel")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Hotel implements Serializable {

    @Id
    @SequenceGenerator( name = "hotel_sequence", sequenceName = "hotel_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hotel_sequence")
    @Column(name = "hotel_id", nullable = false, updatable = false)
    private Long hotelId;

    @Column(name = "hotel_name", length = 100, nullable = false)
    private String hotelName;

    @Column(name = "city", length = 30, nullable = false)
    private String city;

    public Hotel( String hotelName, String city ) {
        this.hotelName = hotelName;
        this.city = city;
    }
}