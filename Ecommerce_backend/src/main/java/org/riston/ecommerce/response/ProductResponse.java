package org.riston.ecommerce.response;

import org.riston.ecommerce.model.Product;

import java.util.List;

public record ProductResponse(
        Long id,
        String title,
        String description,
        int sellingPrice,
        int mrpPrice,
        int discountPercent,
        String color,
        List<String> images,
        String categoryName,
        String sellerName
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
                (p.getSeller() != null) ? p.getSeller().getSellerName() : "Unknown"
        );
    }
}