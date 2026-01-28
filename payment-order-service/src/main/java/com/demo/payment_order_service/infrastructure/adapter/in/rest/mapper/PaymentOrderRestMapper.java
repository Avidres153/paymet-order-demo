package com.demo.payment_order_service.infrastructure.adapter.in.rest.mapper;

import com.demo.payment_order_service.domain.model.Account;
import com.demo.payment_order_service.domain.model.Money;
import com.demo.payment_order_service.domain.model.PaymentOrder;
import com.demo.payment_order_service.domain.model.PaymentStatus;
import com.demo.payment_order_service.infrastructure.adapter.in.rest.dto.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Component
public class PaymentOrderRestMapper {

    public PaymentOrder toDomain(PaymentOrderRequest request) {
        if (request == null) return null;

        // Generamos un ID temporal o nulo para el dominio, ya que es una creación.
        // En este diseño, el servicio o el dominio deberían asignar el ID final.
        // Aquí asignamos null al orderNumber porque aún no existe.
        return new PaymentOrder(
                null, // OrderNumber se genera en el servicio/persistencia o aquí si fuera UUID
                request.getExternalReference(),
                new Account(request.getDebtorAccount().getIban()),
                new Account(request.getCreditorAccount().getIban()),
                new Money(request.getInstructedAmount().getAmount(), request.getInstructedAmount().getCurrency()),
                request.getRemittanceInformation(),
                request.getRequestedExecutionDate(),
                PaymentStatus.RECEIVED, // Estado inicial
                LocalDateTime.now()
        );
    }

    public com.demo.payment_order_service.infrastructure.adapter.in.rest.dto.PaymentOrder toDto(PaymentOrder domain) {
        if (domain == null) return null;

        com.demo.payment_order_service.infrastructure.adapter.in.rest.dto.PaymentOrder dto = 
            new com.demo.payment_order_service.infrastructure.adapter.in.rest.dto.PaymentOrder();

        // Campos heredados de Request
        dto.setExternalReference(domain.getExternalReference());
        dto.setDebtorAccount(toDto(domain.getDebtorAccount()));
        dto.setCreditorAccount(toDto(domain.getCreditorAccount()));
        dto.setInstructedAmount(toDto(domain.getInstructedAmount()));
        dto.setRemittanceInformation(domain.getRemittanceInformation());
        dto.setRequestedExecutionDate(domain.getRequestedExecutionDate());

        // Campos propios de la respuesta
        dto.setOrderNumber(domain.getOrderNumber());
        dto.setStatus(toDto(domain.getStatus()));

        return dto;
    }

    public PaymentOrderStatus toStatusDto(PaymentOrder domain) {
        if (domain == null) return null;

        PaymentOrderStatus dto = new PaymentOrderStatus();
        dto.setOrderNumber(domain.getOrderNumber());
        dto.setStatus(toDto(domain.getStatus()));
        dto.setLastUpdate(domain.getLastUpdate().atOffset(ZoneOffset.UTC)); // LocalDateTime -> OffsetDateTime

        return dto;
    }

    // --- Helpers ---

    private com.demo.payment_order_service.infrastructure.adapter.in.rest.dto.Account toDto(Account domain) {
        com.demo.payment_order_service.infrastructure.adapter.in.rest.dto.Account dto = 
            new com.demo.payment_order_service.infrastructure.adapter.in.rest.dto.Account();
        dto.setIban(domain.iban());
        return dto;
    }

    private InstructedAmount toDto(Money domain) {
        InstructedAmount dto = new InstructedAmount();
        dto.setAmount(domain.amount());
        dto.setCurrency(domain.currency());
        return dto;
    }

    private com.demo.payment_order_service.infrastructure.adapter.in.rest.dto.PaymentStatus toDto(PaymentStatus domain) {
        return com.demo.payment_order_service.infrastructure.adapter.in.rest.dto.PaymentStatus.valueOf(domain.name());
    }
}
