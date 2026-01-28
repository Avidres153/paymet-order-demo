package com.demo.payment_order_service.application.service;

import com.demo.payment_order_service.application.port.in.CreatePaymentOrderUseCase;
import com.demo.payment_order_service.application.port.out.LoadPaymentOrderPort;
import com.demo.payment_order_service.application.port.out.SavePaymentOrderPort;
import com.demo.payment_order_service.domain.model.PaymentOrder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CreatePaymentOrderService implements CreatePaymentOrderUseCase {

    private final SavePaymentOrderPort savePort;
    private final LoadPaymentOrderPort loadPort;

    public CreatePaymentOrderService(SavePaymentOrderPort savePort, LoadPaymentOrderPort loadPort) {
        this.savePort = savePort;
        this.loadPort = loadPort;
    }

    @Override
    public PaymentOrder create(PaymentOrder order) {
        // Validación de idempotencia básica
        if (loadPort.existsByExternalReference(order.getExternalReference())) {
            throw new IllegalStateException("Payment order with this external reference already exists");
        }

        // Persistencia
        savePort.save(order);
        
        return order;
    }
}
