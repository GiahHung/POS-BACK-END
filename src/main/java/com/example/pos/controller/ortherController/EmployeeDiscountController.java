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
import com.example.pos.dto.employeeDiscount.CreateDiscountDTO;
import com.example.pos.dto.employeeDiscount.UpdateDiscount;
import com.example.pos.model.EmployeeDiscount;
import com.example.pos.service.ortherService.EmployeeDiscountService;

@RestController
@RequestMapping("/api/discount")
public class EmployeeDiscountController {
    @Autowired
    EmployeeDiscountService employeeDiscountService;

    @PreAuthorize("hasAuthority('R1')")
    @PostMapping("/createDiscount")
    public ResponseEntity<ResponseData<EmployeeDiscount>> createDiscount(@RequestBody CreateDiscountDTO dto) {
        ResponseData<EmployeeDiscount> res = employeeDiscountService.createEmployeeDiscount(dto);
        return ResponseEntity.ok(res);
    }

    @PreAuthorize("hasAuthority('R1')")
    @PutMapping("/updateDiscount")
    public ResponseEntity<ResponseData<EmployeeDiscount>> updateDiscount(@RequestBody UpdateDiscount dto) {
        ResponseData<EmployeeDiscount> res = employeeDiscountService.updateEmployeeDiscountService(dto);
        return ResponseEntity.ok(res);
    }

    @PreAuthorize("hasAuthority('R1')")
    @GetMapping("/get-page-discount")
    public ResponseEntity<ResponseData<?>> getEmployee(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(defaultValue = "") String search) {
        ResponseData<?> response = employeeDiscountService.getListDiscountService(page, size, sortBy, direction,search);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('R1')")
    @DeleteMapping("/deleteDiscount")
    public ResponseEntity<ResponseData<EmployeeDiscount>> deleteDiscount(@RequestParam Long id) {
        ResponseData<EmployeeDiscount> res = employeeDiscountService.deleteEmployeeDiscountService(id);
        return ResponseEntity.ok(res);
    }

}
