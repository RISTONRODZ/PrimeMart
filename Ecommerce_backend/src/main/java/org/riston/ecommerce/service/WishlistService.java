package org.riston.ecommerce.service;


import org.riston.ecommerce.model.Product;
import org.riston.ecommerce.model.User;
import org.riston.ecommerce.model.Wishlist;

public interface WishlistService {
    Wishlist createWishlist(User user);
    Wishlist getWishlistByUserId(User user);
    Wishlist addProductToWishList(User user, Product product);
}
