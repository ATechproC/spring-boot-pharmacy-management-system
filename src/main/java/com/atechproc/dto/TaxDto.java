package com.atechproc.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TaxDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal total;
}
