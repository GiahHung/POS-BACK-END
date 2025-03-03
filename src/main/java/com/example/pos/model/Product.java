package com.example.pos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;

@Entity

public class Product {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  private String categoryId;
  private double price;
  private String image;

  public Product() {
  }

  public Product(Long id, String name, String categoryId, double price, String image) {
    this.id = id;
    this.name = name;
    this.categoryId = categoryId;
    this.price = price;
    this.image = image;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "categoryId", referencedColumnName = "keyMap", insertable = false, updatable = false)
  private AllCode category;

  public AllCode getCategory() {
    return category;
  }

  public void setCategory(AllCode category) {
    this.category = category;
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

  public String getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(String categoryId) {
    this.categoryId = categoryId;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

}
