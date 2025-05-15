package com.example.pos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.example.pos.model.Voucher;

public interface VoucherRepository extends JpaRepository<Voucher, Long> {
    boolean existsByVoucherCode(String voucherCode);

    Voucher findByVoucherCode(String voucherCode);
}
