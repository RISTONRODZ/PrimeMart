package org.riston.ecommerce.controller;

import lombok.RequiredArgsConstructor;
import org.riston.ecommerce.exception.WishlistNotFoundException;
import org.riston.ecommerce.model.Product;
import org.riston.ecommerce.model.User;
import org.riston.ecommerce.model.Wishlist;
import org.riston.ecommerce.response.ApiResponseDto;
import org.riston.ecommerce.response.WishlistResponseDto;
import org.riston.ecommerce.service.ProductService;
import org.riston.ecommerce.service.UserService;
import org.riston.ecommerce.service.WishlistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;
    private final UserService userService;
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponseDto<WishlistResponseDto>> getWishlistByUserId(@RequestHeader("Authorization") String jwt) {

        User user = userService.findUserByJwtToken(jwt);
        Wishlist wishlist = wishlistService.getWishlistByUserId(user);

        WishlistResponseDto responseData = WishlistResponseDto.fromEntity(wishlist);
        return ResponseEntity.ok(ApiResponseDto.success("Wishlist retrieved successfully", responseData));
    }

    @PostMapping("/add-product/{productId}")
    public ResponseEntity<ApiResponseDto<WishlistResponseDto>> addProductToWishlist(@PathVariable Long productId, @RequestHeader("Authorization") String jwt) throws WishlistNotFoundException {

        Product product = productService.findProductById(productId);
        User user = userService.findUserByJwtToken(jwt);

        Wishlist updatedWishlist = wishlistService.addProductToWishList(user, product);

        WishlistResponseDto responseData = WishlistResponseDto.fromEntity(updatedWishlist);
        return ResponseEntity.ok(ApiResponseDto.success("Product added to wishlist successfully", responseData));
    }
    @DeleteMapping("/remove-product/{productId}")
    public ResponseEntity<ApiResponseDto<WishlistResponseDto>> removeProductFromWishlist(
            @PathVariable Long productId,
            @RequestHeader("Authorization") String jwt) throws WishlistNotFoundException {

        Product product = productService.findProductById(productId);
        User user = userService.findUserByJwtToken(jwt);

        Wishlist updatedWishlist = wishlistService.removeProductFromWishList(user, product);

        WishlistResponseDto responseData = WishlistResponseDto.fromEntity(updatedWishlist);
        return ResponseEntity.ok(ApiResponseDto.success("Product removed from wishlist successfully", responseData));
    }
}