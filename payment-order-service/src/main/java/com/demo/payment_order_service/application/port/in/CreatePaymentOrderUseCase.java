package com.demo.payment_order_service.application.port.in;

import com.demo.payment_order_service.domain.model.PaymentOrder;

public interface CreatePaymentOrderUseCase {
    PaymentOrder create(PaymentOrder command);
}
