package com.sun_travels.sun_travels_api.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class RoomPK implements Serializable {
    @Column(name = "contract_id", nullable = false, updatable = false)
    private Long contractId;

    @Column(name = "room_type", length = 50, nullable = false, updatable = false)
    private String roomType;
}
