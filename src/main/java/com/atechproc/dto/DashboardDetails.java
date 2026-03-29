package com.atechproc.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class DashboardDetails {
    private BigDecimal totalEarnings;
    private BigDecimal totalProfit;
    private Long totalTransactions;
    private List<MedicineDto> topSellingMedicines;
}
