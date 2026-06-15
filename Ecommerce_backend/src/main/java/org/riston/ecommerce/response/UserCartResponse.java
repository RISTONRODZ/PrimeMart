package org.riston.ecommerce.response;
import org.riston.ecommerce.model.Cart;
import java.util.List;

public record UserCartResponse(
        Long id,
        Long userId,
        List<CartItemDto> items,
        double totalMrpPrice,
        double totalSellingPrice,
        int totalItems,
        double couponDiscount,
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