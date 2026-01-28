package com.demo.payment_order_service.infrastructure.adapter.out.persistence;

import com.demo.payment_order_service.application.port.out.LoadPaymentOrderPort;
import com.demo.payment_order_service.application.port.out.SavePaymentOrderPort;
import com.demo.payment_order_service.domain.model.PaymentOrder;
import com.demo.payment_order_service.infrastructure.adapter.out.persistence.entity.PaymentOrderEntity;
import com.demo.payment_order_service.infrastructure.adapter.out.persistence.mapper.PaymentOrderPersistenceMapper;
import com.demo.payment_order_service.infrastructure.adapter.out.persistence.repository.PaymentOrderJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PaymentOrderPersistenceAdapter implements LoadPaymentOrderPort, SavePaymentOrderPort {

    private final PaymentOrderJpaRepository repository;
    private final PaymentOrderPersistenceMapper mapper;

    public PaymentOrderPersistenceAdapter(PaymentOrderJpaRepository repository,
                                          PaymentOrderPersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Optional<PaymentOrder> loadByOrderNumber(String orderNumber) {
        return repository.findByOrderNumber(orderNumber)
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsByExternalReference(String externalReference) {
        return repository.existsByExternalReference(externalReference);
    }

    @Override
    public void save(PaymentOrder paymentOrder) {
        PaymentOrderEntity entityToSave = mapper.toEntity(paymentOrder);

        // Gestión del ID Técnico:
        // El dominio no conoce el ID de base de datos (Long).
        // Si la orden ya existe por su clave de negocio (orderNumber), recuperamos el ID técnico
        // para que JPA realice un UPDATE en lugar de un INSERT fallido.
        repository.findByOrderNumber(paymentOrder.getOrderNumber())
                .ifPresent(existingEntity -> entityToSave.setId(existingEntity.getId()));

        repository.save(entityToSave);
    }
}
