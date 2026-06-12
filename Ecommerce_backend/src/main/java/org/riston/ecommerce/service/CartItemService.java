package org.riston.ecommerce.service;

import org.riston.ecommerce.exception.ItemNotFoundException;
import org.riston.ecommerce.model.CartItem;

public interface CartItemService {
    CartItem updateCartItem(Long userId,Long id,CartItem cartItem) throws ItemNotFoundException;
    void removeCartItem(Long userId,Long cartItemId);
    CartItem findCartItemById(Long id);
}
