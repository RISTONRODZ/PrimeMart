package org.riston.ecommerce.repository;

import org.riston.ecommerce.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Long> {
    Optional<Order> findByOrderId(String orderId);
    List<Order> findByUserId(Long userId);
    List<Order> findBySellerId(Long sellerId);
}
