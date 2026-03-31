package com.atechproc.service.taxes;

import com.atechproc.dto.TaxDto;
import com.atechproc.enums.ACCOUNT_STATUS;
import com.atechproc.exception.AlreadyExistsException;
import com.atechproc.exception.ResourceNotFoundException;
import com.atechproc.mapper.TaxMapper;
import com.atechproc.model.Pharmacy;
import com.atechproc.model.Tax;
import com.atechproc.model.User;
import com.atechproc.repository.TaxRepository;
import com.atechproc.request.taxes.AddNewTaxRequest;
import com.atechproc.request.taxes.UpdateTaxRequest;
import com.atechproc.service.pharmacy.IPharmacyService;
import com.atechproc.service.user.IUserService;
import com.atechproc.utils.Utils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
@RequiredArgsConstructor
public class TaxesService implements ITaxesService {

    private final TaxRepository taxRepository;
    private final IPharmacyService pharmacyService;
    private final Utils utils;

    @Override
    public Tax getTaxById(Long id) {
        return taxRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tax not found with id "+id));
    }

    @Override
    @PreAuthorize("hasRole('PHARMACY_OWNER')")
    public TaxDto addNewTax(AddNewTaxRequest request, String jwt) throws Exception {

        Pharmacy pharmacy = utils.checkPharmacyAndUserStatus(jwt);

        Tax existingTax = taxRepository.findByNameAndYearMonthAndPharmacy_id(request.getName(),
                YearMonth.now().toString(), pharmacy.getId());
        if (existingTax != null) {
            throw new AlreadyExistsException("Tax already exits with name " + request.getName());
        }

        Tax tax = new Tax();
        tax.setName(request.getName());
        tax.setDescription(request.getDescription());
        tax.setTotal(request.getTotal());
        tax.setPharmacy(pharmacy);

        Tax savedTax = taxRepository.save(tax);

        return TaxMapper.toDto(savedTax);
    }

    @Override
    @PreAuthorize("hasRole('PHARMACY_OWNER')")
    public TaxDto updateTax(Long id, UpdateTaxRequest request, String jwt) throws Exception {

        Pharmacy pharmacy = utils.checkPharmacyAndUserStatus(jwt);

        Tax tax = getTaxById(id);
        if (!pharmacy.getTaxes().contains(tax)) {
            throw new Exception("You don't have the rights to update this tax details");
        }

        if (request.getName() != null) {
            tax.setName(request.getName());
        }
        if (request.getDescription() != null) {
            tax.setDescription(request.getDescription());
        }
        if (request.getTotal() != null) {
            tax.setTotal(request.getTotal());
        }

        Tax savedTax = taxRepository.save(tax);

        return TaxMapper.toDto(savedTax);
    }

    @Override
    @PreAuthorize("hasRole('PHARMACY_OWNER')")
    public List<TaxDto> getCurrentMonthTaxes(String jwt) throws Exception {
        Pharmacy pharmacy = utils.checkPharmacyAndUserStatus(jwt);
        List<Tax> taxes = taxRepository.findByPharmacy_idAndYearMonth(pharmacy.getId(), YearMonth.now().toString());
        return TaxMapper.toDTOs(taxes);
    }

    @Override
    @PreAuthorize("hasRole('PHARMACY_OWNER')")
    public void deleteTax(Long id, String jwt) throws Exception {

        Pharmacy pharmacy = utils.checkPharmacyAndUserStatus(jwt);

        Tax tax = getTaxById(id);
        if (!pharmacy.getTaxes().contains(tax)) {
            throw new Exception("You don't have the rights to delete this tax");
        }

        pharmacy.getTaxes().remove(tax);
        taxRepository.deleteById(tax.getId());
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('PHARMACY_OWNER')")
    public void deleteCurrentMonthTaxes(String jwt) throws Exception {
        Pharmacy pharmacy = utils.checkPharmacyAndUserStatus(jwt);
        pharmacy.getTaxes().clear();
        taxRepository.deleteByPharmacy_id(pharmacy.getId());
    }

    @Override
    @PreAuthorize("hasRole('PHARMACY_OWNER')")
    public List<TaxDto> getMonthTaxes(String jwt, YearMonth yearMonth) throws Exception {
        Pharmacy pharmacy = utils.checkPharmacyAndUserStatus(jwt);
        List<Tax> taxes = taxRepository.findByPharmacy_idAndYearMonth(pharmacy.getId(), yearMonth.toString());
        return TaxMapper.toDTOs(taxes);
    }

    @Override
    @PreAuthorize("hasRole('PHARMACY_OWNER')")
    public List<TaxDto> getYearTaxes(String jwt, int year) throws Exception {
        Pharmacy pharmacy = utils.checkPharmacyAndUserStatus(jwt);
        List<Tax> taxes = taxRepository.findByPharmacy_idAndYear(pharmacy.getId(),year);
        return TaxMapper.toDTOs(taxes);
    }

}
