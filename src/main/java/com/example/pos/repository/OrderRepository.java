package com.example.pos.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pos.model.Orders;

public interface OrderRepository extends JpaRepository<Orders,Long>{
   Optional<Orders> findById(Long id);
   
   List<Orders> findByCreatedAtAndStatusId(LocalDateTime createdAt,String statusId);
   
   List<Orders> findByStatusId(String statusId);
}
