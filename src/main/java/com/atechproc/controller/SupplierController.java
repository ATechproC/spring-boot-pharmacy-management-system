package com.atechproc.controller;

import com.atechproc.dto.SupplierDto;
import com.atechproc.mapper.SupplierMapper;
import com.atechproc.model.Supplier;
import com.atechproc.request.supplier.CreateSupplierRequest;
import com.atechproc.request.supplier.UpdateSupplierRequest;
import com.atechproc.response.ApiResponse;
import com.atechproc.service.business.IBusinessLogicService;
import com.atechproc.service.medicine.IMedicineService;
import com.atechproc.service.supplier.ISupplierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/suppliers")
public class SupplierController {

    private final ISupplierService supplierService;
    private final IBusinessLogicService businessLogicService;

    @PostMapping("/create")
    ResponseEntity<ApiResponse> createSupplierHandler(
            @RequestHeader("Authorization") String jwt,
            @Valid
            @RequestBody
            CreateSupplierRequest request
    ) throws Exception {
        SupplierDto supplier = supplierService.createSupplier(request, jwt);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Supplier added successfully", supplier));
    }

    @PutMapping("/update/{id}")
    ResponseEntity<ApiResponse> updateSupplierHandler(
            @RequestHeader("Authorization") String jwt,
            @Valid
            @RequestBody
            UpdateSupplierRequest request,
            @PathVariable Long id
    ) throws Exception {
        SupplierDto supplier = supplierService.updateSupplier(request, id, jwt);
        return ResponseEntity.ok(new ApiResponse("Supplier name update successfully", supplier));
    }

    @GetMapping
    ResponseEntity<ApiResponse> getAllSupplier(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        List<SupplierDto> suppliers = supplierService.getAllSuppliers(jwt);
        return ResponseEntity.ok(new ApiResponse("Success", suppliers));
    }

    @GetMapping("/{id}")
    ResponseEntity<ApiResponse> getSupplierByIdHandler(@PathVariable Long id) {
        Supplier supplier = supplierService.getSupplierById(id);
        return ResponseEntity.ok(new ApiResponse("Success", SupplierMapper.toDto(supplier, businessLogicService)));
    }

    @DeleteMapping("/delete/{id}")
    ResponseEntity<ApiResponse> deleteSupplierHandler(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long id
    ) throws Exception {
        supplierService.deleteSupplier(id, jwt);
        return ResponseEntity.ok(new ApiResponse("deleted successfully", null));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse> searchForSupplierHandler(
            @RequestParam String keyword,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        List<SupplierDto> suppliers = supplierService.searchForSupplier(keyword, jwt);
        return ResponseEntity.ok(new ApiResponse("Success", suppliers));
    }
}
