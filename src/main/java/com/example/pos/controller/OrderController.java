package com.example.pos.controller;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.pos.dto.ResponseData;
import com.example.pos.dto.orders.AddProductToOrderDTO;
import com.example.pos.dto.orders.CreateOrderDTO;
import com.example.pos.dto.orders.OrderOutputDTO;
import com.example.pos.dto.orders.RemoveProductFromOrderDTO;
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
    public ResponseEntity<ResponseData<OrderOutputDTO>> addProductToOrder(@RequestBody AddProductToOrderDTO dto) {
        ResponseData<OrderOutputDTO> response = orderService.addProductToOrder(dto);
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
    public ResponseEntity<ResponseData<List<OrderOutputDTO>>> getAllOrders(
            @RequestParam(required = false) LocalDateTime date) {
        ResponseData<List<OrderOutputDTO>> response = orderService.getAllOrderService(date);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/get-last-orders")
    public ResponseEntity<ResponseData<OrderOutputDTO>> getLastOrders() {
        ResponseData<OrderOutputDTO> response = orderService.getLastOrderService();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/api/set-discount")
    public ResponseEntity<ResponseData<OrderOutputDTO>> discountOrder(@RequestParam String employeeCode, Long orderId) {
        ResponseData<OrderOutputDTO> response = orderService.discountStaffService(employeeCode, orderId);
        return ResponseEntity.ok(response);
    }
    @PutMapping("/api/set-voucher")
    public ResponseEntity<ResponseData<OrderOutputDTO>> discountVoucher(@RequestParam String voucherCode, Long orderId) {
        ResponseData<OrderOutputDTO> response = orderService.discountVoucherService(voucherCode, orderId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/api/remove-product-from-order")
    public ResponseEntity<ResponseData<OrderOutputDTO>> removeProductFromOrder(
            @RequestBody RemoveProductFromOrderDTO dto) {
        ResponseData<OrderOutputDTO> response = orderService.removeProductFromOrder(dto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("api/select-customer")
    public ResponseEntity<ResponseData<OrderOutputDTO>> selectCustomer(@RequestParam Long customerId, Long orderId) {
        ResponseData<OrderOutputDTO> response = orderService.selectCustomerService(customerId, orderId);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("api/delete-customer")
    public ResponseEntity<ResponseData<OrderOutputDTO>> deleteCustomer(@RequestParam Long orderId) {
        ResponseData<OrderOutputDTO> response = orderService.deleteCustomerFromOrder(orderId);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/api/delete-order")
    public ResponseEntity<ResponseData<Orders>> deleteOrder(@RequestParam Long id) {
        ResponseData<Orders> response = orderService.deleteOrderService(id);
        return ResponseEntity.ok(response);
    }
}
