package com.example.pos.component;

import org.springframework.context.ApplicationEvent;
import java.math.BigDecimal;

public class OrderCreatedEvent extends ApplicationEvent {
    private final String paymentId;
    private final BigDecimal totalAmount;

    public OrderCreatedEvent(Object source, String paymentId, BigDecimal totalAmount) {
        super(source);
        this.paymentId = paymentId;
        this.totalAmount = totalAmount;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
}