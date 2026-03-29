package com.atechproc.request.taxes;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateTaxRequest {
    private String name;
    private String description;
    private BigDecimal total;
}
