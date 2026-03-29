package com.atechproc.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column(nullable = false)
//    private int quantity;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "pharmacy_id")
    private Pharmacy pharmacy;

    private BigDecimal total = BigDecimal.ZERO;

    private BigDecimal profit = BigDecimal.ZERO;

    private LocalDateTime createdAt = LocalDateTime.now();

    public BigDecimal calculateCartProfit() {
        BigDecimal pro = BigDecimal.ZERO;
        for(CartItem item : items) {
            pro = pro.add(item.getProfit());
        }
        return pro;
    }

    public BigDecimal calculateTotal() {
        BigDecimal tot = BigDecimal.ZERO;
        for(CartItem cartItem : items) {
            tot = tot.add(cartItem.getTotal());
        }
        return tot;
    }
}
