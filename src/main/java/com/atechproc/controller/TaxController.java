package com.atechproc.controller;

import com.atechproc.dto.TaxDto;
import com.atechproc.mapper.TaxMapper;
import com.atechproc.model.Tax;
import com.atechproc.request.taxes.AddNewTaxRequest;
import com.atechproc.request.taxes.UpdateTaxRequest;
import com.atechproc.response.ApiResponse;
import com.atechproc.service.taxes.ITaxesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/taxes")
@RequiredArgsConstructor
public class TaxController {

    private final ITaxesService taxesService;

    @GetMapping("/current-month")
    public ResponseEntity<ApiResponse> getCurrentMonthTaxesHandler(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        List<TaxDto> taxes = taxesService.getCurrentMonthTaxes(jwt);
        return ResponseEntity.ok(new ApiResponse("Success", taxes));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getTaxHandler(
            @PathVariable Long id
    ) {
        Tax tax = taxesService.getTaxById(id);
        return ResponseEntity.ok(new ApiResponse("Success", TaxMapper.toDto(tax)));
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

    @DeleteMapping("/delete/current-month")
    public ResponseEntity<ApiResponse> deleteCurrentMonthTaxesHandler(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        taxesService.deleteCurrentMonthTaxes(jwt);
        return ResponseEntity.ok(new ApiResponse("Current month taxes deleted successfully", null));
    }

    @GetMapping("/month")
    public ResponseEntity<ApiResponse> getTaxesByMonthHandler(
            @RequestHeader("Authorization") String jwt,
            @RequestParam YearMonth yearMonth
    ) throws Exception {
        List<TaxDto> taxes = taxesService.getMonthTaxes(jwt, yearMonth);
        return ResponseEntity.ok(new ApiResponse("Success", taxes));
    }

    @GetMapping("/year")
    public ResponseEntity<ApiResponse> getTaxesByYearHandler(
            @RequestHeader("Authorization") String jwt,
            @RequestParam int year
    ) throws Exception {
        List<TaxDto> taxes = taxesService.getYearTaxes(jwt, year);
        return ResponseEntity.ok(new ApiResponse("Success", taxes));
    }
}
