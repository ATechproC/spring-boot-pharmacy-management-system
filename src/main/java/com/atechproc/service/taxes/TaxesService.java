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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaxesService implements ITaxesService {

    private final TaxRepository taxRepository;
    private final IPharmacyService pharmacyService;
    private final IUserService userService;

    @Override
    public Tax getTaxById(Long id) {
        Tax tax = taxRepository.findByIdAndIsActive(id, true);
        if(tax == null) {
            throw new ResourceNotFoundException("Tax not found with id "+id);
        }
        return tax;
    }

    @Override
    public TaxDto addNewTax(AddNewTaxRequest request, String jwt) throws Exception {

        Pharmacy pharmacy = pharmacyService.getPharmacyByUser(jwt);

        if (!pharmacy.isOpen()) {
            throw new Exception("You can't achieve this actions because the pharmacy is closed for the moments");
        }
        User user = userService.getUserProfile(jwt);

        if (user.getStatus().equals(ACCOUNT_STATUS.REFUSED)) {
            throw new Exception("You cant achieve this actions because your account is REFUSED");
        }

        if (user.getStatus().equals(ACCOUNT_STATUS.PENDING)) {
            throw new Exception("You cant achieve this actions because your account is PENDING");
        }

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
    public TaxDto updateTax(Long id, UpdateTaxRequest request, String jwt) throws Exception {

        Pharmacy pharmacy = pharmacyService.getPharmacyByUser(jwt);

        if (!pharmacy.isOpen()) {
            throw new Exception("You can't achieve this actions because the pharmacy is closed for the moments");
        }
        User user = userService.getUserProfile(jwt);

        if (user.getStatus().equals(ACCOUNT_STATUS.REFUSED)) {
            throw new Exception("You cant achieve this actions because your account is REFUSED");
        }

        if (user.getStatus().equals(ACCOUNT_STATUS.PENDING)) {
            throw new Exception("You cant achieve this actions because your account is PENDING");
        }

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
    public List<TaxDto> getTaxes(String jwt) {
        Pharmacy pharmacy = pharmacyService.getPharmacyByUser(jwt);
        List<Tax> taxes = taxRepository.findByPharmacy_id(pharmacy.getId());
        return TaxMapper.toDTOs(taxes);
    }

    @Override
    public void deleteTax(Long id, String jwt) throws Exception {

        Pharmacy pharmacy = pharmacyService.getPharmacyByUser(jwt);

        if (!pharmacy.isOpen()) {
            throw new Exception("You can't achieve this actions because the pharmacy is closed for the moments");
        }
        User user = userService.getUserProfile(jwt);

        if (user.getStatus().equals(ACCOUNT_STATUS.REFUSED)) {
            throw new Exception("You cant achieve this actions because your account is REFUSED");
        }

        if (user.getStatus().equals(ACCOUNT_STATUS.PENDING)) {
            throw new Exception("You cant achieve this actions because your account is PENDING");
        }

        Tax tax = getTaxById(id);
        if (!pharmacy.getTaxes().contains(tax)) {
            throw new Exception("You don't have the rights to delete this tax");
        }

        tax.setActive(false);
    }
}
