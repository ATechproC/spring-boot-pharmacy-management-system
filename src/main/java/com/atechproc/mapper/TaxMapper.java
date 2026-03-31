package com.atechproc.mapper;

import com.atechproc.dto.TaxDto;
import com.atechproc.model.Tax;

import java.util.List;

public class TaxMapper {
    public static TaxDto toDto(Tax tax) {
        TaxDto dto = new TaxDto();
        dto.setId(tax.getId());
        dto.setName(tax.getName());
        dto.setDescription(tax.getDescription());
        dto.setTotal(tax.getTotal());
        return dto;
    }

    public static List<TaxDto> toDTOs(List<Tax> taxes) {
        return taxes.stream()
                .map(TaxMapper::toDto).toList();
    }
}
