package org.riston.ecommerce.service.impl;

import lombok.RequiredArgsConstructor;
import org.riston.ecommerce.model.Product;
import org.riston.ecommerce.model.User;
import org.riston.ecommerce.model.Wishlist;
import org.riston.ecommerce.repository.WishlistRepository;
import org.riston.ecommerce.service.WishlistService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {
    private final WishlistRepository wishlistRepository;

    @Override
    @Transactional
    public Wishlist createWishlist(User user) {
        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        return wishlistRepository.save(wishlist);
    }

    @Override
    @Transactional
    public Wishlist getWishlistByUserId(User user) {
        Wishlist wishlist = wishlistRepository.findByUserId(user.getId());
        if(wishlist == null){
            wishlist = createWishlist(user);
        }
        return wishlist;
    }

    @Override
    @Transactional
    public Wishlist addProductToWishList(User user, Product product) {
        Wishlist wishlist = getWishlistByUserId(user);
        wishlist.getProducts().add(product);
        return wishlistRepository.save(wishlist);
    }

    @Override
    @Transactional
    public Wishlist removeProductFromWishList(User user, Product product) {
        Wishlist wishlist = getWishlistByUserId(user);
        if (wishlist.getProducts() != null) {
            wishlist.getProducts().remove(product);
        }

        return wishlistRepository.save(wishlist);
    }
}
