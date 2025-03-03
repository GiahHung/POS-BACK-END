package com.example.pos.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pos.dto.ResponseData;
import com.example.pos.model.RevenueSummary;
import com.example.pos.repository.RevenueSummaryRepository;

@Service
public class RevenueService {
    @Autowired
    RevenueSummaryRepository revenueSummaryRepository;

    public ResponseData<RevenueSummary> getRevenueService() {
        try {
            LocalDate date = LocalDate.now();
            Optional<RevenueSummary> optionalRevenueSummary = revenueSummaryRepository.findByReportDate(date);
            System.out.println(date);
            if (optionalRevenueSummary.isPresent()) {
                RevenueSummary revenueSummary = optionalRevenueSummary.get();
                revenueSummary.getDetailRevenues().size();
                return new ResponseData<>(0, "Success", revenueSummary);
            } else {
                return new ResponseData<>(1, "Not found", null);
            }
        } catch (Exception e) {
            return new ResponseData<>(-1, "Error from server: " + e.getMessage(), null);
        }
    }
}
