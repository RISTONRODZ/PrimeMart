package org.riston.ecommerce.mapper;

import org.riston.ecommerce.model.CartItem;
import org.riston.ecommerce.model.Product;
import org.riston.ecommerce.response.CartItemDto;
import org.springframework.stereotype.Component;

@Component
public class CartItemMapper {
    public CartItemDto toDto(CartItem item) {
        Product product = item.getProduct();
        return new CartItemDto(
                item.getId(),
                product.getId(),
                product.getTitle(),
                (product.getImages() != null && !product.getImages().isEmpty()) ? product.getImages().get(0) : null,
                item.getSize(),
                item.getQuantity(),
                item.getMrpPrice(),
                item.getSellingPrice(),
                product.getSeller() != null ? product.getSeller().getSellerName() : null
        );
    }
}