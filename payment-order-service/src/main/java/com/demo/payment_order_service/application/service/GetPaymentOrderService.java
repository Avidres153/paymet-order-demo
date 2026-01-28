package com.demo.payment_order_service.application.service;

import com.demo.payment_order_service.application.port.in.GetPaymentOrderQuery;
import com.demo.payment_order_service.application.port.in.GetPaymentStatusQuery;
import com.demo.payment_order_service.application.port.out.LoadPaymentOrderPort;
import com.demo.payment_order_service.domain.model.PaymentOrder;
import com.demo.payment_order_service.domain.model.PaymentStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class GetPaymentOrderService implements GetPaymentOrderQuery, GetPaymentStatusQuery {

    private final LoadPaymentOrderPort loadPort;

    public GetPaymentOrderService(LoadPaymentOrderPort loadPort) {
        this.loadPort = loadPort;
    }

    @Override
    public Optional<PaymentOrder> getByOrderNumber(String orderNumber) {
        return loadPort.loadByOrderNumber(orderNumber);
    }

    @Override
    public Optional<PaymentStatus> getStatus(String orderNumber) {
        return loadPort.loadByOrderNumber(orderNumber)
                .map(PaymentOrder::getStatus);
    }
}
