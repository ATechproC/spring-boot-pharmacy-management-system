package com.atechproc.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CartDto {
    private Long id;
    private List<CartItemDto> cartItems;
    private BigDecimal total;
    private BigDecimal profit;
}
