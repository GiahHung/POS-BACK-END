package com.example.pos.service.ortherService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.pos.dto.ResponseData;
import com.example.pos.dto.customer.CreateCustomer;
import com.example.pos.dto.customer.UpdateCustomerDTO;
import com.example.pos.model.Customer;
import com.example.pos.repository.CustomerRepository;

@Service
public class CustomerService {
    @Autowired
    CustomerRepository customerRepository;

    public ResponseData<Customer> createCustomerService(CreateCustomer dto) {
        try {
            if (dto.getName().isEmpty() || dto.getPhoneNumber().isEmpty()) {
                return new ResponseData<Customer>(2, "Missing required field", (Customer) null);
            }
            boolean checkCustomer = customerRepository.existsByPhoneNumber(dto.getPhoneNumber());
            if (checkCustomer == true) {
                return new ResponseData<Customer>(1, "Customer is already exist", (Customer) null);
            }
            Customer customer = new Customer(0, dto.getName(), dto.getPhoneNumber(), 0);
            Customer saveCustomer = customerRepository.save(customer);
            return new ResponseData<Customer>(0, "Create success", saveCustomer);
        } catch (Exception e) {
            return new ResponseData<Customer>(3, "Error from server" + e.getMessage(), (Customer) null);
        }

    }

    public ResponseData<Customer> deleteCustomerService(Long id) {
        try {
            if (id == null) {
                return new ResponseData<Customer>(2, "Missing required field", (Customer) null);
            }
            boolean checkCustomer = customerRepository.existsById(id);
            if (checkCustomer == true) {
                customerRepository.deleteById(id);
                return new ResponseData<Customer>(0, "Delete success", (Customer) null);
            } else {
                return new ResponseData<Customer>(1, "Customer not found", (Customer) null);
            }
        } catch (Exception e) {
            return new ResponseData<Customer>(3, "Error from server" + e.getMessage(), (Customer) null);
        }
    }

    public ResponseData<Customer> updateCustomerService( UpdateCustomerDTO dto) {
        try {
            if (dto.getId() == null || dto.getName().isEmpty() || dto.getPhoneNumber().isEmpty()) {
                return new ResponseData<>(2, "Missing required field", null);
            }

            Customer existingCustomer = customerRepository.findById(dto.getId()).orElse(null);
            if (existingCustomer == null) {
                return new ResponseData<>(1, "Customer not found", null);
            }

            boolean checkPhoneExists = customerRepository.existsByPhoneNumber(dto.getPhoneNumber());
            if (checkPhoneExists && !existingCustomer.getPhoneNumber().equals(dto.getPhoneNumber())) {
                return new ResponseData<>(1, "Phone number already exists", null);
            }

            existingCustomer.setName(dto.getName());
            existingCustomer.setPhoneNumber(dto.getPhoneNumber());

            Customer updatedCustomer = customerRepository.save(existingCustomer);
            return new ResponseData<>(0, "Update success", updatedCustomer);
        } catch (Exception e) {
            return new ResponseData<>(3, "Error from server: " + e.getMessage(), null);
        }
    }

    public ResponseData<List<Customer>> getListCustomerService(String search, String sortBy, String direction) {
        try {
            Sort sort = Sort.by(direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
            List<Customer> listCustomers = customerRepository.findByNameContaining(search, sort);
            return new ResponseData<List<Customer>>(0, "success", listCustomers);
        } catch (Exception e) {
            return new ResponseData<List<Customer>>(1, "Error from server" + e.getMessage(), (List<Customer>) null);
        }
    }
}
