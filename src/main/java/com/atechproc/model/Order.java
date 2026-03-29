package com.atechproc.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.*;
import java.math.BigDecimal;
import java.time.temporal.WeekFields;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orders;

    @Column(nullable = false)
    private BigDecimal total;

    @Column(nullable = false)
    private BigDecimal profit;

    @ManyToOne
    @JoinColumn(name = "pharmacy_id")
    private Pharmacy pharmacy;

    @OneToOne
    @JoinColumn(name = "pharmacist_id")
    private User pharmacist;

    private LocalDateTime createdAt = LocalDateTime.now();

    private int day = LocalDate.now().getDayOfMonth();

    private LocalDate date = LocalDate.now();

    private int year = LocalDate.now().getYear();

    @Column(name = "year_month_value")
    private String yearMonth = YearMonth.now().toString();

    private int weekOfMonth =
            LocalDate.now().get(WeekFields.ISO.weekOfMonth());
}