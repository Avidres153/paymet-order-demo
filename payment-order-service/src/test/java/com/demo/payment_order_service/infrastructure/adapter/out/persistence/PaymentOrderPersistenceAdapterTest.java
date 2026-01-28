package com.demo.payment_order_service.infrastructure.adapter.out.persistence;

import com.demo.payment_order_service.domain.model.Account;
import com.demo.payment_order_service.domain.model.Money;
import com.demo.payment_order_service.domain.model.PaymentOrder;
import com.demo.payment_order_service.domain.model.PaymentStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test") // Usaremos un perfil de test para configurar H2
class PaymentOrderPersistenceAdapterTest {

    @Autowired
    private PaymentOrderPersistenceAdapter adapter;

    @Test
    void shouldSaveAndLoadPaymentOrder() {
        // Given
        PaymentOrder order = new PaymentOrder(
                "PO-INT-001",
                "EXT-INT-001",
                new Account("ES1234567890"),
                new Account("ES0987654321"),
                new Money(new BigDecimal("500.00"), "EUR"),
                "Integration Test Payment",
                LocalDate.now(),
                PaymentStatus.RECEIVED,
                LocalDateTime.now()
        );

        // When
        adapter.save(order);

        // Then
        Optional<PaymentOrder> loadedOrder = adapter.loadByOrderNumber("PO-INT-001");
        assertThat(loadedOrder).isPresent();
        assertThat(loadedOrder.get().getExternalReference()).isEqualTo("EXT-INT-001");
        assertThat(loadedOrder.get().getInstructedAmount().amount()).isEqualByComparingTo(new BigDecimal("500.00"));
    }

    @Test
    void shouldCheckExistenceByExternalReference() {
        // Given
        PaymentOrder order = new PaymentOrder(
                "PO-INT-002",
                "EXT-INT-002",
                new Account("ES123"), new Account("ES456"),
                new Money(BigDecimal.TEN, "EUR"), null, LocalDate.now(),
                PaymentStatus.RECEIVED, LocalDateTime.now()
        );
        adapter.save(order);

        // When & Then
        assertThat(adapter.existsByExternalReference("EXT-INT-002")).isTrue();
        assertThat(adapter.existsByExternalReference("NON-EXISTENT")).isFalse();
    }
}
