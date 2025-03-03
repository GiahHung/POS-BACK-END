package com.example.pos.service;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pos.model.RefreshToken;
import com.example.pos.repository.RefreshTokenRepository;

@Service
public class RefreshTokenService {
    @Autowired
    RefreshTokenRepository refreshTokenRepository;
    // Thời gian hiệu lực của refresh token (ví dụ: 7 ngày)
    private final Duration refreshTokenDuration = Duration.ofDays(7);

    /**
     * Tạo refresh token mới cho username.
     */
    public RefreshToken createRefreshToken(String username) {
        String token = UUID.randomUUID().toString();
        Instant expiryDate = Instant.now().plus(refreshTokenDuration);
        RefreshToken refreshToken = new RefreshToken(token, username, expiryDate);
        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    /**
     * Tìm refresh token theo chuỗi token.
     */
    public Optional<RefreshToken> findByToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token);
        return Optional.ofNullable(refreshToken);
    }

    /**
     * Kiểm tra xem refresh token đã hết hạn hay chưa.
     */
    public boolean isRefreshTokenExpired(RefreshToken refreshToken) {
        return refreshToken.getExpiryDate().isBefore(Instant.now());
    }

    /**
     * Xóa refresh token (ví dụ: khi đăng xuất).
     */
    public void deleteRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token);
        refreshTokenRepository.delete(refreshToken);
    }

}