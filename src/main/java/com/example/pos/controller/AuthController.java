package com.example.pos.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.pos.dto.RefreshTokenRequest;
import com.example.pos.jwt.JwtUtil;
import com.example.pos.model.Employee;
import com.example.pos.repository.EmployeeRepository;
import com.example.pos.service.RefreshTokenService;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final EmployeeRepository employeeRepository; // Giả sử bạn dùng để lấy thông tin người dùng

    public AuthController(JwtUtil jwtUtil, RefreshTokenService refreshTokenService,
            EmployeeRepository employeeRepository) {
        this.jwtUtil = jwtUtil;
        this.refreshTokenService = refreshTokenService;
        this.employeeRepository = employeeRepository;
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {
        System.out.println("==========asasasasasasasasasasasas"+request.getRefreshToken());
        return refreshTokenService.findByToken(request.getRefreshToken())
                .map(refreshToken -> {
                    // Kiểm tra thời gian hiệu lực của refresh token
                    if (refreshTokenService.isRefreshTokenExpired(refreshToken)) {
                        // Token đã hết hạn, xoá token và trả về lỗi
                        refreshTokenService.deleteRefreshToken(refreshToken.getToken());
                        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                .body("Refresh token đã hết hạn. Vui lòng đăng nhập lại.");
                    }
                    // Nếu token hợp lệ, tạo Access Token mới
                    Employee employee = employeeRepository.findEmployeeByUserName(refreshToken.getUsername());
                    if (employee == null) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                .body("User không tồn tại");
                    }
                    String newAccessToken = jwtUtil.generateToken(employee.getUserName(), employee.getRoleId());
                    // (Tùy chọn) Bạn cũng có thể cấp refresh token mới và xoá refresh token cũ
                    return ResponseEntity.ok(Map.of(
                            "accessToken", newAccessToken,
                            "refreshToken", refreshToken.getToken()));
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Refresh token không hợp lệ"));
    }
}