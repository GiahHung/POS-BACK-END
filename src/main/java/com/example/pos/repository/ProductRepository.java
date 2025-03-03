package com.example.pos.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pos.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findAll(Pageable pageable);

    List<Product> findByCategoryId(String categoryId);

    Page<Product> findByNameContainingIgnoreCase(String search, Pageable pageable);
    
    List<Product> findByNameContaining(String search);
}
