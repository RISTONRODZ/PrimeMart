package org.riston.ecommerce.service.impl;

import lombok.RequiredArgsConstructor;
import org.riston.ecommerce.model.Cart;
import org.riston.ecommerce.model.CartItem;
import org.riston.ecommerce.model.Product;
import org.riston.ecommerce.model.User;
import org.riston.ecommerce.repository.CartItemRepository;
import org.riston.ecommerce.repository.CartRepository;
import org.riston.ecommerce.service.CartService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public CartItem addCartItem(User user, Product product, String size, int quantity) {
        Cart cart = findUserCart(user);
        CartItem isPresent = cartItemRepository.findByCartAndProductAndSize(cart, product, size);
        if(isPresent == null){
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setUserId(user.getId());
            cartItem.setSize(size);
            int totalPrice = quantity * product.getSellingPrice();
            cartItem.setSellingPrice(totalPrice);
            cart.getCartItems().add(cartItem);
            cartItem.setMrpPrice(quantity * product.getMrpPrice());
            cartItem.setCart(cart);
            CartItem savedItem = cartItemRepository.save(cartItem);
            cart.getCartItems().add(savedItem);
            findUserCart(user);
            cartItemRepository.save(cartItem);
            return savedItem;
        }
        return isPresent;
    }

    @Override
    public Cart findUserCart(User user) {

        Cart cart = cartRepository.findByUserId(user.getId());
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cart = cartRepository.save(cart);
        }
        int totalPrice = 0;
        int totalDiscountedPrice = 0;
        int totalItem = 0;
        for (CartItem cartItem : cart.getCartItems()) {
            totalPrice += cartItem.getMrpPrice();
            totalDiscountedPrice += cartItem.getSellingPrice();
            totalItem += cartItem.getQuantity();
        }
        cart.setTotalMrpPrice(totalPrice);
        cart.setTotalItem(totalItem);
        cart.setTotalSellingPrice(totalDiscountedPrice);
        cart.setDiscount(calculateDiscountPercentage(totalPrice, totalDiscountedPrice));
        cart.setTotalItem(totalItem);
        return cart;
    }

    private int calculateDiscountPercentage(Integer mrpPrice, Integer sellingPrice) {
        if (mrpPrice == null || mrpPrice <= 0 || sellingPrice == null) {
            return 0;
        }
        if (sellingPrice >= mrpPrice) {
            return 0;
        }
        double discount = mrpPrice - sellingPrice;
        double discountPercentage = (discount / mrpPrice) * 100;
        return (int) discountPercentage;
    }
}
