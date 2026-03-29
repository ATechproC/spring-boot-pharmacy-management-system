package com.atechproc.controller;

import com.atechproc.dto.CreditDto;
import com.atechproc.enums.CREDIT_STATUS;
import com.atechproc.model.Credit;
import com.atechproc.request.credit.CreateCreditRequest;
import com.atechproc.response.ApiResponse;
import com.atechproc.service.cart.ICartService;
import com.atechproc.service.credit.ICreditService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/credits")
public class CreditController {

    private final ICreditService creditService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getCreditByIdHandler(
            @PathVariable Long id,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        CreditDto credit = creditService.getCreditByIdAndJwt(id, jwt);
        return ResponseEntity.ok(new ApiResponse("Success", credit));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getCreditsHandler(
            @RequestHeader("Authorization") String jwt
    ) {
        List<CreditDto> credits = creditService.getPharmacyCredits(jwt);
        return ResponseEntity.ok(new ApiResponse("Success", credits));
    }

    @GetMapping("/search/name")
    public ResponseEntity<ApiResponse> searchByNameHandler(
            @RequestParam String name,
            @RequestHeader("Authorization") String jwt
    ) {
        List<CreditDto> credits = creditService.searchForCreditsByName(name, jwt);
        return ResponseEntity.ok(new ApiResponse("Success", credits));
    }

    @GetMapping("/search/cin")
    public ResponseEntity<ApiResponse> searchByCINHandler(
            @RequestParam String cin,
            @RequestHeader("Authorization") String jwt
    ) {
        List<CreditDto> credits = creditService.searchForCreditsByCIN(cin, jwt);
        return ResponseEntity.ok(new ApiResponse("Success", credits));
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createCreditHandler(
            @RequestHeader("Authorization") String jwt,
            @RequestBody CreateCreditRequest request
    ) throws Exception {
        CreditDto credit = creditService.createCredit(request, jwt);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Client Credit created successfully", credit));
    }

    @PutMapping("/update-paid-amount/{id}")
    public ResponseEntity<ApiResponse> updatePaidAmountHandler(
            @RequestHeader("Authorization") String jwt,
            @RequestParam BigDecimal paidAmount,
            @PathVariable Long id
    ) throws Exception {
        CreditDto credit = creditService.updateCreditPaidAmount(id, paidAmount, jwt);
        return ResponseEntity.ok(new ApiResponse("Paid Amound updated successfully", credit));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteCreditHandler(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long id
    ) throws Exception {
        creditService.deleteCreditById(id, jwt);
        return ResponseEntity.ok(new ApiResponse("Credit deleted successfully", null));
    }

    @GetMapping("/status")
    public ResponseEntity<ApiResponse> getCreditsByStatusHandler(
            @RequestHeader("Authorization") String jwt,
            @RequestParam CREDIT_STATUS status
            ) throws Exception {
        List<CreditDto> credits = creditService.getCreditsByStatus(jwt, status);
        return ResponseEntity.ok(new ApiResponse("Success", credits));
    }
}
