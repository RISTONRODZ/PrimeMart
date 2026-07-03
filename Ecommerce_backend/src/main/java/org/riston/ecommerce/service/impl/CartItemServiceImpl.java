package org.riston.ecommerce.service.impl;

import lombok.RequiredArgsConstructor;
import org.riston.ecommerce.exception.ItemNotFoundException;
import org.riston.ecommerce.model.CartItem;
import org.riston.ecommerce.model.User;
import org.riston.ecommerce.repository.CartItemRepository;
import org.riston.ecommerce.service.CartItemService;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;

    @Override
    @Transactional
    public CartItem updateCartItem(Long userId, Long id, CartItem cartItem) throws AccessDeniedException {
        CartItem item = findCartItemById(id);
        User cartItemUser = item.getCart().getUser();

        if (!cartItemUser.getId().equals(userId)) {
            throw new AccessDeniedException("You are not authorized to update this cart item");
        }
        item.setQuantity(cartItem.getQuantity());
        item.setMrpPrice(item.getQuantity() * item.getProduct().getMrpPrice());
        item.setSellingPrice(item.getQuantity() * item.getProduct().getSellingPrice());

        return cartItemRepository.save(item);
    }

    @Override
    @Transactional
    public void removeCartItem(Long userId, Long cartItemId) {
        CartItem item = findCartItemById(cartItemId);
        User cartItemUser = item.getCart().getUser();

        if (!cartItemUser.getId().equals(userId)) {
            throw new AccessDeniedException("You are not authorized to delete this cart item");
        }

        item.getCart().getCartItems().remove(item);

        cartItemRepository.delete(item);
    }

    @Override
    public CartItem findCartItemById(Long id) {
        return cartItemRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("item not found with id: " + id));
    }
}
