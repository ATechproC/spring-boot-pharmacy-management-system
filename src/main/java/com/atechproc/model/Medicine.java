package com.atechproc.model;

import com.atechproc.enums.MEDICINE_STATUS;
import com.atechproc.enums.MEDICINE_TYPE;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "medicine")
public class Medicine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM")
    private YearMonth expiredDate;

    @Column(nullable = false)
    private BigDecimal purchasePrice;

    @Column(nullable = false)
    private BigDecimal sellingPrice;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private String batchNumber;

    private MEDICINE_TYPE type = MEDICINE_TYPE.TABLET;

    @ElementCollection
    @OneToMany(mappedBy = "medicine", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    private BigDecimal profit = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private MEDICINE_STATUS status = MEDICINE_STATUS.AVAILABLE;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "pharmacy_id")
    private Pharmacy pharmacy;

    private boolean low = false;

    private int soldQuantity = 0;

    private boolean active = true;

    @Column(nullable = false)
    private boolean inStock;

    public BigDecimal calculateProfit() {
        return sellingPrice.subtract(purchasePrice);
    }

    public MEDICINE_STATUS updateStatus() {
        if(expiredDate != null && expiredDate.isBefore(YearMonth.now())) {
            status = MEDICINE_STATUS.EXPIRED;
        } else status = MEDICINE_STATUS.AVAILABLE;
        return status;
    }

    public boolean updateLowVerification() {
        return quantity <= 5;
    }

    public boolean verifyIsInStock() {
        return quantity != 0;
    }
}
