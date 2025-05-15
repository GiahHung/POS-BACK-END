package com.example.pos.controller.ortherController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.pos.dto.ResponseData;
import com.example.pos.dto.customer.CreateCustomer;
import com.example.pos.dto.customer.UpdateCustomerDTO;
import com.example.pos.model.Customer;
import com.example.pos.service.ortherService.CustomerService;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {
    @Autowired
    CustomerService customerService;

    @PostMapping("/createCustomer")
    public ResponseEntity<ResponseData<Customer>> createCustomer(@RequestBody CreateCustomer createCustomer) {
        ResponseData<Customer> res = customerService.createCustomerService(createCustomer);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/get-customer")
    public ResponseEntity<ResponseData<?>> getEmployee(@RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        ResponseData<?> response = customerService.getListCustomerService(search, sortBy, direction);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('R1')")
    @DeleteMapping("/deleteCustomer")
    public ResponseEntity<ResponseData<Customer>> deleteCustomer(@RequestParam Long id) {
        ResponseData<Customer> response = customerService.deleteCustomerService(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('R1')")
    @PutMapping("/updateCustomer")
    public ResponseEntity<ResponseData<Customer>> updateCustomer(@RequestBody UpdateCustomerDTO dto) {
        ResponseData<Customer> res = customerService.updateCustomerService(dto);
        return ResponseEntity.ok(res);
    }

}
