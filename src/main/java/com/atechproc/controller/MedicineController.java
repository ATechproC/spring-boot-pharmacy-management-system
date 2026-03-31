package com.atechproc.controller;

import com.atechproc.dto.CategoryMedicineDto;
import com.atechproc.dto.MedicineDto;
import com.atechproc.enums.MEDICINE_STATUS;
import com.atechproc.enums.MEDICINE_TYPE;
import com.atechproc.mapper.MedicineMapper;
import com.atechproc.model.Medicine;
import com.atechproc.request.medicine.CreateMedicineRequest;
import com.atechproc.request.medicine.UpdateMedicineRequest;
import com.atechproc.response.ApiResponse;
import com.atechproc.service.medicine.IMedicineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/medicines")
public class MedicineController {

    private final IMedicineService medicineService;

    @PostMapping("/create")
    ResponseEntity<ApiResponse> createMedicineHandler(
            @Valid
            @RequestBody
            CreateMedicineRequest request,
            @RequestParam Long supplierId,
            @RequestParam Long categoryId,
            @RequestHeader("Authorization") String jwt
            ) throws Exception {
        MedicineDto medicine = medicineService.createMedicine(request, supplierId, categoryId, jwt);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Medicine created successfully", medicine));
    }

    @GetMapping("/{id}")
    ResponseEntity<ApiResponse> getMedicineHandler(@PathVariable Long id) {
        Medicine medicine = medicineService.getMedicineById(id);
        return ResponseEntity.ok(
                new ApiResponse("Success", MedicineMapper.toDto(medicine))
        );
    }

    @PutMapping("/update/{id}")
    ResponseEntity<ApiResponse> updateMedicine(
            @Valid
            @RequestBody
            UpdateMedicineRequest request,
            @RequestParam Long supplierId,
            @RequestParam Long categoryId,
            @PathVariable Long id,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        MedicineDto medicine = medicineService.updateMedicine(request, supplierId, categoryId, id, jwt);
        return ResponseEntity.ok(new ApiResponse("Updated successfully", medicine));
    }

    @GetMapping
    ResponseEntity<ApiResponse> getAllPharmacyMedicines(@RequestHeader("Authorization") String jwt) {
        List<MedicineDto> medicines = medicineService.getAllPharmacyMedicines(jwt);
        return ResponseEntity.ok(new ApiResponse("Success", medicines));
    }

    @DeleteMapping("/delete/{id}")
    ResponseEntity<ApiResponse> deleteMedicineById(
            @PathVariable Long id,
            @RequestParam Long categoryId,
            @RequestParam Long supplierId,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        medicineService.deleteMedicine(id, categoryId, supplierId, jwt);
        return ResponseEntity.ok(new ApiResponse("deleted successfully", null));
    }

    @GetMapping("/batch-number/{batchNumber}")
    ResponseEntity<ApiResponse> getMedicinesByBatchNumberHandler(
            @PathVariable String batchNumber,
            @RequestParam String name,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        MedicineDto medicine = medicineService.getMedicineByBatchNumber(batchNumber, name, jwt);
        return ResponseEntity.ok(new ApiResponse("Success", medicine));
    }

    @GetMapping("/search/name")
    ResponseEntity<ApiResponse> searchForMedicineHandler(
            @RequestHeader("Authorization") String jwt,
            @RequestParam String keyword
    ) throws Exception {
        List<MedicineDto> medicines = medicineService.searchForMedicinesByName(keyword, jwt);
        return ResponseEntity.ok(new ApiResponse("Success", medicines));
    }

    @GetMapping("/category/{categoryId}")
    ResponseEntity<ApiResponse> getMedicinesByCategoryHandler(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long categoryId
    ) throws Exception {
        List<MedicineDto> medicines = medicineService.getMedicinesByCategory(categoryId, jwt);
        return ResponseEntity.ok(new ApiResponse("Success", medicines));
    }

    @GetMapping("/supplier/{supplierId}")
    ResponseEntity<ApiResponse> getMedicinesBySupplierHandler(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long supplierId
    ) throws Exception {
        List<MedicineDto> medicines = medicineService.getMedicinesBySupplier(supplierId, jwt);
        return ResponseEntity.ok(new ApiResponse("Success", medicines));
    }

    @PutMapping("/update-status/{id}")
    ResponseEntity<ApiResponse> updateMedicineStatusHandler(
            @PathVariable Long id,
            @RequestParam MEDICINE_STATUS status,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        MedicineDto medicine = medicineService.updateMedicineStatus(id, jwt, status);
        return ResponseEntity.ok(new ApiResponse("Medicine staus updated successfully", medicine));
    }

