package com.example.pos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pos.model.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long>{
    RefreshToken findByToken(String token);
}
