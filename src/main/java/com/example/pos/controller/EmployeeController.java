package com.example.pos.controller;

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

import com.example.pos.dto.RefreshTokenRequest;
import com.example.pos.dto.ResponseData;
import com.example.pos.dto.employee.CreateEmployeeDTO;
import com.example.pos.dto.employee.EmployeeOutputDTO;
import com.example.pos.dto.employee.UpdateEmployeeDTO;
import com.example.pos.model.Employee;
import com.example.pos.model.Product;
import com.example.pos.service.EmployeeService;
import com.example.pos.service.RefreshTokenService;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;
    @Autowired
    RefreshTokenService refreshTokenService;

    @PreAuthorize("hasAuthority('R1')")
    @PostMapping("/create-employee")
    public ResponseEntity<ResponseData<Employee>> createEmployee(@RequestBody CreateEmployeeDTO employeeDTO) {
        ResponseData<Employee> response = employeeService.createEmployeeService(employeeDTO);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('R1')")
    @GetMapping("/get-page-employee")
    public ResponseEntity<ResponseData<?>> getEmployee(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(defaultValue = "") String search) {
        ResponseData<?> response = employeeService.getEmployeeService(page, size, sortBy, direction, search);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseData<EmployeeOutputDTO> login(@RequestBody CreateEmployeeDTO employeeDTO) {
        return employeeService.loginService(employeeDTO);
    }

    @PreAuthorize("hasAuthority('R1')")
    @PutMapping("/update-employee")
    public ResponseEntity<ResponseData<Employee>> updateEmployee(@RequestBody UpdateEmployeeDTO dto) {
        ResponseData<Employee> response = employeeService.updateEmployeeService(dto);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('R1')")
    @DeleteMapping("/delete-employee")
    public ResponseEntity<ResponseData<Employee>> deleteEmployee(@RequestParam Long id) {
        ResponseData<Employee> response = employeeService.deleteEmployeeService(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody RefreshTokenRequest request) {
        // Thu hồi (revoke) refresh token
        refreshTokenService.deleteRefreshToken(request.getRefreshToken());
        return ResponseEntity.ok("Đăng xuất thành công");
    }

}
