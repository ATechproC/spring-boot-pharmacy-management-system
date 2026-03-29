package com.atechproc.request.medicine;

import com.atechproc.enums.MEDICINE_TYPE;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

@Data
public class CreateMedicineRequest {
    @NotNull(message = "Medicine name is required")
    private String name;

    @NotNull(message = "Expired date is required")
    @JsonFormat(pattern = "yyyy-MM")
    private YearMonth expiredDate;

    @NotNull(message = "Purchase price is required")
    @Positive(message = "Purchase price must be greater than 0")
    private BigDecimal purchasePrice;

    @NotNull(message = "Selling price is required")
    @Positive(message = "Selling price must be greater than 0")
    private BigDecimal sellingPrice;

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;

    @NotNull(message = "Batch number is required")
    private String batchNumber;

    @NotNull(message = "Medicine type is required")
    private MEDICINE_TYPE type;
}
