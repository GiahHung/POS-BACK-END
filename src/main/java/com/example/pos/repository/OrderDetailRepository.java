package com.example.pos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pos.model.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail,Long>{
OrderDetail findByOrderIdAndProductId(Long orderId, Long productId);
}
