package org.riston.ecommerce.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.riston.ecommerce.model.Wishlist;
import java.util.List;

@Schema(description = "Response body containing wishlist details")
public record WishlistResponseDto(
        @Schema(description = "Unique identifier of the wishlist", example = "50")
        Long id,

        @Schema(description = "List of products added to the wishlist")
        List<ProductResponse> products
) {
    public static WishlistResponseDto fromEntity(Wishlist wishlist) {
        if (wishlist == null) return null;
        List<ProductResponse> productDtos = wishlist.getProducts() != null ? wishlist.getProducts().stream()
                .map(ProductResponse::new)
                .toList() : List.of();

        return new WishlistResponseDto(
                wishlist.getId(),
                productDtos
        );
    }
}