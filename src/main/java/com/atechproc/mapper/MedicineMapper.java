package com.atechproc.mapper;

import com.atechproc.dto.*;
import com.atechproc.model.Medicine;

import java.util.List;

public class MedicineMapper {
    public static MedicineDto toDto(Medicine medicine) {
        MedicineDto dto = new MedicineDto();

        SupplierInfoDto supplier = new SupplierInfoDto();
        supplier.setName(medicine.getSupplier().getName());
        supplier.setId(medicine.getSupplier().getId());

        CategoryInfoDto category = new CategoryInfoDto();
        category.setId(medicine.getCategory().getId());
        category.setName(medicine.getCategory().getName());

        dto.setId(medicine.getId());
        dto.setName(medicine.getName());
        dto.setProfit(medicine.getProfit());
        dto.setImages(ImageMapper.toDTOs(medicine.getImages()));
        dto.setQuantity(medicine.getQuantity());
        dto.setStatus(medicine.getStatus().toString());
        dto.setExpiredDate(medicine.getExpiredDate());
        dto.setPurchasePrice(medicine.getPurchasePrice());
        dto.setSellingPrice(medicine.getSellingPrice());
        dto.setBatchNumber(medicine.getBatchNumber());
        dto.setType(medicine.getType().toString());
        dto.setSupplier(supplier);
        dto.setCategory(category);
        dto.setLow(medicine.isLow());
        dto.setSoldQuantity(medicine.getSoldQuantity());
        dto.setInStock(medicine.isInStock());
        dto.setActive(medicine.isActive());

        return dto;
    }


    public static List<MedicineDto> toDTOs(List<Medicine> medicines) {
        return medicines.stream()
                .map(MedicineMapper::toDto).toList();
    }

    public static CategoryMedicineDto toCategoryMedicineDto(Medicine medicine) {
        CategoryMedicineDto dto = new CategoryMedicineDto();

        SupplierInfoDto supplier = new SupplierInfoDto();
        supplier.setName(medicine.getSupplier().getName());
        supplier.setId(medicine.getSupplier().getId());

        dto.setId(medicine.getId());
        dto.setName(medicine.getName());
        dto.setProfit(medicine.getProfit());
        dto.setImages(ImageMapper.toDTOs(medicine.getImages()));
        dto.setQuantity(medicine.getQuantity());
        dto.setStatus(medicine.getStatus().toString());
        dto.setExpiredDate(medicine.getExpiredDate());
        dto.setPurchasePrice(medicine.getPurchasePrice());
        dto.setSellingPrice(medicine.getSellingPrice());
        dto.setBatchNumber(medicine.getBatchNumber());
        dto.setType(medicine.getType().toString());
        dto.setLow(medicine.isLow());
        dto.setSoldQuantity(medicine.getSoldQuantity());
        dto.setInStock(medicine.isInStock());
        dto.setActive(medicine.isActive());
        dto.setSupplier(supplier);

        return dto;
    }

    public static List<CategoryMedicineDto> toCategoryMedicineDTOs(List<Medicine> medicines) {
        return medicines.stream()
                .map(MedicineMapper::toCategoryMedicineDto).toList();
    }
}
