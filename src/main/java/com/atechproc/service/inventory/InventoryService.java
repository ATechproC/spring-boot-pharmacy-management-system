package com.atechproc.service.inventory;

import com.atechproc.dto.DashboardDetails;
import com.atechproc.dto.InventoryDetails;
import com.atechproc.enums.MEDICINE_STATUS;
import com.atechproc.enums.USER_ROLE;
import com.atechproc.model.Pharmacy;
import com.atechproc.repository.*;
import com.atechproc.service.pharmacy.IPharmacyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryService implements IInventoryService {
    private final MedicineRepository medicineRepository;
    private final CategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;
    private final CreditRepository creditRepository;
    private final UserRepository userRepository;
    private final IPharmacyService pharmacyService;
    private final TaxRepository taxRepository;

    @Override
    public InventoryDetails getInventoryDetails(String jwt) {
        Pharmacy pharmacy = pharmacyService.getPharmacyByUser(jwt);

        Long expiredMedicines = medicineRepository.countByStatusAndPharmacy_idAndActive(MEDICINE_STATUS.EXPIRED, pharmacy.getId(), true);
        Long suppliers = supplierRepository.countByPharmacy_idAndActive(pharmacy.getId(), true);
        Long groups = categoryRepository.countByPharmacy_idAndActive(pharmacy.getId(), true);
        Long pharmacists = userRepository.countByRoleAndPharmacy_id(USER_ROLE.PHARMACIST, pharmacy.getId());
        Long availableMedicines = medicineRepository.countByInStockAndPharmacy_idAndStatusAndInStockAndActive(true, pharmacy.getId(),MEDICINE_STATUS.AVAILABLE, true, true);
        Long medicinesOutOfStock = medicineRepository.countByInStockAndPharmacy_idAndActive(false, pharmacy.getId(), true);
        Long totalNumberOfMedicines = medicineRepository.countByPharmacy_idAndActive(pharmacy.getId(), true);
        Long totalOfCredits = creditRepository.countByPharmacy_id(pharmacy.getId());
        Long totalOfTaxes = taxRepository.countByPharmacy_id(pharmacy.getId());

        InventoryDetails details = new InventoryDetails();
        details.setAvailableMedicines(availableMedicines);
        details.setExpiredMedicines(expiredMedicines);
        details.setSuppliers(suppliers);
        details.setGroups(groups);
        details.setPharmacists(pharmacists);
        details.setMedicinesOutOfStock(medicinesOutOfStock);
        details.setTotalNumberOfMedicines(totalNumberOfMedicines);
        details.setTotalOfCredits(totalOfCredits);
        details.setTotalOfTaxes(totalOfTaxes);

        return details;
    }
}
