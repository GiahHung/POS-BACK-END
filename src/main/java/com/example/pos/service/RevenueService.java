package com.example.pos.service;

import java.time.LocalDate;
import java.util.List;
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

                return new ResponseData<RevenueSummary>(0, "Success", revenueSummary);
            } else {
                return new ResponseData<RevenueSummary>(1, "Not found", (RevenueSummary) null);
            }
        } catch (Exception e) {
            return new ResponseData<RevenueSummary>(-1, "Error from server: " + e.getMessage(), (RevenueSummary) null);
        }
    }

    public ResponseData<List<RevenueSummary>> getAllRevenueService() {
        try {
            List<RevenueSummary> listRevenue = revenueSummaryRepository.findAll();

            return new ResponseData<List<RevenueSummary>>(0, "Success", listRevenue);
        } catch (Exception e) {
            return new ResponseData<List<RevenueSummary>>(-1, "Error from server: " + e.getMessage(),
                    (List<RevenueSummary>) null);
        }
    }
}
