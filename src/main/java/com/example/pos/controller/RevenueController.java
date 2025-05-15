package com.example.pos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pos.dto.ResponseData;
import com.example.pos.model.RevenueSummary;
import com.example.pos.service.RevenueService;

@RestController
public class RevenueController {
    @Autowired
    RevenueService revenueService;

    @PreAuthorize("hasAuthority('R1')")
    @GetMapping("/api/get-revenue")
    public ResponseEntity<ResponseData<?>> getRevenue() {
        ResponseData<?> res = revenueService.getRevenueService();
        return ResponseEntity.ok(res);
    }

    @PreAuthorize("hasAuthority('R1')")
    @GetMapping("/api/get-all-revenue")
    public ResponseEntity<ResponseData<List<RevenueSummary>>> getAllRevenue() {
        ResponseData<List<RevenueSummary>> res = revenueService.getAllRevenueService();
        return ResponseEntity.ok(res);
    }
}
