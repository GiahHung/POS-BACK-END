package com.example.pos.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pos.model.RevenueSummary;

public interface RevenueSummaryRepository extends JpaRepository<RevenueSummary, Integer>{
     Optional<RevenueSummary> findByReportDate(LocalDate reportDate);
}
