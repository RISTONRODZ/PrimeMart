package org.riston.ecommerce.response;

import org.riston.ecommerce.model.Wishlist;
import java.util.List;

public record WishlistResponseDto(
        Long id,
        List<ProductResponse> products
) {
    public static WishlistResponseDto fromEntity(Wishlist wishlist) {
        List<ProductResponse> productDtos = wishlist.getProducts() != null ? wishlist.getProducts().stream()
                .map(ProductResponse::new)
                .toList() : List.of();

        return new WishlistResponseDto(
                wishlist.getId(),
                productDtos
        );
    }
}