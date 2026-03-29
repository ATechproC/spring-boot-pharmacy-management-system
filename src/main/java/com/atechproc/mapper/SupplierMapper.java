package com.atechproc.mapper;

import com.atechproc.dto.SupplierDto;
import com.atechproc.model.Supplier;
import com.atechproc.service.business.IBusinessLogicService;
import com.atechproc.service.medicine.IMedicineService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

public class SupplierMapper {

    public static SupplierDto toDto(Supplier supplier, IBusinessLogicService businessLogicService) {
        SupplierDto dto = new SupplierDto();
        dto.setId(supplier.getId());
        dto.setName(supplier.getName());
        dto.setMedicines(MedicineMapper.toDTOs(supplier.getMedicines()));
        dto.setActive(supplier.isActive());

        Long numberOfActiveMedicines = businessLogicService.countNumberOfActiveMedicinesInSupplier(
                supplier.getPharmacy().getId(), supplier.getId()
        );

        dto.setNumberOfActiveMedicines(numberOfActiveMedicines);

        return dto;
    }

    public static List<SupplierDto> toDTOs(List<Supplier> suppliers, IBusinessLogicService businessLogicService) {
        return suppliers.stream()
                .map(supplier -> toDto(supplier, businessLogicService)).toList();
    }
}
