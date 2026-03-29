package com.atechproc.mapper;

import com.atechproc.dto.AddressDto;
import com.atechproc.model.Address;

public class AddressMapper {
    public static AddressDto toDto(Address address) {
        AddressDto dto = new AddressDto();
        dto.setId(address.getId());
        dto.setCity(address.getCity());
        dto.setCountry(address.getCountry());
        dto.setStreet(address.getStreet());
        return dto;
    }
}
