package com.atechproc.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

@Data
public class CategoryMedicineDto {
    private Long id;
    private String name;
    private YearMonth expiredDate;
    private BigDecimal purchasePrice;
    private BigDecimal sellingPrice;
    private int quantity;
    private List<ImageDto> images;
    private BigDecimal profit;
    private String status;
    private String batchNumber;
    private String type;
    private SupplierInfoDto supplier;
    private int soldQuantity;
    private boolean low;
    private boolean inStock;
    private boolean active;
}
