package org.riston.ecommerce.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
public class CartController {
    private final CartService cartService;
    private final CartItemService cartItemService;
    private final UserService userService;
    private final ProductService productService;
    private final CouponService couponService;

    @GetMapping
    public ResponseEntity<ApiResponseDto<UserCartResponse>> findUserCartHandler(@RequestHeader("Authorization") String jwt) {
        User user = userService.findUserByJwtToken(jwt);
        Cart cart = cartService.findUserCart(user);
        UserCartResponse cartResponse = UserCartResponse.fromEntity(cart);
        return ResponseEntity.ok(ApiResponseDto.success("User cart retrieved success", cartResponse));
    }

    @PutMapping("/add")
    public ResponseEntity<ApiResponseDto<CartItemDto>> addItemToCart(@Valid @RequestBody AddItemRequestDto req, @RequestHeader("Authorization") String jwt) {
        User user = userService.findUserByJwtToken(jwt);
        Product product = productService.findProductById(req.productId());
        CartItem item = cartService.addCartItem(user, product, req.size(), req.quantity());
        CartItemDto dto = new CartItemDto(item.getId(), product.getId(), product.getTitle(), (product.getImages() != null && !product.getImages().isEmpty()) ? product.getImages().get(0) : null, item.getSize(), item.getQuantity(), item.getMrpPrice(), item.getSellingPrice(), product.getSeller() != null ? product.getSeller().getSellerName() : null);
        return ResponseEntity.ok(ApiResponseDto.success("Item added to cart success", dto));
    }

    @DeleteMapping("/item/{cartItemId}")
    public ResponseEntity<ApiResponseDto<String>> deleteCartItemHandler(@PathVariable Long cartItemId, @RequestHeader("Authorization") String jwt) {

        User user = userService.findUserByJwtToken(jwt);

        cartItemService.removeCartItem(user.getId(), cartItemId);

        return ResponseEntity.ok(ApiResponseDto.success("Item removed from cart", null));
    }

    @PutMapping("/item/{cartItemId}")
    public ResponseEntity<ApiResponseDto<CartItemDto>> updateCartItemHandler(@PathVariable Long cartItemId, @Valid @RequestBody UpdateCartItemRequest req, @RequestHeader("Authorization") String jwt) {
        User user = userService.findUserByJwtToken(jwt);

        CartItem cartItemDetails = new CartItem();
        cartItemDetails.setQuantity(req.quantity());
        cartItemDetails.setSize(req.size());

        CartItem updatedCartItem = cartItemService.updateCartItem(user.getId(), cartItemId, cartItemDetails);
        CartItemDto responseData = CartItemDto.fromEntity(updatedCartItem);

        return ResponseEntity.ok(ApiResponseDto.success("Cart item updated successfully", responseData));
    }
    @PostMapping("/apply")
    public ResponseEntity<ApiResponseDto<Cart>> applyCoupon(@RequestParam String apply, @RequestParam String code, @RequestParam double orderValue, @RequestHeader("Authorization") String jwt) {
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
