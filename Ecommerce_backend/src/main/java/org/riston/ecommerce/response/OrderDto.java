package org.riston.ecommerce.response;

import java.time.LocalDateTime;
import java.util.List;

public record OrderDto(
        Long id,
        String orderId,
        Long sellerId,
        List<OrderItemDto> orderItems,
        ShippingAddressDto shippingAddress,
        double totalMrpPrice,
        int totalSellingPrice,
        String orderStatus,
        int totalItem,
        String paymentStatus,
        LocalDateTime orderDate,
        LocalDateTime deliverDate
) {
    public static OrderDto fromEntity(org.riston.ecommerce.model.Order entity) {
        if (entity == null) return null;

        List<OrderItemDto> items = entity.getOrderItems() != null ?
                entity.getOrderItems().stream().map(OrderItemDto::fromEntity).toList() : List.of();

        return new OrderDto(
                entity.getId(),
                entity.getOrderId(),
                entity.getSellerId(),
                items,
                ShippingAddressDto.fromEntity(entity.getShippingAddress()),
                entity.getTotalMrpPrice(),
                entity.getTotalSellingPrice(),
                entity.getOrderStatus() != null ? entity.getOrderStatus().name() : "PENDING",
                entity.getTotalItem(),
                entity.getPaymentStatus() != null ? entity.getPaymentStatus().name() : "PENDING",
                entity.getOrderDate(),
                entity.getDeliverDate()
        );
    }
}