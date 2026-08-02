package org.riston.ecommerce.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.riston.ecommerce.model.OrderItem;
@Schema(description = "Data transfer object representing an individual item within an order")
public record OrderItemDto(
        @Schema(description = "Unique identifier of the order item", example = "1001")
        Long id,

        @Schema(description = "ID of the product", example = "500")
        Long productId,

        @Schema(description = "Title of the product", example = "Running Shoes")
        String productTitle,

        @Schema(description = "URL of the product image")
        String productImage,

        @Schema(description = "Colors of the item (comma-separated)", example = "Blue, Red")
        String color,

        @Schema(description = "Selected size", example = "10")
        String size,

        @Schema(description = "Quantity purchased", example = "1")
        int quantity,

        @Schema(description = "MRP price at the time of purchase", example = "2999")
        int mrpPrice,

        @Schema(description = "Actual selling price at the time of purchase", example = "1999")
        int sellingPrice
) {

    public static OrderItemDto fromEntity(OrderItem entity) {
        if (entity == null) {
            return null;
        }


        var product = entity.getProduct();

        String mainImage = null;
        if (product != null && product.getImages() != null && !product.getImages().isEmpty()) {
            mainImage = product.getImages().getFirst();
        }

        String colors = null;
        if (product != null && product.getColors() != null && !product.getColors().isEmpty()) {
            colors = String.join(", ", product.getColors());
        }

        return new OrderItemDto(entity.getId(), product != null ? product.getId() : null, product != null ? product.getTitle() : "Unknown Product", mainImage, colors, entity.getSize(), entity.getQuantity(), entity.getMrpPrice(), entity.getSellingPrice());
    }
}