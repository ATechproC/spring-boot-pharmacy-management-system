package com.atechproc.service.pharmacy;

import com.atechproc.dto.PharmacyDto;
import com.atechproc.model.Pharmacy;
import com.atechproc.request.pharmacy.CreatePharmacyRequest;
import com.atechproc.request.pharmacy.UpdatePharmacyRequest;

import java.util.List;

public interface IPharmacyService {
    PharmacyDto createPharmacy(CreatePharmacyRequest request, String jwt) throws Exception;

    // Pharmacy getPharmacyByUser(String jwt);
    Pharmacy getPharmacyById(Long pharmacyId);

    PharmacyDto updatePharmacyByOwner(UpdatePharmacyRequest request, String jwt) throws Exception;

    PharmacyDto updatePharmacyStatus(String jwt) throws Exception;

    List<PharmacyDto> getAllPharmacies();

    Pharmacy getPharmacyByUser(String jwt);
}
