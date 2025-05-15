package com.example.pos.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.pos.model.Customer;


public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByPhoneNumberOrName(String phoneNumber, String name);

    boolean existsByPhoneNumber(String phoneNumber);

    List<Customer> findByNameContaining(String search, Sort sort);
}
