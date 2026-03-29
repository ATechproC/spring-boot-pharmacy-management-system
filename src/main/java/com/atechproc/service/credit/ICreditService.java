package com.atechproc.service.credit;

import com.atechproc.dto.CreditDto;
import com.atechproc.enums.CREDIT_STATUS;
import com.atechproc.model.Credit;
import com.atechproc.request.credit.CreateCreditRequest;

import java.math.BigDecimal;
import java.util.List;

public interface ICreditService {
    Credit getCreditById(Long id);
    CreditDto createCredit(CreateCreditRequest request, String jwt) throws Exception;
    CreditDto updateCreditPaidAmount(Long id, BigDecimal paidAmount, String jwt) throws Exception;
    List<CreditDto> getPharmacyCredits(String jwt);
    void deleteCreditById(Long id, String jwt) throws Exception;
    List<CreditDto> searchForCreditsByCIN(String clientId, String jwt);
    List<CreditDto> searchForCreditsByName(String name, String jwt);
    List<CreditDto> getCreditsByStatus(String jwt, CREDIT_STATUS status) throws Exception;
    CreditDto getCreditByIdAndJwt(Long id, String jwt) throws Exception;
}
