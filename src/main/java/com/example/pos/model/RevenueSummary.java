package com.example.pos.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "revenue_summary")
public class RevenueSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer totalOrder;
    private BigDecimal totalRevenue;

    // Thêm trường lưu ngày báo cáo
    private LocalDate reportDate;

    // Quan hệ 1-nhiều với DetailRevenue
    @OneToMany(mappedBy = "revenueSummary", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<DetailRevenue> detailRevenues = new ArrayList<>();

    public RevenueSummary() {
    }

    public RevenueSummary(Integer totalOrder, BigDecimal totalRevenue, LocalDate reportDate) {
        this.totalOrder = totalOrder;
        this.totalRevenue = totalRevenue;
        this.reportDate = reportDate;
    }

    // Getters & Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTotalOrder() {
        return totalOrder;
    }

    public void setTotalOrder(Integer totalOrder) {
        this.totalOrder = totalOrder;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public List<DetailRevenue> getDetailRevenues() {
        return detailRevenues;
    }

    public void setDetailRevenues(List<DetailRevenue> detailRevenues) {
        this.detailRevenues = detailRevenues;
    }

    // Phương thức tiện ích để thêm chi tiết doanh thu
    public void addDetailRevenue(DetailRevenue detailRevenue) {
        detailRevenues.add(detailRevenue);
        detailRevenue.setRevenueSummary(this);
    }
}
