package com.sun_travels.sun_travels_api.dtos;

import com.sun_travels.sun_travels_api.enums.ContractType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AddContractRequest {

    private Long hotelId;
    private LocalDate startDate;
    private LocalDate endDate;
    private double markup;
    private ContractType contractType;
    private List<AddRoomRequest> rooms;
}
