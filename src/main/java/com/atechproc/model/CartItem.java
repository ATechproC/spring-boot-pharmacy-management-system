package com.atechproc.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cart-items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "medicine_id", nullable = false)
    private Medicine medicine;

    private int quantity = 1;

    private BigDecimal total = BigDecimal.ZERO;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @Column(nullable = false)
    private BigDecimal profit;

    private LocalDateTime createdAt = LocalDateTime.now();

    public BigDecimal calculateCartItemProfit() {
        return medicine.getProfit().multiply(new BigDecimal(quantity));
    }

    public BigDecimal calculateCartItemTotal() {
        return medicine.getSellingPrice().multiply(new BigDecimal(quantity));
    }
}
