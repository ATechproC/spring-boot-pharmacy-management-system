package com.atechproc.request.medicine;

import com.atechproc.enums.MEDICINE_TYPE;
import com.atechproc.request.address.UpdateAddressRequest;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

@Data
public class UpdateMedicineRequest {
    private String name;
    private YearMonth expiredDate;
    private BigDecimal purchasePrice;
    private BigDecimal sellingPrice;
    private int quantity;
    private String batchNumber;
    private MEDICINE_TYPE type;
    private Long categoryId;
    private Long supplierId;
}
