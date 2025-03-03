package com.example.pos.service.ortherService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.pos.dto.ResponseData;
import com.example.pos.dto.customer.CreateCustomer;
import com.example.pos.model.Customer;
import com.example.pos.repository.CustomerRepository;

@Service
public class CustomerService {
    @Autowired
    CustomerRepository customerRepository;

    public ResponseData<Customer> createCustomerService(CreateCustomer dto) {
        try {
            if (dto.getName() == null || dto.getPhoneNumber() == null) {
                return new ResponseData<Customer>(2, "Missing required field", null);
            }
            boolean checkCustomer = customerRepository.existsByPhoneNumber(dto.getPhoneNumber());
            if (checkCustomer == true) {
                return new ResponseData<Customer>(1, "Customer is already exist", null);
            }
            Customer customer = new Customer(0, dto.getName(), dto.getPhoneNumber(), 0);
            Customer saveCustomer = customerRepository.save(customer);
            return new ResponseData<Customer>(0, "Create success", saveCustomer);
        } catch (Exception e) {
            return new ResponseData<Customer>(3, "Error from server" + e.getMessage(), null);
        }

    }

    public ResponseData<Customer> deleteCustomerService(Long id) {
        try {
            if (id == null) {
                return new ResponseData<Customer>(2, "Missing required field", null);
            }
            boolean checkCustomer = customerRepository.existsById(id);
            if (checkCustomer == true) {
                customerRepository.deleteById(id);
                return new ResponseData<>(0, "Delete success", null);
            } else {
                return new ResponseData<>(1, "Customer not found", null);
            }
        } catch (Exception e) {
            return new ResponseData<Customer>(3, "Error from server" + e.getMessage(), null);
        }
    }

    public ResponseData<Page<Customer>> getListCustomerService(int page, int size, String sortBy, String direction) {
        try {
            Sort sort = Sort.by(direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
            Pageable pageable = PageRequest.of(Math.min(page-1, 0), size, sort);
            Page<Customer> employeePage = customerRepository.findAll(pageable);
            return new ResponseData<>(0, "success", employeePage);
        } catch (Exception e) {
            return new ResponseData<>(1, "Error from server" + e.getMessage(), null);
        }
    }
}
