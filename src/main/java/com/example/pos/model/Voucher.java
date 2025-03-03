package com.example.pos.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Voucher {
  @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
