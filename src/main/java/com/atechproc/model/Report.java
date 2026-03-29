package com.atechproc.model;

import com.atechproc.dto.MedicineDto;
import com.atechproc.dto.OrderDto;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class Report {
    private BigDecimal totalEarnings;
    private BigDecimal totalProfit;
    private Long totalTransactions;
    List<MedicineDto> topSellingMedicines;
    List<OrderDto> orders;
}
