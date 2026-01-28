package com.demo.payment_order_service.infrastructure.adapter.in.rest.controller;

import com.demo.payment_order_service.application.port.in.CreatePaymentOrderUseCase;
import com.demo.payment_order_service.application.port.in.GetPaymentOrderQuery;
import com.demo.payment_order_service.application.port.in.GetPaymentStatusQuery;
import com.demo.payment_order_service.domain.model.Account;
import com.demo.payment_order_service.domain.model.Money;
import com.demo.payment_order_service.domain.model.PaymentOrder;
import com.demo.payment_order_service.domain.model.PaymentStatus;
import com.demo.payment_order_service.infrastructure.adapter.in.rest.dto.InstructedAmount;
import com.demo.payment_order_service.infrastructure.adapter.in.rest.dto.PaymentOrderRequest;
import com.demo.payment_order_service.infrastructure.adapter.in.rest.dto.PaymentOrderStatus;
import com.demo.payment_order_service.infrastructure.adapter.in.rest.mapper.PaymentOrderRestMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentOrderControllerTest {

    @Mock
    private CreatePaymentOrderUseCase createUseCase;

    @Mock
    private GetPaymentOrderQuery getOrderQuery;

    @Mock
    private GetPaymentStatusQuery getStatusQuery;

    // Usamos el mapper real para probar la lógica de transformación también
    private PaymentOrderRestMapper mapper = new PaymentOrderRestMapper();

    private PaymentOrderController controller;

    @BeforeEach
    void setUp() {
        // Inyección manual de dependencias (Constructor Injection)
        controller = new PaymentOrderController(createUseCase, getOrderQuery, getStatusQuery, mapper);
    }

    @Test
    void shouldCreatePaymentOrderSuccessfully() {
        // Given
        PaymentOrderRequest request = new PaymentOrderRequest();
        request.setExternalReference("EXT-001");
        request.setDebtorAccount(new com.demo.payment_order_service.infrastructure.adapter.in.rest.dto.Account().iban("ES123"));
        request.setCreditorAccount(new com.demo.payment_order_service.infrastructure.adapter.in.rest.dto.Account().iban("ES456"));
        request.setInstructedAmount(new InstructedAmount().amount(new BigDecimal("100.50")).currency("EUR"));
        request.setRequestedExecutionDate(LocalDate.now());

        PaymentOrder createdDomainOrder = new PaymentOrder(
                "PO-TEST-123", "EXT-001",
                new Account("ES123"), new Account("ES456"),
                new Money(new BigDecimal("100.50"), "EUR"),
                null, LocalDate.now(),
                PaymentStatus.RECEIVED, LocalDateTime.now()
        );

        when(createUseCase.create(any(PaymentOrder.class))).thenReturn(createdDomainOrder);

        // When
        // Invocamos directamente el método Java del controlador, sin pasar por HTTP
        ResponseEntity<com.demo.payment_order_service.infrastructure.adapter.in.rest.dto.PaymentOrder> response = 
            controller.createPaymentOrder(request);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("PO-TEST-123", response.getBody().getOrderNumber());
        assertEquals("RECEIVED", response.getBody().getStatus().getValue());
    }

    @Test
    void shouldGetPaymentOrderSuccessfully() {
        // Given
        String orderNumber = "PO-TEST-123";
        PaymentOrder domainOrder = new PaymentOrder(
                orderNumber, "EXT-001",
                new Account("ES123"), new Account("ES456"),
                new Money(new BigDecimal("100.50"), "EUR"),
                null, LocalDate.now(),
                PaymentStatus.PENDING, LocalDateTime.now()
        );

        when(getOrderQuery.getByOrderNumber(orderNumber)).thenReturn(Optional.of(domainOrder));

        // When
        ResponseEntity<com.demo.payment_order_service.infrastructure.adapter.in.rest.dto.PaymentOrder> response = 
            controller.getPaymentOrder(orderNumber);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(orderNumber, response.getBody().getOrderNumber());
    }

    @Test
    void shouldReturnNotFoundWhenOrderDoesNotExist() {
        // Given
        String orderNumber = "PO-UNKNOWN";
        when(getOrderQuery.getByOrderNumber(orderNumber)).thenReturn(Optional.empty());

        // When
        ResponseEntity<com.demo.payment_order_service.infrastructure.adapter.in.rest.dto.PaymentOrder> response = 
            controller.getPaymentOrder(orderNumber);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void shouldGetPaymentOrderStatusSuccessfully() {
        // Given
        String orderNumber = "PO-TEST-123";
        PaymentOrder domainOrder = new PaymentOrder(
                orderNumber, "EXT-001",
                new Account("ES123"), new Account("ES456"),
                new Money(new BigDecimal("100.50"), "EUR"),
                null, LocalDate.now(),
                PaymentStatus.COMPLETED, LocalDateTime.now()
        );

        when(getOrderQuery.getByOrderNumber(orderNumber)).thenReturn(Optional.of(domainOrder));

        // When
        ResponseEntity<PaymentOrderStatus> response = controller.getPaymentOrderStatus(orderNumber);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("COMPLETED", response.getBody().getStatus().getValue());
    }
}
