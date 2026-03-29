package com.atechproc.mapper;

import com.atechproc.dto.PharmacyDto;
import com.atechproc.model.Pharmacy;
import com.atechproc.service.business.IBusinessLogicService;
import com.atechproc.service.medicine.IMedicineService;

import java.util.List;

public class PharmacyMapper {

    public static PharmacyDto toDto(Pharmacy pharmacy, IBusinessLogicService businessLogicService) {

        PharmacyDto dto = new PharmacyDto();
        dto.setId(pharmacy.getId());
        dto.setName(pharmacy.getName());
        dto.setOpen(pharmacy.isOpen());
        dto.setOpeningTime(pharmacy.getOpeningTime().toString());
        dto.setClosingTime(pharmacy.getClosingTime().toString());
        dto.setMedicines(MedicineMapper.toDTOs(pharmacy.getMedicines()));
        dto.setSuppliers(SupplierMapper.toDTOs(pharmacy.getSuppliers(), businessLogicService));
        dto.setCategories(CategoryMapper.toDTOs(pharmacy.getCategories(), businessLogicService));
        dto.setAddress(AddressMapper.toDto(pharmacy.getAddress()));
        dto.setActive(pharmacy.isActive());
        dto.setOwner(UserMapper.toDto(pharmacy.getOwner()));

        return dto;
    }

    public static List<PharmacyDto> toDTOs(List<Pharmacy> pharmacies, IBusinessLogicService businessLogicService) {
        return pharmacies.stream()
                .map(pharmacy ->  toDto(pharmacy, businessLogicService)).toList();
    }
}
