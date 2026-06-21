package org.riston.ecommerce.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.riston.ecommerce.model.Cart;
import org.riston.ecommerce.model.CartItem;
import org.riston.ecommerce.model.Product;
import org.riston.ecommerce.model.User;
import org.riston.ecommerce.repository.CartItemRepository;
import org.riston.ecommerce.repository.CartRepository;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    @Test
    @DisplayName("Should successfully calculate prices and add a new item to an empty cart")
    void shouldAddNewCartItemSuccessfully() {
        User user = new User();
        user.setId(101L);

        Product product = new Product();
        product.setMrpPrice(500);
        product.setSellingPrice(400);

        Cart existingCart = new Cart();
        existingCart.setId(1L);
        existingCart.setCartItems(new HashSet<>());

        when(cartRepository.findByUserId(101L)).thenReturn(existingCart);
        when(cartItemRepository.findByCartAndProductAndSize(existingCart, product, "L")).thenReturn(null);

        when(cartItemRepository.save(any(CartItem.class))).thenAnswer(invocation -> {
            CartItem argument = invocation.getArgument(0);
            argument.setMrpPrice(1000);
            argument.setSellingPrice(800);
            argument.setQuantity(2);
            return argument;
        });

        CartItem result = cartService.addCartItem(user, product, "L", 2);

        assertThat(result).isNotNull();
        assertThat(result.getSize()).isEqualTo("L");
        assertThat(result.getQuantity()).isEqualTo(2);
        assertThat(result.getSellingPrice()).isEqualTo(800);
        assertThat(result.getMrpPrice()).isEqualTo(1000);
        assertThat(result.getCart()).isEqualTo(existingCart);

        assertThat(existingCart.getTotalSellingPrice()).isEqualTo(800);
        assertThat(existingCart.getTotalMrpPrice()).isEqualTo(1000);
    }

    @Test
    @DisplayName("Should find cart related to user by id and compute total prices")
    void findUserCart() {
        User user = new User();
        user.setId(1L);

        Cart cart = new Cart();
        cart.setUser(user);

        CartItem cartItem = new CartItem();
        cartItem.setMrpPrice(1000);
        cartItem.setSellingPrice(800);
        cartItem.setQuantity(2);

        Set<CartItem> items = new HashSet<>();
        items.add(cartItem);
        cart.setCartItems(items);

        when(cartRepository.findByUserId(1L)).thenReturn(cart);

        Cart resultCart = cartService.findUserCart(user);

        assertThat(resultCart).isNotNull();
        assertThat(resultCart.getTotalMrpPrice()).isEqualTo(1000);
        assertThat(resultCart.getTotalSellingPrice()).isEqualTo(800);
        assertThat(resultCart.getTotalItem()).isEqualTo(2);

        verify(cartRepository, times(1)).findByUserId(1L);
    }
}