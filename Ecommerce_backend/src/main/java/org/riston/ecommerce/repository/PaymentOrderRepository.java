package org.riston.ecommerce.repository;

import org.riston.ecommerce.model.PaymentOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentOrderRepository extends JpaRepository<PaymentOrder,Long> {
    PaymentOrder findByPaymentLinkId(String paymentId);
}
