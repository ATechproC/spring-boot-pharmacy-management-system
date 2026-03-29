package com.atechproc.service.Order;

import com.atechproc.dto.OrderDto;
import com.atechproc.dto.OrderItemDto;
import com.atechproc.model.Order;
import com.atechproc.model.OrderItem;

import java.time.*;
import java.util.List;

public interface IOrderService {
    OrderDto createOrder(String jwt) throws Exception;
    OrderDto getOrder(Long orderId, String jwt) throws Exception;
    Order getOrderById(Long id);
    OrderItem getOrderItem(Long id);

    List<Order> getTodayOrders(String jwt) throws Exception;
    List<Order> getThisMonthOrders(String jwt) throws Exception;
    List<Order> getThisYearOrders(String jwt) throws Exception;
    List<Order> getThisWeekOrders(String jwt) throws Exception;

    List<Order> getDayOrders(String jwt, LocalDate date) throws Exception;
    List<Order> getMonthOrders(String jwt, YearMonth yearMonth) throws Exception;
    List<Order> getYearOrders(String jwt, int year) throws Exception;
    List<Order> getWeekOfMonthOrders(String jwt, int weekOfMonth, YearMonth yearMonth) throws Exception;

    List<Order> getAllOrders(String jwt) throws Exception;

    OrderItem getOrderItemByMedicine(Long id);

    List<OrderItemDto> getLastOrderItems(String jwt) throws Exception;
}
