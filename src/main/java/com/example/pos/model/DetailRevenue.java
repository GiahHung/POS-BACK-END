package com.example.pos.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "detail_revenue")
public class DetailRevenue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer totalOrder;
    private String paymentId;
    private BigDecimal totalSale;
    private LocalDateTime reportDate;

    // Thiết lập mối quan hệ với revenue_summary
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "revenue_summary_id")
    @JsonBackReference
    private RevenueSummary revenueSummary;

    public DetailRevenue() {
    }

    public DetailRevenue(Integer totalOrder, String paymentId, BigDecimal totalSale, LocalDateTime reportDate) {
        this.totalOrder = totalOrder;
        this.paymentId = paymentId;
        this.totalSale = totalSale;
        this.reportDate = reportDate;
    }

    // Các getter, setter

    public Integer getRevenueId() {
        return id;
    }

    public void setRevenueId(Integer id) {
        this.id = id;
    }

    public Integer getTotalOrder() {
        return totalOrder;
    }

    public void setTotalOrder(Integer totalOrder) {
        this.totalOrder = totalOrder;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public BigDecimal getTotalSale() {
        return totalSale;
    }

    public void setTotalSale(BigDecimal totalSale) {
        this.totalSale = totalSale;
    }

    public LocalDateTime getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDateTime reportDate) {
        this.reportDate = reportDate;
    }

    public RevenueSummary getRevenueSummary() {
        return revenueSummary;
    }

    public void setRevenueSummary(RevenueSummary revenueSummary) {
        this.revenueSummary = revenueSummary;
    }
}