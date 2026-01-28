package com.demo.payment_order_service.infrastructure.adapter.out.persistence.repository;

import com.demo.payment_order_service.infrastructure.adapter.out.persistence.entity.PaymentOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PaymentOrderJpaRepository extends JpaRepository<PaymentOrderEntity, Long> {

    Optional<PaymentOrderEntity> findByOrderNumber(String orderNumber);

    boolean existsByExternalReference(String externalReference);
}
