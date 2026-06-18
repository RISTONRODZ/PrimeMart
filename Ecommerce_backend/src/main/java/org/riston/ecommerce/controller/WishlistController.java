package org.riston.ecommerce.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.riston.ecommerce.annotation.ApiNotFoundResponse;
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
@Tag(
        name = "Wishlist Management",
        description = "Endpoints for managing user wishlists and adding/removing products"
)
public class WishlistController {

    private final WishlistService wishlistService;
    private final UserService userService;
    private final ProductService productService;

    @GetMapping
    @Operation(
            summary = "Get user's wishlist",
            description = "Retrieves the complete wishlist for the authenticated user"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Wishlist retrieved successfully"),
    })
    public ResponseEntity<ApiResponseDto<WishlistResponseDto>> getWishlistByUserId(
            @RequestHeader("Authorization") String jwt
    ) {

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
    @Operation(
            summary = "Remove product from wishlist",
            description = "Removes a product from the user's wishlist"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product removed from wishlist successfully"),

    })
    @ApiNotFoundResponse
    public ResponseEntity<ApiResponseDto<WishlistResponseDto>> removeProductFromWishlist(
            @Parameter(description = "Product ID to remove", required = true)
            @PathVariable Long productId,
            @RequestHeader("Authorization") String jwt
    ) throws WishlistNotFoundException {

        Product product = productService.findProductById(productId);
        User user = userService.findUserByJwtToken(jwt);

        Wishlist updatedWishlist = wishlistService.removeProductFromWishList(user, product);

        WishlistResponseDto responseData = WishlistResponseDto.fromEntity(updatedWishlist);
        return ResponseEntity.ok(ApiResponseDto.success("Product removed from wishlist successfully", responseData));
    }
}