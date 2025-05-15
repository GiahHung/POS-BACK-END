package com.example.pos.dto.orders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.example.pos.model.OrderDetail;

public class OrderOutputDTO {
    private Long id;
    private String employeeName;
    private String customerName;
    private double totalAmount;
    private String payment;
    private double discount;
    private String statusId;
    private LocalDateTime date;
    private List<OrderDetail> listOrder;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public OrderOutputDTO(Long id,
            String employeeName,
            String customerName, double totalAmount, String payment,
            double discount, String statusId, List<OrderDetail> listOrder, LocalDateTime date) {
        this.id = id;
        this.employeeName = employeeName;
        this.customerName = customerName;
        this.totalAmount = totalAmount;
        this.payment = payment;
        this.discount = discount;
        this.statusId = statusId;
        this.listOrder = listOrder;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public List<OrderDetail> getListOrder() {
        return listOrder;
    }

    public void setListOrder(List<OrderDetail> listOrder) {
        this.listOrder = listOrder;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

}
