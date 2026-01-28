package com.demo.payment_order_service.infrastructure.adapter.in.rest.controller;

import com.demo.payment_order_service.application.port.in.CreatePaymentOrderUseCase;
import com.demo.payment_order_service.application.port.in.GetPaymentOrderQuery;
import com.demo.payment_order_service.application.port.in.GetPaymentStatusQuery;
import com.demo.payment_order_service.domain.model.Account;
import com.demo.payment_order_service.domain.model.Money;
import com.demo.payment_order_service.domain.model.PaymentOrder;
import com.demo.payment_order_service.domain.model.PaymentStatus;
import com.demo.payment_order_service.infrastructure.adapter.in.rest.api.PaymentOrdersApi;
import com.demo.payment_order_service.infrastructure.adapter.in.rest.dto.PaymentOrderRequest;
import com.demo.payment_order_service.infrastructure.adapter.in.rest.dto.PaymentOrderStatus;
import com.demo.payment_order_service.infrastructure.adapter.in.rest.mapper.PaymentOrderRestMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
public class PaymentOrderController implements PaymentOrdersApi {

    private final CreatePaymentOrderUseCase createUseCase;
    private final GetPaymentOrderQuery getOrderQuery;
    private final GetPaymentStatusQuery getStatusQuery;
    private final PaymentOrderRestMapper mapper;

    public PaymentOrderController(CreatePaymentOrderUseCase createUseCase,
                                  GetPaymentOrderQuery getOrderQuery,
                                  GetPaymentStatusQuery getStatusQuery,
                                  PaymentOrderRestMapper mapper) {
        this.createUseCase = createUseCase;
        this.getOrderQuery = getOrderQuery;
        this.getStatusQuery = getStatusQuery;
        this.mapper = mapper;
    }

    @Override
    public ResponseEntity<com.demo.payment_order_service.infrastructure.adapter.in.rest.dto.PaymentOrder> createPaymentOrder(PaymentOrderRequest request) {
        // 1. Mapeo DTO -> Dominio

        PaymentOrder domainCommand = new PaymentOrder(
                generateShortOrderNumber(),
                request.getExternalReference(),
                new Account(request.getDebtorAccount().getIban()),
                new Account(request.getCreditorAccount().getIban()),
                new Money(request.getInstructedAmount().getAmount(), request.getInstructedAmount().getCurrency()),
                request.getRemittanceInformation(),
                request.getRequestedExecutionDate(),
                PaymentStatus.RECEIVED,
                LocalDateTime.now()
        );

        // 2. Invocación del Caso de Uso
        PaymentOrder createdOrder = createUseCase.create(domainCommand);

        // 3. Mapeo Dominio -> DTO Respuesta
        return new ResponseEntity<>(mapper.toDto(createdOrder), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<com.demo.payment_order_service.infrastructure.adapter.in.rest.dto.PaymentOrder> getPaymentOrder(String orderNumber) {
        return getOrderQuery.getByOrderNumber(orderNumber)
                .map(mapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<PaymentOrderStatus> getPaymentOrderStatus(String orderNumber) {
        return getOrderQuery.getByOrderNumber(orderNumber)
                .map(mapper::toStatusDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    private String generateShortOrderNumber() {
        return "PO-".concat(UUID.randomUUID().toString().substring(0, 4).toUpperCase());
    }
}
