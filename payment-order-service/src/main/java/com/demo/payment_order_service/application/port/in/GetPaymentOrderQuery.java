package com.demo.payment_order_service.application.port.in;

import com.demo.payment_order_service.domain.model.PaymentOrder;
import java.util.Optional;

public interface GetPaymentOrderQuery {
    Optional<PaymentOrder> getByOrderNumber(String orderNumber);
}
