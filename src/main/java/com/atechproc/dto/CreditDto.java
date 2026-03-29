package com.atechproc.dto;

import com.atechproc.enums.CREDIT_STATUS;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreditDto {
    private Long id;
    private String clientId;
    private String name;
    private BigDecimal totalAmount;
    private BigDecimal paidAmount;
    private BigDecimal remainingAmount;
    private CREDIT_STATUS status;
}
