package org.riston.ecommerce.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.riston.ecommerce.model.CartItem;
@Schema(description = "Data transfer object representing an item in the shopping cart")
public record CartItemDto(
        @Schema(description = "Unique identifier of the cart item", example = "101")
        Long id,

        @Schema(description = "ID of the associated product", example = "500")
        Long productId,

        @Schema(description = "Title of the product", example = "Running Shoes")
        String productTitle,

        @Schema(description = "URL of the product's primary image")
        String productImage,

        @Schema(description = "Selected size", example = "10")
        String size,

        @Schema(description = "Quantity requested", example = "2")
        int quantity,

        @Schema(description = "Maximum Retail Price", example = "2999.0")
        double mrpPrice,

        @Schema(description = "Actual selling price", example = "1999.0")
        double sellingPrice,

        @Schema(description = "Name of the seller", example = "Nike Official Store")
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
