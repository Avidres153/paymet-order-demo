package com.demo.payment_order_service.application.port.out;

import com.demo.payment_order_service.domain.model.PaymentOrder;
import java.util.Optional;

public interface LoadPaymentOrderPort {

    Optional<PaymentOrder> loadByOrderNumber(String orderNumber);

    boolean existsByExternalReference(String externalReference);
}