    @PutMapping("/update-quantity/{id}")
    public ResponseEntity<ApiResponse> updateMedicineQuantityHandler(
            @PathVariable Long id,
            @RequestHeader("Authorization") String jwt,
            @RequestParam int quantity
    ) throws Exception {
        MedicineDto medicine = medicineService.updateMedicineQuantity(id, jwt, quantity);
        return ResponseEntity.ok(new ApiResponse("Medicine quantity updated successfully", medicine));
    }

    @GetMapping("/expired")
    public ResponseEntity<ApiResponse> getExpiredMedicinesHandler(
            @RequestHeader("Authorization") String jwt
    ) {
        List<MedicineDto> medicines = medicineService.getExpiredMedicines(jwt);
        return ResponseEntity.ok(new ApiResponse("Success", medicines));
    }

    @GetMapping("/search/batch-number")
    public ResponseEntity<ApiResponse> searchForMedicinesByBatchNumberHandler(
            @RequestParam String keyword,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        List<MedicineDto> medicines = medicineService.searchForMedicineByBatchNumber(keyword, jwt);
        return ResponseEntity.ok(new ApiResponse("Success", medicines));
    }

    @GetMapping("/type")
    public ResponseEntity<ApiResponse> getMedicinesTyType(
            @RequestParam MEDICINE_TYPE type,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        List<MedicineDto> medicines = medicineService.getMedicinesByType(type, jwt);
        return ResponseEntity.ok(new ApiResponse("Success", medicines));
    }

    @GetMapping("/status")
    public ResponseEntity<ApiResponse> getMedicineByStatus(
            @RequestParam MEDICINE_STATUS status,
            @RequestHeader("Authorization") String jwt
    ) {
        List<MedicineDto> medicines = medicineService.getMedicinesByStatus(status, jwt);
        return ResponseEntity.ok(new ApiResponse("Success", medicines));
    }

    @GetMapping("/available")
    public ResponseEntity<ApiResponse> getAvailableMedicinesHandler(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        List<MedicineDto> medicines = medicineService.getAvailableMedicines(jwt);
        return ResponseEntity.ok(new ApiResponse("Success", medicines));
    }

    @GetMapping("/search/category/{categoryId}")
    public ResponseEntity<ApiResponse> searchFroMedicineByGroupAndName(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long categoryId,
            @RequestParam String keyword
    ) throws Exception {
        List<MedicineDto> medicines = medicineService.searchForMedicinesByGroupAndMedicineName(jwt, categoryId, keyword);
        return ResponseEntity.ok(new ApiResponse("Success", medicines));
    }

    @GetMapping("/search/supplier/{supplierId}")
    public ResponseEntity<ApiResponse> searchFroMedicineBySupplierAndName(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long supplierId,
            @RequestParam String keyword
    ) throws Exception {
        List<MedicineDto> medicines = medicineService.searchForMedicinesBySupplierAndMedicineName(jwt, supplierId, keyword);
        return ResponseEntity.ok(new ApiResponse("Success", medicines));
    }

    @GetMapping("/search/expired")
    public ResponseEntity<ApiResponse> searchForExpiredMedicinesByNameHandler(
            @RequestHeader("Authorization") String jwt,
            @RequestParam String keyword
    ) throws Exception {
        List<MedicineDto> medicines = medicineService.searchForExpiredMedicinesByName(jwt, keyword);
        return ResponseEntity.ok(new ApiResponse("Success", medicines));
    }

    @GetMapping("/search/name-batch-number")
    public ResponseEntity<ApiResponse> searchForMedicineByNameAndBatchNumber(
            @RequestHeader("Authorization") String jwt,
            @RequestParam String name,
            @RequestParam String batchNumber
    ) throws Exception {
        List<MedicineDto> medicines = medicineService.searchForMedicinesByNameAndBatchNumber(jwt, name, batchNumber);
        return ResponseEntity.ok(new ApiResponse("Success", medicines));
    }

    @GetMapping("/inStock")
    public ResponseEntity<ApiResponse> getInStockMedicines(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        List<MedicineDto> medicines = medicineService.getInStockMedicines(jwt);
        return ResponseEntity.ok(new ApiResponse("Success", medicines));
    }

    @GetMapping("/outta-stock")
    public ResponseEntity<ApiResponse> getOuttaStockMedicines(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        List<MedicineDto> medicines = medicineService.getOutStockMedicines(jwt);
        return ResponseEntity.ok(new ApiResponse("Success", medicines));
    }

    @GetMapping("/low")
    public ResponseEntity<ApiResponse> getLowMedicinesHandler(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        List<MedicineDto> medicines = medicineService.getLowStockMedicines(jwt);
        return ResponseEntity.ok(new ApiResponse("Success", medicines));
    }
}
