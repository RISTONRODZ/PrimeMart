package org.riston.ecommerce.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.riston.ecommerce.model.Product;

import java.util.List;
@Schema(description = "Detailed response payload for a product")
public record ProductResponse(
        @Schema(description = "Unique product identifier", example = "500")
        Long id,

        @Schema(description = "Product title", example = "Running Shoes")
        String title,

        @Schema(description = "Detailed description of the product")
        String description,

        @Schema(description = "Final price after discount", example = "1999")
        int sellingPrice,

        @Schema(description = "Original MRP price", example = "2999")
        int mrpPrice,

        @Schema(description = "Discount percentage", example = "33")
        int discountPercent,

        @Schema(description = "Available color", example = "Blue")
        String color,

        @Schema(description = "List of product image URLs")
        List<String> images,

        @Schema(description = "Associated category name", example = "Footwear")
        String categoryName,

        @Schema(description = "Name of the seller", example = "Nike Official Store")
        String sellerName,

        @Schema(description = "Available clothing or shoe sizes", example = "S, M, L, XL")
        String sizes,

        @Schema(description = "Total number of ratings received for the product", example = "1250")
        int numRatings

) {
    public ProductResponse(Product p) {
        this(
                p.getId(),
                p.getTitle(),
                p.getDescription(),
                p.getSellingPrice(),
                p.getMrpPrice(),
                p.getDiscountPercent(),
                p.getColor(),
                p.getImages(),
                (p.getCategory() != null) ? p.getCategory().getCategoryId() : "N/A",
                (p.getSeller() != null) ? p.getSeller().getSellerName() : "Unknown",
                p.getSizes(),
                p.getNumRatings()
        );
    }
}