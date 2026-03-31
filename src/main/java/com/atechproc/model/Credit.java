package com.atechproc.model;

import com.atechproc.enums.CREDIT_STATUS;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "credits")
public class Credit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String clientId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    @Column(nullable = false)
    private BigDecimal paidAmount;

    @Column(nullable = false)
    private BigDecimal remainingAmount;

    @Column(nullable = false)
    private String phone;

    private CREDIT_STATUS status = CREDIT_STATUS.PENDING;

    @ManyToOne
    @JoinColumn(name = "pharmacy_id")
    private Pharmacy pharmacy;

    public BigDecimal calculateRemainingAmount() {
        return totalAmount.subtract(paidAmount);
    }

    public CREDIT_STATUS updateStatus() {
        if(totalAmount.compareTo(paidAmount) == 0) {
            return CREDIT_STATUS.PAID;
        }
        return CREDIT_STATUS.PENDING;
    }
}
