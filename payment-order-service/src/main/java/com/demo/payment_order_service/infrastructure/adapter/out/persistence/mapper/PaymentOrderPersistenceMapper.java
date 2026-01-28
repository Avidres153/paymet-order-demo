package com.demo.payment_order_service.infrastructure.adapter.out.persistence.mapper;

import com.demo.payment_order_service.domain.model.Account;
import com.demo.payment_order_service.domain.model.Money;
import com.demo.payment_order_service.domain.model.PaymentOrder;
import com.demo.payment_order_service.domain.model.PaymentStatus;
import com.demo.payment_order_service.infrastructure.adapter.out.persistence.entity.PaymentOrderEntity;
import org.springframework.stereotype.Component;

@Component
public class PaymentOrderPersistenceMapper {

    public PaymentOrder toDomain(PaymentOrderEntity entity) {
        if (entity == null) {
            return null;
        }

        return new PaymentOrder(
            entity.getOrderNumber(),
            entity.getExternalReference(),
            new Account(entity.getDebtorIban()),
            new Account(entity.getCreditorIban()),
            new Money(entity.getAmountValue(), entity.getAmountCurrency()),
            entity.getRemittanceInformation(),
            entity.getRequestedExecutionDate(),
            PaymentStatus.valueOf(entity.getStatus()),
            entity.getLastUpdate()
        );
    }

    public PaymentOrderEntity toEntity(PaymentOrder domain) {
        if (domain == null) {
            return null;
        }

        PaymentOrderEntity entity = new PaymentOrderEntity();
        entity.setOrderNumber(domain.getOrderNumber());
        entity.setExternalReference(domain.getExternalReference());
        entity.setDebtorIban(domain.getDebtorAccount().iban());
        entity.setCreditorIban(domain.getCreditorAccount().iban());
        entity.setAmountValue(domain.getInstructedAmount().amount());
        entity.setAmountCurrency(domain.getInstructedAmount().currency());
        entity.setRemittanceInformation(domain.getRemittanceInformation());
        entity.setRequestedExecutionDate(domain.getRequestedExecutionDate());
        entity.setStatus(domain.getStatus().name());
        entity.setLastUpdate(domain.getLastUpdate());
        
        return entity;
    }
}
