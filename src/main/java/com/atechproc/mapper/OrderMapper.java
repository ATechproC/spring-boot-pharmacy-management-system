package com.atechproc.mapper;

import com.atechproc.dto.MedicineDto;
import com.atechproc.dto.OrderDto;
import com.atechproc.dto.OrderItemDto;
import com.atechproc.model.Order;
import com.atechproc.model.OrderItem;

import java.time.LocalTime;
import java.util.List;

public class OrderMapper {
    public static OrderItemDto toOrderItemDto(OrderItem orderItem) {
        OrderItemDto dto = new OrderItemDto();
        dto.setId(orderItem.getId());
        dto.setQuantity(orderItem.getQuantity());
        dto.setMedicine(MedicineMapper.toDto(orderItem.getMedicine()));
        dto.setTotal(orderItem.getTotal());
        dto.setProfit(orderItem.getProfit());

        return dto;
    }

    public static List<OrderItemDto> toOrderItemDTOs(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(OrderMapper::toOrderItemDto).toList();
    }

    public static OrderDto toOrderDto(Order order) {
        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setOrderItems(toOrderItemDTOs(order.getOrders()));
        dto.setProfit(order.getProfit());
        dto.setTotal(order.getTotal());
        dto.setDate(order.getDate());
        dto.setYearMonth(order.getYearMonth());
        dto.setYear(order.getYear());
        dto.setDay(order.getDay());
        dto.setWeekOfMonth(order.getWeekOfMonth());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setUsername(order.getPharmacist().getUsername());
        return dto;
    }

    public static List<OrderDto> toOrderDTOs(List<Order> orders) {
        return orders.stream()
                .map(OrderMapper::toOrderDto).toList();
    }

}
