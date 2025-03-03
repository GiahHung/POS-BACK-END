package com.example.pos.dto.product;

import java.math.BigDecimal;

public class ProductOutputDTO {
 private Long id;
    private String name;
    private String Image;
    private Double price;
    private String categoryName; // Sẽ chứa giá trị "coffee" thay vì mã "C1"
    private String categoryId;
    public ProductOutputDTO(Long id, String name, String image, Double price, String categoryName, String categoryId) {
        this.id = id;
        this.name = name;
        this.Image = image;
        this.price = price;
        this.categoryName = categoryName;
        this.categoryId = categoryId;
    }
    public String getCategoryId() {
        return categoryId;
    }
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getImage() {
        return Image;
    }
    public void setImage(String image) {
        Image = image;
    }
    public Double getPrice() {
        return price;
    }
    public void setPrice(Double price) {
        this.price = price;
    }
    public String getCategoryName() {
        return categoryName;
    }
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    // Constructors, Getters, Setters
   
    
}
