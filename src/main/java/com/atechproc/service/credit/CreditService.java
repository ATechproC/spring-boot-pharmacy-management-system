package com.atechproc.service.credit;

import com.atechproc.dto.CreditDto;
import com.atechproc.enums.ACCOUNT_STATUS;
import com.atechproc.enums.CREDIT_STATUS;
import com.atechproc.exception.AlreadyExistsException;
import com.atechproc.exception.ResourceNotFoundException;
import com.atechproc.mapper.CreditMapper;
import com.atechproc.model.Credit;
import com.atechproc.model.Pharmacy;
import com.atechproc.model.User;
import com.atechproc.repository.CreditRepository;
import com.atechproc.request.credit.CreateCreditRequest;
import com.atechproc.service.pharmacy.IPharmacyService;
import com.atechproc.service.user.IUserService;
import com.atechproc.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CreditService implements ICreditService {

    private final CreditRepository creditRepository;
    private final IPharmacyService pharmacyService;
    private final Utils utils;

    @Override
    public Credit getCreditById(Long id) {
        return creditRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Credit not found with id "+id));
    }

    @Override
    @PreAuthorize("hasAnyRole('PHARMACY_OWNER', 'PHARMACIST')")
    public CreditDto createCredit(CreateCreditRequest request, String jwt) throws Exception {
        Pharmacy pharmacy = utils.checkPharmacyAndUserStatus(jwt);

        Credit existingCredit = creditRepository.findByClientIdAndPharmacy_id(request.getClientId(), pharmacy.getId());

        if(existingCredit != null) {
            throw new AlreadyExistsException("Client with CIN "+existingCredit.getClientId()+" already exits!");
        }

        if(request.getPaidAmount().compareTo(request.getTotalAmount()) >= 0) {
            throw new Exception("The total amount must be greater than the paid amount");
        }

        Credit credit = new Credit();
        credit.setClientId(request.getClientId());
        credit.setName(request.getName());
        credit.setTotalAmount(request.getTotalAmount());
        credit.setPaidAmount(request.getPaidAmount());
        credit.setRemainingAmount(credit.calculateRemainingAmount());
        credit.setPharmacy(pharmacy);
        credit.setPhone(request.getPhone());

        Credit savedCredit = creditRepository.save(credit);

        return CreditMapper.toDto(savedCredit);
    }

    @Override
    @PreAuthorize("hasAnyRole('PHARMACY_OWNER', 'PHARMACIST')")
    public CreditDto updateCreditPaidAmount(Long id, BigDecimal paidAmount, String jwt) throws Exception {
        Pharmacy pharmacy = utils.checkPharmacyAndUserStatus(jwt);

        Credit credit = getCreditById(id);

        if(!pharmacy.getCredits().contains(credit)) {
            throw new Exception("You don't have the rights to achieve this action");
        }

        if(paidAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new Exception("Paid Amount must be greater than 0");
        }

        if(paidAmount.compareTo(credit.getTotalAmount()) > 0) {
            throw new Exception("Paid Amount must be less or equals the total Amount");
        }

        credit.setPaidAmount(paidAmount);
        credit.setRemainingAmount(credit.calculateRemainingAmount());
        credit.setStatus(credit.updateStatus());
        Credit savedCredit = creditRepository.save(credit);

        System.out.println("################ "+savedCredit.getStatus());

        return CreditMapper.toDto(savedCredit);
    }

    @Override
    @PreAuthorize("hasAnyRole('PHARMACY_OWNER', 'PHARMACIST')")
    public List<CreditDto> getPharmacyCredits(String jwt) {
        Pharmacy pharmacy = pharmacyService.getPharmacyByUser(jwt);
        List<Credit> credits = creditRepository.findByPharmacy_id(pharmacy.getId());
        return CreditMapper.toDTOs(credits);
    }

    @Override
    @PreAuthorize("hasAnyRole('PHARMACY_OWNER', 'PHARMACIST')")
    public void deleteCreditById(Long id, String jwt) throws Exception {

        Pharmacy pharmacy = utils.checkPharmacyAndUserStatus(jwt);

        Credit credit = getCreditById(id);

        if(!pharmacy.getCredits().contains(credit)) {
            throw new Exception("You don't have the rights to achieve this action");
        }

        pharmacy.getCredits().remove(credit);

        creditRepository.deleteById(id);
    }

    @Override
    @PreAuthorize("hasAnyRole('PHARMACY_OWNER', 'PHARMACIST')")
    public List<CreditDto> searchForCreditsByCIN(String clientId, String jwt) {
        Pharmacy pharmacy = pharmacyService.getPharmacyByUser(jwt);
        List<Credit> credits = creditRepository.searchFroCreditsByCIN(clientId, pharmacy.getId());
        return CreditMapper.toDTOs(credits);
    }

    @Override
    @PreAuthorize("hasAnyRole('PHARMACY_OWNER', 'PHARMACIST')")
    public List<CreditDto> searchForCreditsByName(String name, String jwt) {
        Pharmacy pharmacy = pharmacyService.getPharmacyByUser(jwt);
        List<Credit> credits = creditRepository.searchFroCreditsByName(name, pharmacy.getId());
        return CreditMapper.toDTOs(credits);
    }

    @Override
    @PreAuthorize("hasAnyRole('PHARMACY_OWNER', 'PHARMACIST')")
    public List<CreditDto> getCreditsByStatus(String jwt, CREDIT_STATUS status) throws Exception {
        Pharmacy pharmacy = utils.checkPharmacyAndUserStatus(jwt);

        List<Credit> credits = creditRepository.findByStatusAndPharmacy_id(status, pharmacy.getId());

        return CreditMapper.toDTOs(credits);
    }

    @Override
    public CreditDto getCreditByIdAndJwt(Long id, String jwt) throws Exception {
        Pharmacy pharmacy = pharmacyService.getPharmacyByUser(jwt);
        Credit credit = getCreditById(id);
        if(!pharmacy.getCredits().contains(credit)) {
            throw new Exception("You can't achieve this actions");
        }
        return CreditMapper.toDto(credit);
    }

}
