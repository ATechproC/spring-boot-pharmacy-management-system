package com.atechproc.controller;

import com.atechproc.dto.OrderDto;
import com.atechproc.dto.OrderItemDto;
import com.atechproc.response.ApiResponse;
import com.atechproc.service.Order.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/orders")
public class OrderController {

    private final IOrderService orderService;

    @PostMapping("/create")
    ResponseEntity<ApiResponse> createOrderHandler(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        OrderDto order = orderService.createOrder(jwt);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Order created successfully", order));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse> getOrderByIdHandler(
            @PathVariable Long orderId,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        OrderDto order = orderService.getOrder(orderId, jwt);
        return ResponseEntity.ok(new ApiResponse("Success", order));
    }

    @GetMapping("/recent-sales")
    public ResponseEntity<ApiResponse> fetchRecentSalesHandler(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        List<OrderItemDto> orderItems = orderService.getLastOrderItems(jwt);
        return ResponseEntity.ok(new ApiResponse("Success", orderItems));
    }
}
