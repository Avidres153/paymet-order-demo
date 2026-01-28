package com.demo.payment_order_service.domain.model;

public record Account(String iban) {
    public Account {
        if (iban == null || iban.isBlank()) {
            throw new IllegalArgumentException("IBAN cannot be empty");
        }
    }
}
