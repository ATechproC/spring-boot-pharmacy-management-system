package com.atechproc.request.credit;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateCreditRequest {

    @NotEmpty(message = "Client CIN is required")
    private String clientId;
    @NotEmpty(message = "Client name is required")
    private String name;

    @NotNull(message = "Credit total amount")
    private BigDecimal totalAmount;

    @NotNull(message = "Credit paid amount")
    private BigDecimal paidAmount;
}
