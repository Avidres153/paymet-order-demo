package com.demo.payment_order_service.domain.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    @Test
    void shouldCreateAccountWithValidIban() {
        Account account = new Account("ES1234567890");
        assertEquals("ES1234567890", account.iban());
    }

    @Test
    void shouldThrowExceptionWhenIbanIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new Account(null));
    }

    @Test
    void shouldThrowExceptionWhenIbanIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> new Account(""));
        assertThrows(IllegalArgumentException.class, () -> new Account("   "));
    }
}
