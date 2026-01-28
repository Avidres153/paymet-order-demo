package com.demo.payment_order_service.application.port.out;

import com.demo.payment_order_service.domain.model.PaymentOrder;

public interface SavePaymentOrderPort {

    void save(PaymentOrder paymentOrder);
}
