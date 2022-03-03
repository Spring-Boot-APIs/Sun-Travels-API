package com.sun_travels.sun_travels_api.dtos;

import com.sun_travels.sun_travels_api.enums.ContractType;

public interface SearchAvailabilityQuery {
    public Long getHotelId();
    public String getHotelName();
    public String getCity();
    public ContractType getContractType();
    public double getMarkup();
    public String getRoomType();
    public double getPrice();
}