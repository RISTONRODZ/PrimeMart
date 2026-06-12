package org.riston.ecommerce.service;

import org.riston.ecommerce.model.Cart;
import org.riston.ecommerce.model.CartItem;
import org.riston.ecommerce.model.Product;
import org.riston.ecommerce.model.User;
public interface CartService {
    CartItem addCartItem(
            User user,
            Product product,
            String size,
            int quantity
    );
    Cart findUserCart(User user);
}
