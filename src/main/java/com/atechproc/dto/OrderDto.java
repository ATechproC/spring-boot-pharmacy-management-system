package com.atechproc.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
public class OrderDto {
    private Long id;
    private List<OrderItemDto> orderItems;
    private BigDecimal total;
    private BigDecimal profit;
    private LocalDateTime createdAt;
    private LocalDate date;
    private int day;
    private String yearMonth;
    private int year;
    private int weekOfMonth;
    private String username;
}
