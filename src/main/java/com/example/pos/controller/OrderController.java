package com.example.pos.controller;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.pos.dto.ResponseData;
import com.example.pos.dto.orders.AddProductToOrderDTO;
import com.example.pos.dto.orders.CreateOrderDTO;
import com.example.pos.dto.orders.UpdateOrderDTO;
import com.example.pos.model.Orders;
import com.example.pos.service.OrderService;

@RestController
public class OrderController {
    @Autowired
    OrderService orderService;

    @PostMapping("/api/create-order")
    public ResponseEntity<ResponseData<Orders>> createOrder(@RequestBody CreateOrderDTO dto) {
        ResponseData<Orders> response = orderService.createOrderService(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/add-product-to-order")
    public ResponseEntity<ResponseData<Orders>> addProductToOrder(@RequestBody AddProductToOrderDTO dto) {
        ResponseData<Orders> response = orderService.addProductToOrder(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/get-orders")
    public ResponseEntity<ResponseData<?>> getOrders(@RequestParam Long id) {
        ResponseData<?> response = orderService.getOrderService(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/api/complete-order")
    public ResponseEntity<ResponseData<Orders>> completeOrder(@RequestBody UpdateOrderDTO dto) {
        ResponseData<Orders> response = orderService.completeOrderService(dto);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('R1')")
    @GetMapping("/api/get-all-orders")
    public ResponseEntity<ResponseData<List<Orders>>> getAllOrders(@RequestParam(required = false) LocalDateTime date) {
        ResponseData<List<Orders>> response = orderService.getAllOrderService(date);
        return ResponseEntity.ok(response);
    }
}
