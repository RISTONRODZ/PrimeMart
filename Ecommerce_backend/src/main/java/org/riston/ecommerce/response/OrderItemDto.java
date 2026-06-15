package org.riston.ecommerce.response;

import org.riston.ecommerce.model.OrderItem;

public record OrderItemDto(Long id, Long productId, String productTitle, String productImage, String color, String size,
                           int quantity, int mrpPrice, int sellingPrice) {

    public static OrderItemDto fromEntity(OrderItem entity) {
        if (entity == null) {
            return null;
        }


        var product = entity.getProduct();

        String mainImage = null;
        if (product != null && product.getImages() != null && !product.getImages().isEmpty()) {
            mainImage = product.getImages().get(0);
        }

        return new OrderItemDto(entity.getId(), product != null ? product.getId() : null, product != null ? product.getTitle() : "Unknown Product", mainImage, product != null ? product.getColor() : null, entity.getSize(), entity.getQuantity(), entity.getMrpPrice(), entity.getSellingPrice());
    }
}