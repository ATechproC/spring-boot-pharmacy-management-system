package com.atechproc.request.taxes;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddNewTaxRequest {

    @NotEmpty(message = "Tax name is required")
    private String name;

    private String description;

    @NotNull(message = "Tax total is required")
    private BigDecimal total;
}
