package com.example.pos.service.ortherService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pos.dto.ResponseData;
import com.example.pos.dto.voucher.CreateVoucherDTO;
import com.example.pos.dto.voucher.UpdateVoucherDTO;
import com.example.pos.model.Voucher;
import com.example.pos.repository.VoucherRepository;

@Service
public class VoucherService {
    @Autowired
    private VoucherRepository voucherRepository;

    public ResponseData<Voucher> createVoucher(CreateVoucherDTO dto) {
        try {
            if (dto.getVoucherCode().isEmpty() || dto.getDiscount() <= 0) {
                return new ResponseData<>(2, "Missing required fields", null);
            }

            boolean exists = voucherRepository.existsByVoucherCode(dto.getVoucherCode());
            if (exists) {
                return new ResponseData<>(1, "Voucher code already exists", null);
            }

            Voucher newVoucher = new Voucher(null, dto.getVoucherCode(), dto.getDiscount(), true);
            Voucher savedVoucher = voucherRepository.save(newVoucher);
            return new ResponseData<>(0, "Voucher created successfully", savedVoucher);
        } catch (Exception e) {
            return new ResponseData<>(3, "Error from server: " + e.getMessage(), null);
        }
    }

    public ResponseData<Voucher> updateVoucher(UpdateVoucherDTO dto) {
        try {
            if (dto.getId() == null || dto.getVoucherCode().isEmpty() || dto.getDiscount() <= 0) {
                return new ResponseData<>(2, "Missing required fields", null);
            }

            Voucher existingVoucher = voucherRepository.findById(dto.getId()).orElse(null);
            if (existingVoucher == null) {
                return new ResponseData<>(1, "Voucher not found", null);
            }
            existingVoucher.setVoucherCode(dto.getVoucherCode());
            existingVoucher.setDiscount(dto.getDiscount());
            existingVoucher.setIsActive(dto.getIsActive());

            Voucher updatedVoucher = voucherRepository.save(existingVoucher);
            return new ResponseData<>(0, "Voucher updated successfully", updatedVoucher);
        } catch (Exception e) {
            return new ResponseData<>(3, "Error from server: " + e.getMessage(), null);
        }
    }


    public ResponseData<List<Voucher>> getListVoucher() {
        try {
            List<Voucher> vouchers = voucherRepository.findAll();
            return new ResponseData<>(0, "Success", vouchers);
        } catch (Exception e) {
            return new ResponseData<>(3, "Error from server: " + e.getMessage(), null);
        }
    }

    public ResponseData<Voucher> deleteVoucher(Long id) {
        try {
            if (id == null) {
                return new ResponseData<>(2, "Missing required field", null);
            }

            boolean exists = voucherRepository.existsById(id);
            if (!exists) {
                return new ResponseData<>(1, "Voucher not found", null);
            }
            voucherRepository.deleteById(id);
            return new ResponseData<>(0, "Voucher deleted successfully", null);
        } catch (Exception e) {
            return new ResponseData<>(3, "Error from server: " + e.getMessage(), null);
        }
    }
}
