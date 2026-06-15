package org.riston.ecommerce.response;

import org.riston.ecommerce.model.CartItem;

public record CartItemDto(
     Long id,
     Long productId,
     String productTitle,
     String productImage,
     String size,
     int quantity,
     double mrpPrice,
     double sellingPrice,
     String sellerName

) {
    public static CartItemDto fromEntity(CartItem item) {
        return new CartItemDto(
                item.getId(),
                item.getProduct() != null ? item.getProduct().getId() : null,
                item.getProduct() != null ? item.getProduct().getTitle() : null,
                (item.getProduct() != null && item.getProduct().getImages() != null && !item.getProduct().getImages().isEmpty())
                        ? item.getProduct().getImages().get(0) : null,
                item.getSize(),
                item.getQuantity(),
                item.getMrpPrice(),
                item.getSellingPrice(),
                (item.getProduct() != null && item.getProduct().getSeller() != null)
                        ? item.getProduct().getSeller().getSellerName() : null
        );
    }
}
