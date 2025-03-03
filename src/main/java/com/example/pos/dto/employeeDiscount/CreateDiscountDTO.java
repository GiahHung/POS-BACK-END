package com.example.pos.dto.employeeDiscount;

public class CreateDiscountDTO {
    private String name;
    private double discountValue;
   
    public double getDiscountValue() {
        return discountValue;
    }
    public void setDiscountValue(double discountValue) {
        this.discountValue = discountValue;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
}
