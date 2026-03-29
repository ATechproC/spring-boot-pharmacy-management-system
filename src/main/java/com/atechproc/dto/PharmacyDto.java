package com.atechproc.dto;

import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PharmacyDto {
    private Long id;
    private boolean isOpen;
    private String name;
    private String openingTime;
    private String closingTime;
    private List<SupplierDto> suppliers;
    private List<MedicineDto> medicines;
    private List<CategoryDto> categories;
    private AddressDto address;
    private boolean isActive;
    private UserDto owner;
}
