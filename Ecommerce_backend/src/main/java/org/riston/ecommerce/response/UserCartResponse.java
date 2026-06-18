package org.riston.ecommerce.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.riston.ecommerce.model.Cart;
import java.util.List;

@Schema(description = "Response body containing user cart details")
public record UserCartResponse(
        @Schema(description = "Unique identifier of the cart", example = "10")
        Long id,

        @Schema(description = "ID of the user who owns the cart", example = "501")
        Long userId,

        @Schema(description = "List of items currently in the cart")
        List<CartItemDto> items,

        @Schema(description = "Sum of MRP prices of all items", example = "2500.0")
        double totalMrpPrice,

        @Schema(description = "Sum of actual selling prices of all items", example = "2100.0")
        double totalSellingPrice,

        @Schema(description = "Total quantity of items in the cart", example = "3")
        int totalItems,

        @Schema(description = "Amount deducted via coupon", example = "200.0")
        double couponDiscount,

        @Schema(description = "Code of the coupon applied, if any", example = "SAVE10")
        String appliedCouponCode
) {
    public static UserCartResponse fromEntity(Cart cart) {
        if (cart == null) {
            return null;
        }

        List<CartItemDto> itemDtos = cart.getCartItems() != null
                ? cart.getCartItems().stream()
                .map(CartItemDto::fromEntity)
                .toList()
                : List.of();

        return new UserCartResponse(
                cart.getId(),
                cart.getUser() != null ? cart.getUser().getId() : null,
                itemDtos,
                cart.getTotalMrpPrice(),
                cart.getTotalSellingPrice(),
                cart.getTotalItem(),
                cart.getDiscount(),
                cart.getCouponCode()
        );
    }
}