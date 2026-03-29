package com.atechproc.mapper;

import com.atechproc.dto.CategoryDto;
import com.atechproc.model.Category;
import com.atechproc.service.business.IBusinessLogicService;

import java.util.List;

public class CategoryMapper {
    public static CategoryDto toDto(Category category, IBusinessLogicService businessLogicService) {
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setMedicines(MedicineMapper.toDTOs(category.getMedicines()));
        dto.setActive(category.isActive());

        Long numberOfActiveMedicines = businessLogicService.countNumberOfActiveMedicinesInGroup(
                category.getPharmacy().getId(), category.getId()
        );

        dto.setNumberOfActiveMedicines(numberOfActiveMedicines);

        return dto;
    }

    public static List<CategoryDto> toDTOs(List<Category> categories, IBusinessLogicService businessLogicService) {
        return categories.stream()
                .map(category -> toDto(category, businessLogicService)).toList();
    }
}
