package com.atechproc.controller;

import com.atechproc.dto.TaxDto;
import com.atechproc.request.taxes.AddNewTaxRequest;
import com.atechproc.request.taxes.UpdateTaxRequest;
import com.atechproc.response.ApiResponse;
import com.atechproc.service.taxes.ITaxesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/taxes")
@RequiredArgsConstructor
public class TaxController {

    private final ITaxesService taxesService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllTaxesHandler(
            @RequestHeader("Authorization") String jwt
    ) {
        List<TaxDto> taxes = taxesService.getTaxes(jwt);
        return ResponseEntity.ok(new ApiResponse("Success", taxes));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addNewTaxHandler(
            @Valid @RequestBody AddNewTaxRequest request,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        TaxDto tax = taxesService.addNewTax(request, jwt);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Tax create successfully", tax));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateTaxDetailsHandler(
        @RequestBody UpdateTaxRequest request,
        @RequestHeader("Authorization") String jwt,
        @PathVariable Long id
    ) throws Exception {
        TaxDto tax = taxesService.updateTax(id, request, jwt);
        return ResponseEntity.ok(new ApiResponse("Success", tax));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteTaxHandler(
            @PathVariable Long id,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        taxesService.deleteTax(id, jwt);
        return ResponseEntity.ok(new ApiResponse("Item deleted successfully", null));
    }

}
