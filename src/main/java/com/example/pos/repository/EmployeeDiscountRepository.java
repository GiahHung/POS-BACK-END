package com.example.pos.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pos.model.Employee;
import com.example.pos.model.EmployeeDiscount;

public interface EmployeeDiscountRepository extends JpaRepository<EmployeeDiscount, Long> {
    EmployeeDiscount findByEmployeeCode(String employeeCode);

    EmployeeDiscount findByName(String name);

    Page<EmployeeDiscount> findAll(Pageable pageable);

    Page<EmployeeDiscount> findByNameContainingIgnoreCase(String search, Pageable pageable);
}
