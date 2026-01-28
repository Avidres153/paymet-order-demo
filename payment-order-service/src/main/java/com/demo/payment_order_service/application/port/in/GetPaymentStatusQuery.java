package com.demo.payment_order_service.application.port.in;

import com.demo.payment_order_service.domain.model.PaymentStatus;
import java.util.Optional;

public interface GetPaymentStatusQuery {
    Optional<PaymentStatus> getStatus(String orderNumber);
}
