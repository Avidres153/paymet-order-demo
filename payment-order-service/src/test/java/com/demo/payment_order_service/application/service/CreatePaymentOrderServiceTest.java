package com.demo.payment_order_service.application.service;

import com.demo.payment_order_service.application.port.out.LoadPaymentOrderPort;
import com.demo.payment_order_service.application.port.out.SavePaymentOrderPort;
import com.demo.payment_order_service.domain.model.Account;
import com.demo.payment_order_service.domain.model.Money;
import com.demo.payment_order_service.domain.model.PaymentOrder;
import com.demo.payment_order_service.domain.model.PaymentStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreatePaymentOrderServiceTest {

    @Mock
    private SavePaymentOrderPort savePort;

    @Mock
    private LoadPaymentOrderPort loadPort;

    @InjectMocks
    private CreatePaymentOrderService service;

    @Test
    void shouldCreateOrderWhenNotExists() {
        // Given
        PaymentOrder order = createValidOrder();
        when(loadPort.existsByExternalReference(order.getExternalReference())).thenReturn(false);

        // When
        service.create(order);

        // Then
        verify(savePort, times(1)).save(order);
    }

    @Test
    void shouldThrowExceptionWhenOrderAlreadyExists() {
        // Given
        PaymentOrder order = createValidOrder();
        when(loadPort.existsByExternalReference(order.getExternalReference())).thenReturn(true);

        // When & Then
        assertThrows(IllegalStateException.class, () -> service.create(order));
        verify(savePort, never()).save(any());
    }

    private PaymentOrder createValidOrder() {
        return new PaymentOrder(
            "PO-123",
            "EXT-REF-001",
            new Account("ES001"),
            new Account("ES002"),
            new Money(BigDecimal.TEN, "EUR"),
            "Concepto",
            LocalDate.now(),
            PaymentStatus.RECEIVED,
            LocalDateTime.now()
        );
    }
}
