package org.riston.ecommerce.repository;

import org.riston.ecommerce.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart,Long> {
    Cart findByUserId(Long id);
}
