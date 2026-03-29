package com.atechproc.service.business;

import com.atechproc.dto.MedicineDto;
import com.atechproc.mapper.MedicineMapper;
import com.atechproc.model.Medicine;
import com.atechproc.model.Supplier;
import com.atechproc.repository.MedicineRepository;
import com.atechproc.service.medicine.IMedicineService;
import com.atechproc.service.supplier.ISupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BusinessLogicService implements IBusinessLogicService {

    private final MedicineRepository medicineRepository;

    @Override
    public Long countNumberOfActiveMedicinesInSupplier(Long pharmacyId, Long supplierId) {
        return medicineRepository.countByPharmacy_idAndSupplier_idAndActive(pharmacyId, supplierId, true);
    }

    @Override
    public Long countNumberOfActiveMedicinesInGroup(Long pharmacyId, Long groupId) {
        return medicineRepository.countByPharmacy_idAndCategory_idAndActive(pharmacyId, groupId, true);
    }
}
