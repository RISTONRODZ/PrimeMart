package org.riston.ecommerce.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.riston.ecommerce.annotation.ApiNotFoundResponse;
import org.riston.ecommerce.model.Cart;
import org.riston.ecommerce.model.CartItem;
import org.riston.ecommerce.model.Product;
import org.riston.ecommerce.model.User;
import org.riston.ecommerce.request.AddItemRequestDto;
import org.riston.ecommerce.response.ApiResponseDto;
import org.riston.ecommerce.response.CartItemDto;
import org.riston.ecommerce.response.UpdateCartItemRequest;
import org.riston.ecommerce.response.UserCartResponse;
import org.riston.ecommerce.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cart")
@Tag(
        name = "Shopping Cart",
        description = "Endpoints for managing shopping cart, items, and coupon application"
)
public class CartController {
    private final CartService cartService;
    private final CartItemService cartItemService;
    private final UserService userService;
    private final ProductService productService;
    private final CouponService couponService;

    @GetMapping
    @Operation(
            summary = "Retrieve user's cart",
            description = "Fetches the complete shopping cart for the authenticated user including all items and applicable coupons"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart retrieved successfully"),
    })
    public ResponseEntity<ApiResponseDto<UserCartResponse>> findUserCartHandler(
            @RequestHeader("Authorization") String jwt
    ) {
        User user = userService.findUserByJwtToken(jwt);
        Cart cart = cartService.findUserCart(user);
        UserCartResponse cartResponse = UserCartResponse.fromEntity(cart);
        return ResponseEntity.ok(ApiResponseDto.success("User cart retrieved success", cartResponse));
    }

    @PutMapping("/add")
    @Operation(
            summary = "Add item to cart",
            description = "Adds a product to the user's shopping cart with specified size and quantity"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item added to cart successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request or product not found"),
    })
    public ResponseEntity<ApiResponseDto<CartItemDto>> addItemToCart(
            @Valid @RequestBody AddItemRequestDto req,
            @RequestHeader("Authorization") String jwt
    ) {
        User user = userService.findUserByJwtToken(jwt);
        Product product = productService.findProductById(req.productId());
        CartItem item = cartService.addCartItem(user, product, req.size(), req.quantity());
        CartItemDto dto = new CartItemDto(item.getId(), product.getId(), product.getTitle(), (product.getImages() != null && !product.getImages().isEmpty()) ? product.getImages().get(0) : null, item.getSize(), item.getQuantity(), item.getMrpPrice(), item.getSellingPrice(), product.getSeller() != null ? product.getSeller().getSellerName() : null);
        return ResponseEntity.ok(ApiResponseDto.success("Item added to cart success", dto));
    }

    @DeleteMapping("/item/{cartItemId}")
    @Operation(
            summary = "Remove item from cart",
            description = "Deletes a specific item from the user's shopping cart"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item removed from cart successfully"),
    })
    @ApiNotFoundResponse
    public ResponseEntity<ApiResponseDto<String>> deleteCartItemHandler(
            @Parameter(description = "ID of the cart item to remove", required = true)
            @PathVariable Long cartItemId,
            @RequestHeader("Authorization") String jwt
    ) {

        User user = userService.findUserByJwtToken(jwt);

        cartItemService.removeCartItem(user.getId(), cartItemId);

        return ResponseEntity.ok(ApiResponseDto.success("Item removed from cart", null));
    }

    @PutMapping("/item/{cartItemId}")
    @Operation(
            summary = "Update cart item",
            description = "Updates the quantity and/or size of a cart item"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart item updated successfully"),
    })
    @ApiNotFoundResponse
    public ResponseEntity<ApiResponseDto<CartItemDto>> updateCartItemHandler(
            @Parameter(description = "ID of the cart item to update", required = true)
            @PathVariable Long cartItemId,
            @Valid @RequestBody UpdateCartItemRequest req,
            @RequestHeader("Authorization") String jwt
    ) {
        User user = userService.findUserByJwtToken(jwt);

        CartItem cartItemDetails = new CartItem();
        cartItemDetails.setQuantity(req.quantity());
        cartItemDetails.setSize(req.size());

        CartItem updatedCartItem = cartItemService.updateCartItem(user.getId(), cartItemId, cartItemDetails);
        CartItemDto responseData = CartItemDto.fromEntity(updatedCartItem);

        return ResponseEntity.ok(ApiResponseDto.success("Cart item updated successfully", responseData));
    }

    @PostMapping("/apply")
    @Operation(
            summary = "Apply or remove coupon",
            description = "Applies a discount coupon to the cart or removes it"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Coupon processed successfully"),
    })
    public ResponseEntity<ApiResponseDto<Cart>> applyCoupon(
            @Parameter(description = "Whether to apply ('true') or remove ('false') the coupon", required = true)
            @RequestParam String apply,
            @Parameter(description = "Coupon code to apply", required = true)
            @RequestParam String code,
            @Parameter(description = "Current order value for coupon validation", required = true)
            @RequestParam double orderValue,
            @RequestHeader("Authorization") String jwt
    ) {
        User user = userService.findUserByJwtToken(jwt);
        Cart cart;

        if (apply.equals("true")) {
            cart = couponService.applyCoupon(code, orderValue, user);
        } else {
            cart = couponService.removeCoupon(code, user);
        }

        return ResponseEntity.ok(ApiResponseDto.success("Coupon processed successfully", cart));
    }

}
