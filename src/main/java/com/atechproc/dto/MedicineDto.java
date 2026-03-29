package com.atechproc.dto;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Data
public class MedicineDto {
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
    private CategoryInfoDto category;
    private int soldQuantity;
    private boolean low;
    private boolean inStock;
    private boolean active;
}
