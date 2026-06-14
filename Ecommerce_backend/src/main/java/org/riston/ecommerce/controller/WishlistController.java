package org.riston.ecommerce.controller;

import lombok.RequiredArgsConstructor;
import org.riston.ecommerce.exception.WishlistNotFoundException;
import org.riston.ecommerce.model.Product;
import org.riston.ecommerce.model.User;
import org.riston.ecommerce.model.Wishlist;
import org.riston.ecommerce.service.ProductService;
import org.riston.ecommerce.service.UserService;
import org.riston.ecommerce.service.WishlistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wishlist")
public class WishlistController {
    private final WishlistService wishlistService;
    private final UserService userService;
    private final ProductService productService;
    @GetMapping()
    public ResponseEntity<Wishlist> getWishlistByUserId(
            @RequestHeader("Authorization") String jwt) {

        User user = userService.findUserByJwtToken(jwt);
        Wishlist wishlist = wishlistService.getWishlistByUserId(user);
        return ResponseEntity.ok(wishlist);
    }
    @PostMapping("/add-product/{productId}")
    public ResponseEntity<Wishlist> addProductToWishlist(
            @PathVariable Long productId,
            @RequestHeader("Authorization") String jwt) throws WishlistNotFoundException {

        Product product = productService.findProductById(productId);
        User user = userService.findUserByJwtToken(jwt);
        Wishlist updatedWishlist = wishlistService.addProductToWishList(
                user,
                product
        );
        return ResponseEntity.ok(updatedWishlist);
    }
}
