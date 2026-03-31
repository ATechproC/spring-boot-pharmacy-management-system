package com.atechproc.service.supplier;

import com.atechproc.dto.PharmacyDto;
import com.atechproc.dto.SupplierDto;
import com.atechproc.enums.ACCOUNT_STATUS;
import com.atechproc.enums.USER_ROLE;
import com.atechproc.exception.AlreadyExistsException;
import com.atechproc.exception.ResourceNotFoundException;
import com.atechproc.mapper.SupplierMapper;
import com.atechproc.model.Medicine;
import com.atechproc.model.Pharmacy;
import com.atechproc.model.Supplier;
import com.atechproc.model.User;
import com.atechproc.repository.PharmacyRepository;
import com.atechproc.repository.SupplierRepository;
import com.atechproc.request.supplier.CreateSupplierRequest;
import com.atechproc.request.supplier.UpdateSupplierRequest;
import com.atechproc.service.business.IBusinessLogicService;
import com.atechproc.service.medicine.IMedicineService;
import com.atechproc.service.pharmacy.IPharmacyService;
import com.atechproc.service.user.IUserService;
import com.atechproc.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplierService implements ISupplierService {

    private final SupplierRepository supplierRepository;
    private final IPharmacyService pharmacyService;
    private final IBusinessLogicService businessLogicService;
    private final Utils utils;

    @Override
    public Supplier getSupplierById(Long id) {
        Supplier supplier = supplierRepository.findByIdAndActive(id, true);
        if (supplier == null) {
            throw new ResourceNotFoundException("Supplier not found");
        }
        return supplier;
    }

    @Override
    @PreAuthorize("hasAnyRole('PHARMACY_OWNER', 'PHARMACIST')")
    public SupplierDto createSupplier(CreateSupplierRequest request, String jwt) throws Exception {

        Pharmacy pharmacy = utils.checkPharmacyAndUserStatus(jwt);

        Supplier supplier = supplierRepository.findByNameAndPharmacy_id(request.getName(), pharmacy.getId());

        if (supplier != null) {
            supplier.setActive(true);
        } else
            supplier = new Supplier();

        supplier.setName(request.getName());
        supplier.setPharmacy(pharmacy);

        Supplier savedSupplier = supplierRepository.save(supplier);

        return SupplierMapper.toDto(savedSupplier, businessLogicService);
    }

    @Override
    @PreAuthorize("hasAnyRole('PHARMACY_OWNER', 'PHARMACIST')")
    public SupplierDto updateSupplier(UpdateSupplierRequest request, Long id, String jwt)
            throws Exception {

        Pharmacy pharmacy = utils.checkPharmacyAndUserStatus(jwt);

        Supplier supplier = getSupplierById(id);
        if (!pharmacy.getSuppliers().contains(supplier)) {
            throw new Exception("You are not allowed to update this category name");
        }

        if (request.getName() != null) {
            supplier.setName(request.getName());
        }

        Supplier savedSupplier = supplierRepository.save(supplier);

        return SupplierMapper.toDto(savedSupplier, businessLogicService);
    }

    @Override
    public List<SupplierDto> getAllSuppliers(String jwt) throws Exception {

        Pharmacy pharmacy = utils.checkPharmacyAndUserStatus(jwt);
        List<Supplier> suppliers = supplierRepository.findByPharmacy_idAndActive(pharmacy.getId(), true);
        return SupplierMapper.toDTOs(suppliers, businessLogicService);
    }

    @Override
    @PreAuthorize("hasAnyRole('PHARMACY_OWNER', 'PHARMACIST')")
    public void deleteSupplier(Long id, String jwt) throws Exception {

        Pharmacy pharmacy = utils.checkPharmacyAndUserStatus(jwt);

        Supplier supplier = getSupplierById(id);
        if (!pharmacy.getSuppliers().contains(supplier)) {
            throw new Exception("You are not allowed to delete this supplier");
        }

        supplier.setActive(false);
        supplierRepository.save(supplier);
    }

    @Override
    public List<SupplierDto> searchForSupplier(String keyword, String jwt) throws Exception {

        Pharmacy pharmacy = utils.checkPharmacyAndUserStatus(jwt);
        List<Supplier> suppliers = supplierRepository.searchForSupplier(keyword, pharmacy.getId());
        return SupplierMapper.toDTOs(suppliers, businessLogicService);
    }
}
