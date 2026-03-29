package com.atechproc.controller;

import com.atechproc.dto.PharmacyDto;
import com.atechproc.mapper.PharmacyMapper;
import com.atechproc.model.Pharmacy;
import com.atechproc.request.pharmacy.CreatePharmacyRequest;
import com.atechproc.request.pharmacy.UpdatePharmacyRequest;
import com.atechproc.response.ApiResponse;
import com.atechproc.service.business.BusinessLogicService;
import com.atechproc.service.pharmacy.PharmacyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/pharmacy")
public class PharmacyController {

    private final PharmacyService pharmacyService;
    private final BusinessLogicService businessLogicService;

    @PostMapping("/create")
    ResponseEntity<ApiResponse> createPharmacyHandler(
            @Valid @RequestBody CreatePharmacyRequest request,
            @RequestHeader("Authorization") String jwt) throws Exception {
        PharmacyDto pharmacy = pharmacyService.createPharmacy(request, jwt);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Pharmacy created successfully", pharmacy));
    }

    @GetMapping("/owner")
    ResponseEntity<ApiResponse> getPharmacyByUserHandler(
            @RequestHeader("Authorization") String jwt) {
        Pharmacy pharmacy = pharmacyService.getPharmacyByUser(jwt);
        return ResponseEntity.ok(new ApiResponse("Success", PharmacyMapper.toDto(pharmacy, businessLogicService)));
    }

    @PutMapping("/update")
    ResponseEntity<ApiResponse> updatePharmacyByOwnerHandler(
            @Valid @RequestBody UpdatePharmacyRequest request,
            @RequestHeader("Authorization") String jwt) throws Exception {
        PharmacyDto pharmacy = pharmacyService.updatePharmacyByOwner(request, jwt);
        return ResponseEntity.ok(new ApiResponse("Pharmacy update with success", pharmacy));
    }

    @PutMapping("/update-status")
    ResponseEntity<ApiResponse> updatePharmacyStatus(
            @RequestHeader("Authorization") String jwt) throws Exception {
        PharmacyDto pharmacy = pharmacyService.updatePharmacyStatus(jwt);
        return ResponseEntity.ok(new ApiResponse("Status updated successfully", pharmacy));
    }

    @GetMapping("/all")
    ResponseEntity<ApiResponse> getAllPharmaciesHandler() {
        List<PharmacyDto> pharmacies = pharmacyService.getAllPharmacies();
        return ResponseEntity.ok(new ApiResponse("Success", pharmacies));
    }
}
