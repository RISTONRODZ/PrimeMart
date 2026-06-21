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

        when(cartItemRepository.save(any(CartItem.class))).thenAnswer(invocation ->
            invocation.<CartItem>getArgument(0)
        );

        CartItem result = cartService.addCartItem(user, product, "L", 2);

        assertThat(result).isNotNull();
        assertThat(result.getSize()).isEqualTo("L");
        assertThat(result.getQuantity()).isEqualTo(2);
        assertThat(result.getSellingPrice()).isEqualTo(800);
        assertThat(result.getMrpPrice()).isEqualTo(1000);
        assertThat(result.getCart()).isEqualTo(existingCart);

        assertThat(existingCart.getTotalSellingPrice()).isEqualTo(800);
        assertThat(existingCart.getTotalMrpPrice()).isEqualTo(1000);
        assertThat(existingCart.getDiscount()).isEqualTo(20);
        verify(cartRepository, times(1)).save(existingCart);
    }

    @Test
    @DisplayName("Should return existing item when trying to add an item that is already present in cart")
    void addCartItem_WhenItemAlreadyPresent_ReturnsExistingItem() {
        User user = new User();
        user.setId(101L);

        Product product = new Product();
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setCartItems(new HashSet<>());

        CartItem existingItem = new CartItem();
        existingItem.setId(5L);
        existingItem.setProduct(product);
        existingItem.setSize("L");
        existingItem.setQuantity(2);

        when(cartRepository.findByUserId(101L)).thenReturn(cart);
        when(cartItemRepository.findByCartAndProductAndSize(cart, product, "L")).thenReturn(existingItem);

        CartItem result = cartService.addCartItem(user, product, "L", 2);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(5L);
        verify(cartItemRepository, never()).save(any());
        verify(cartRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should find cart related to user by id and compute total prices")
    void findUserCart_WhenCartExists() {
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
        assertThat(resultCart.getDiscount()).isEqualTo(20);

        verify(cartRepository, times(1)).findByUserId(1L);
        verify(cartRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should create and save a new cart when user cart is not found")
    void findUserCart_WhenCartDoesNotExist_CreatesAndSavesNewCart() {
        User user = new User();
        user.setId(1L);

        when(cartRepository.findByUserId(1L)).thenReturn(null);
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Cart resultCart = cartService.findUserCart(user);

        assertThat(resultCart).isNotNull();
        assertThat(resultCart.getUser()).isEqualTo(user);
        assertThat(resultCart.getTotalMrpPrice()).isEqualTo(0);
        assertThat(resultCart.getTotalSellingPrice()).isEqualTo(0);
        assertThat(resultCart.getTotalItem()).isEqualTo(0);
        assertThat(resultCart.getDiscount()).isEqualTo(0);

        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    @DisplayName("Should set discount percentage to 0 when selling price is greater than or equal to mrp price")
    void recalculateCartTotals_WhenSellingPriceIsGreaterOrEqualToMrp_SetsDiscountToZero() {
        User user = new User();
        user.setId(1L);

        Cart cart = new Cart();
        cart.setUser(user);

        CartItem cartItem = new CartItem();
        cartItem.setMrpPrice(500);
        cartItem.setSellingPrice(600);
        cartItem.setQuantity(1);

        Set<CartItem> items = new HashSet<>();
        items.add(cartItem);
        cart.setCartItems(items);

        when(cartRepository.findByUserId(1L)).thenReturn(cart);

        Cart resultCart = cartService.findUserCart(user);

        assertThat(resultCart.getDiscount()).isEqualTo(0);
    }

    @Test
    @DisplayName("Should set discount percentage to 0 when mrp price is negative or zero")
    void recalculateCartTotals_WhenMrpIsInvalid_SetsDiscountToZero() {
        User user = new User();
        user.setId(1L);

        Cart cart = new Cart();
        cart.setUser(user);

        CartItem cartItem = new CartItem();
        cartItem.setMrpPrice(0);
        cartItem.setSellingPrice(0);
        cartItem.setQuantity(1);

        Set<CartItem> items = new HashSet<>();
        items.add(cartItem);
        cart.setCartItems(items);

        when(cartRepository.findByUserId(1L)).thenReturn(cart);

        Cart resultCart = cartService.findUserCart(user);

        assertThat(resultCart.getDiscount()).isEqualTo(0);
    }
}