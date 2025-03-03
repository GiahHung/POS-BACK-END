package com.example.pos.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pos.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Page<Employee> findAll(Pageable pageable);
    boolean existsByUserName(String userName);
    Employee findEmployeeByUserName(String userName);
    Optional<Employee> findByUserName(String userName);
    Page<Employee> findByNameContainingIgnoreCase(String search, Pageable pageable);
}
