package com.atechproc.dto;

import lombok.Data;

import java.util.List;

@Data
public class CategoryDto {
    private Long id;
    private String name;
    private List<MedicineDto> medicines;
    private boolean active;
    private Long numberOfActiveMedicines;
}
