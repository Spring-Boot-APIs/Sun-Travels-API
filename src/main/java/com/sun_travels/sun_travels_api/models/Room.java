package com.sun_travels.sun_travels_api.models;

import com.sun_travels.sun_travels_api.Helper;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "room")
@Data
@NoArgsConstructor
@ToString
public class Room implements Serializable {

    @EmbeddedId
    private RoomPK roomPK;

    @Column(name = "price", precision = 8, scale = 2, nullable = false)
    private double price;

    @Column(name = "no_of_rooms", columnDefinition = "SMALLINT", nullable = false)
    private int noOfRooms;

    @Column(name = "max_adults", columnDefinition = "TINYINT", nullable = false)
    private int maxAdults;

    public Room( RoomPK roomPK, double price, int noOfRooms, int maxAdults ) {
        this.roomPK = roomPK;
        this.price = Helper.roundToTwoDecimals(price);
        this.noOfRooms = noOfRooms;
        this.maxAdults = maxAdults;
    }
}
