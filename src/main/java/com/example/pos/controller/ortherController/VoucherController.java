package com.example.pos.controller.ortherController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.pos.dto.ResponseData;
import com.example.pos.dto.voucher.CreateVoucherDTO;
import com.example.pos.dto.voucher.UpdateVoucherDTO;
import com.example.pos.model.Voucher;
import com.example.pos.service.ortherService.VoucherService;

@RestController
public class VoucherController {
    @Autowired
    VoucherService voucherService;

    @GetMapping("/api/get-voucher")
    public ResponseEntity<ResponseData<List<Voucher>>> getVoucher() {
        ResponseData<List<Voucher>> response = voucherService.getListVoucher();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/create-voucher")
    public ResponseEntity<ResponseData<Voucher>> createVoucher(@RequestBody CreateVoucherDTO dto) {
        ResponseData<Voucher> response = voucherService.createVoucher(dto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/api/update-voucher")
    public ResponseEntity<ResponseData<Voucher>> updateVoucher(@RequestBody UpdateVoucherDTO dto) {
        ResponseData<Voucher> response = voucherService.updateVoucher(dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/api/delete-voucher")
    public ResponseEntity<ResponseData<Voucher>> deleteVoucher(@RequestParam Long id) {
        ResponseData<Voucher> response = voucherService.deleteVoucher(id);
        return ResponseEntity.ok(response);
    }
}
