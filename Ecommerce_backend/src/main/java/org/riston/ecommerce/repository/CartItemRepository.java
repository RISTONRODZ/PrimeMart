package org.riston.ecommerce.repository;

import org.riston.ecommerce.model.Cart;
import org.riston.ecommerce.model.CartItem;
import org.riston.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem,Long>  {
    CartItem findByCartAndProductAndSize(Cart cart, Product product, String size);
}
