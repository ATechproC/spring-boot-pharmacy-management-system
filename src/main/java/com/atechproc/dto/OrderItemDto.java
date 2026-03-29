package com.atechproc.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDto {
    private Long id;
    private MedicineDto medicine;
    private BigDecimal total;
    private int quantity;
    private BigDecimal profit;
}
