package com.demo.payment_order_service.domain.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class MoneyTest {

    @Test
    void shouldCreateMoneyWithValidValues() {
        Money money = new Money(new BigDecimal("100.50"), "EUR");
        assertEquals(new BigDecimal("100.50"), money.amount());
        assertEquals("EUR", money.currency());
    }

    @Test
    void shouldThrowExceptionWhenAmountIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> 
            new Money(new BigDecimal("-10.00"), "EUR"));
    }

    @Test
    void shouldThrowExceptionWhenAmountIsZero() {
        assertThrows(IllegalArgumentException.class, () -> 
            new Money(BigDecimal.ZERO, "EUR"));
    }

    @Test
    void shouldThrowExceptionWhenCurrencyIsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> 
            new Money(new BigDecimal("10.00"), "EU")); // Too short
        
        assertThrows(IllegalArgumentException.class, () -> 
            new Money(new BigDecimal("10.00"), "EURO")); // Too long
    }
}
