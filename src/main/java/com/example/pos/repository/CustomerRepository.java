package com.example.pos.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pos.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByPhoneNumberOrName(String phoneNumber, String name);

    boolean existsByPhoneNumber(String phoneNumber);

    Page<Customer> findAll(Pageable pageable);
}
