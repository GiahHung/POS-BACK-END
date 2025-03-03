package com.example.pos.service;

import java.util.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.pos.component.OrderCreatedEvent;
import com.example.pos.dto.ResponseData;
import com.example.pos.dto.orders.AddProductToOrderDTO;
import com.example.pos.dto.orders.CreateOrderDTO;
import com.example.pos.dto.orders.UpdateOrderDTO;
import com.example.pos.model.OrderDetail;
import com.example.pos.model.Orders;
import com.example.pos.model.Product;
import com.example.pos.repository.OrderDetailRepository;
import com.example.pos.repository.OrderRepository;
import com.example.pos.repository.ProductRepository;

@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public ResponseData<Orders> createOrderService(CreateOrderDTO dto) {
        try {
            Orders orders = new Orders(null, dto.getEmployeeId(), null, 0, null, 0, "S1");
            Orders saveOrder = orderRepository.save(orders);
            return new ResponseData<Orders>(0, "Create success", saveOrder);
        } catch (Exception e) {
            return new ResponseData<Orders>(0, "error from server" + e.getMessage(), null);
        }
    }

    public ResponseData<Orders> completeOrderService(UpdateOrderDTO dto) {
        try {
            if (dto.getId() == null) {
                return new ResponseData<>(3, "Missing required field", null);
            }
            boolean checkData = orderRepository.existsById(dto.getId());
            if (checkData == true) {
                Orders order = orderRepository.findById(dto.getId()).orElse(null);
                if (order != null) {
                    order.setStatusId("S2");
                    order.setPaymentId(dto.getPaymentId());
                    Orders saveOrder = orderRepository.save(order);
                    eventPublisher.publishEvent(new OrderCreatedEvent(this, dto.getPaymentId(), dto.getTotalAmount()));
                    return new ResponseData<Orders>(0, "Create success", saveOrder);
                }
            }
            return new ResponseData<>(1, "order not found", null);

        } catch (Exception e) {
            return new ResponseData<Orders>(0, "error from server" + e.getMessage(), null);
        }
    }

    @Transactional
    public ResponseData<Orders> addProductToOrder(AddProductToOrderDTO dto) {

        Orders order = orderRepository.findById(dto.getOrderId()).orElse(null);
        if (order == null) {
            return new ResponseData<>(1, "Order not found", null);
        }

        Product product = productRepository.findById(dto.getProductId()).orElse(null);
        if (product == null) {
            return new ResponseData<>(2, "Product not found", null);
        }

        // Kiểm tra sản phẩm đã có trong đơn hàng chưa
        OrderDetail orderDetail = orderDetailRepository.findByOrderIdAndProductId(dto.getOrderId(),
                dto.getProductId());

        if (orderDetail != null) {
            // Nếu đã có, tăng số lượng
            int newQuantity = orderDetail.getQuantity() + 1;
            orderDetail.setQuantity(newQuantity);
            // Cập nhật totalPrice dựa trên số lượng mới và giá của sản phẩm
            orderDetail.setTotalPrice(newQuantity * product.getPrice());
        } else {
            // Nếu chưa có, thêm mới với số lượng = 1
            orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProduct(product);
            orderDetail.setQuantity(1);
            orderDetail.setTotalPrice(product.getPrice());
        }

        // Cập nhật tổng tiền đơn hàng
        order.setTotalAmount(order.getTotalAmount() + product.getPrice());

        // Lưu thay đổi
        orderDetailRepository.save(orderDetail);
        orderRepository.save(order);

        Optional<Orders> optionalOrder = orderRepository.findById(order.getId());
        if (optionalOrder.isPresent()) {
            Orders NewOrder = optionalOrder.get();
            // Nếu quan hệ orderDetails được cấu hình theo LAZY, bạn có thể kích hoạt load
            // dữ liệu bằng cách gọi hàm size()
            order.getOrderDetails().size();
            return new ResponseData<>(0, "Get order success", NewOrder);
        } else {
            return new ResponseData<>(1, "Order not found", null);
        }

    }

    public ResponseData<Orders> getOrderService(Long orderId) {
        try {
            Optional<Orders> optionalOrder = orderRepository.findById(orderId);
            if (optionalOrder.isPresent()) {
                Orders order = optionalOrder.get();
                // Nếu quan hệ orderDetails được cấu hình theo LAZY, bạn có thể kích hoạt load
                // dữ liệu bằng cách gọi hàm size()
                order.getOrderDetails().size();
                return new ResponseData<>(0, "Get order success", order);
            } else {
                return new ResponseData<>(1, "Order not found", null);
            }
        } catch (Exception e) {
            return new ResponseData<>(-1, "Error: " + e.getMessage(), null);
        }
    }

    public ResponseData<List<Orders>> getAllOrderService(LocalDateTime createdAt) {
        try {
            List<Orders> orders;
            if (createdAt != null) {
                orders = orderRepository.findByCreatedAtAndStatusId(createdAt, "S2");
            } else {
                orders = orderRepository.findByStatusId("S2");
            }
            for (Orders order : orders) {
                order.getOrderDetails().size();
                if (order.getPayment() != null) {
                    order.getPayment().getValue();
                }
            }
            return new ResponseData<>(0, "Get all orders success", orders);
        } catch (Exception e) {
            return new ResponseData<>(-1, "Error: " + e.getMessage(), null);
        }
    }

}
