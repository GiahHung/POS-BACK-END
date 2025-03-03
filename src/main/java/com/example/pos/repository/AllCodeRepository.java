package com.example.pos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pos.model.AllCode;

public interface AllCodeRepository extends JpaRepository<AllCode, Long> {
    List<AllCode> findByType(String type);

    boolean existsByType(String type);
}
