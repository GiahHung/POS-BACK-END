package com.example.pos.service;

import java.security.MessageDigest;
import java.util.Base64;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.pos.dto.ResponseData;
import com.example.pos.dto.employee.CreateEmployeeDTO;
import com.example.pos.dto.employee.EmployeeOutputDTO;
import com.example.pos.dto.employee.UpdateEmployeeDTO;
import com.example.pos.jwt.JwtUtil;
import com.example.pos.model.Employee;
import com.example.pos.model.RefreshToken;
import com.example.pos.repository.EmployeeRepository;

@Service
public class EmployeeService implements UserDetailsService {
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private RefreshTokenService refreshTokenService;

    public String hashText(String password) throws Exception {
        String plainText = password;
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashedBytes = digest.digest(plainText.getBytes());
        return Base64.getEncoder().encodeToString(hashedBytes);
    }

    public boolean checkUserName(String userName) {
        boolean checkUserName = employeeRepository.existsByUserName(userName);
        if (checkUserName) {
            return true;
        } else {
            return false;
        }
    }

    public ResponseData<Employee> createEmployeeService(CreateEmployeeDTO dto) {
        try {
            if (dto.getEmail().isEmpty() || dto.getName().isEmpty() || dto.getPassword().isEmpty()
                    || dto.getPhoneNumber().isEmpty() || dto.getUserName().isEmpty() || dto.getRoleId().isEmpty()) {
                return new ResponseData<>(1, "Missing required field", null);
            }
            if (checkUserName(dto.getUserName())) {
                return new ResponseData<>(1, "Username already exists", null);
            }
            String hashedPassword = hashText(dto.getPassword());
            Employee employee = new Employee(null, dto.getUserName(),
                    hashedPassword, dto.getName(), dto.getRoleId(),
                    dto.getPhoneNumber(), dto.getEmail());

            employeeRepository.save(employee);

            return new ResponseData<>(0, "Create employee success", null);
        } catch (Exception e) {
            return new ResponseData<>(1, "Error creating employee" + e.getMessage(), null);
        }
    }

    public ResponseData<EmployeeOutputDTO> loginService(CreateEmployeeDTO dto) {
        try {
            if (dto.getUserName().isEmpty() || dto.getPassword().isEmpty()) {
                return new ResponseData<>(2, "Missing required field", null);
            }
            boolean checkUserName = checkUserName(dto.getUserName());
            if (checkUserName == false) {
                return new ResponseData<>(3, "Username or password wrong", null);
            }
            String hashedPassword = hashText(dto.getPassword());
            Employee employee = employeeRepository.findEmployeeByUserName(dto.getUserName());
            if (!employee.getPassword().equals(hashedPassword)) {
                return new ResponseData<>(4, "Username or password wrong", null);
            } else {
                String token = jwtUtil.generateToken(employee.getUserName(), employee.getRoleId());
                RefreshToken refreshToken = refreshTokenService.createRefreshToken(employee.getUserName());
                EmployeeOutputDTO outputDTO = new EmployeeOutputDTO(employee.getId(), employee.getUserName(),
                        employee.getName(),
                        employee.getRoleId(), employee.getPhoneNumber(), employee.getEmail(), null);
                outputDTO.setToken(token);
                outputDTO.setRefreshToken(refreshToken);
                return new ResponseData<>(0, "Login success", outputDTO);
            }
        } catch (Exception e) {
            return new ResponseData<>(1, "Error from server" + e.getMessage(), null);
        }
    }

    // public ResponseData<Page<Employee>> getEmployeeService(int page, int size,
    // String sortBy, String direction) {
    // Sort sort = Sort.by(direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC
    // : Sort.Direction.ASC, sortBy);
    // Pageable pageable = PageRequest.of(page, size, sort);
    // Page<Employee> employeePage = employeeRepository.findAll(pageable);
    // return new ResponseData<>(0, "success", employeePage);
    // }
    public ResponseData<Page<EmployeeOutputDTO>> getEmployeeService(int page, int size, String sortBy,
            String direction, String search) {
        Sort sort = Sort.by(direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
        Pageable pageable = PageRequest.of(Math.max(page - 1, 0), size, sort);
        Page<Employee> employeePage;
        if (search != null && !search.trim().isEmpty()) {
            employeePage = employeeRepository.findByNameContainingIgnoreCase(search, pageable);
        } else {
            employeePage = employeeRepository.findAll(pageable);
        }

        // Chuyển đổi Employee sang EmployeeDTO
        Page<EmployeeOutputDTO> employeeDTOPage = employeePage.map(employee -> {
            String roleName = null;
            // Nếu đã mapping quan hệ, bạn có thể lấy giá trị role từ
            // employee.getRole().getValue()
            if (employee.getRole() != null) {
                roleName = employee.getRole().getValue();
            }
            return new EmployeeOutputDTO(
                    employee.getId(),
                    employee.getUserName(),
                    employee.getName(),
                    employee.getRoleId(),
                    employee.getPhoneNumber(),
                    employee.getEmail(),
                    roleName);
        });

        return new ResponseData<>(0, "success", employeeDTOPage);
    }

    public ResponseData<Employee> updateEmployeeService(UpdateEmployeeDTO dto) {
        try {
            Boolean checkData = employeeRepository.existsById(dto.getId());
            if (checkData == true) {
                Employee employee = employeeRepository.findById(dto.getId()).orElse(null);
                employee.setName(dto.getName());
                employee.setEmail(dto.getEmail());
                employee.setUserName(dto.getUserName());
                employee.setPhoneNumber(dto.getPhoneNumber());
                employee.setRoleId(dto.getRoleId());
                Employee updatedEmployee = employeeRepository.save(employee);
                return new ResponseData<>(0, "Update employee success", updatedEmployee);
            } else {
                return new ResponseData<>(1, "employee not found", null);
            }
        } catch (Exception e) {
            return new ResponseData<Employee>(0, "error from server" + e.getMessage(), null);
        }
    }

    public ResponseData<Employee> deleteEmployeeService(Long id) {
        try {
            Boolean checkData = employeeRepository.existsById(id);
            if (checkData == true) {
                employeeRepository.deleteById(id);
                return new ResponseData<>(0, "delete employee success", null);
            } else {
                return new ResponseData<>(1, "employee not found", null);
            }
        } catch (Exception e) {
            return new ResponseData<Employee>(0, "error from server" + e.getMessage(), null);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Employee employee = employeeRepository.findByUserName(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userName));
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(employee.getRoleId());
        return new org.springframework.security.core.userdetails.User(employee.getUserName(), employee.getPassword(),
                Collections.singletonList(authority));
    }

}
