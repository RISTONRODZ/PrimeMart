package org.riston.ecommerce.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;
@Schema(description = "Data transfer object representing a customer order")
public record OrderDto(
        @Schema(description = "Internal database ID", example = "1")
        Long id,

        @Schema(description = "Public-facing order identifier", example = "ORD-998877")
        String orderId,

        @Schema(description = "ID of the seller", example = "55")
        Long sellerId,

        @Schema(description = "List of items contained in the order")
        List<OrderItemDto> orderItems,

        @Schema(description = "Shipping address details")
        ShippingAddressDto shippingAddress,

        @Schema(description = "Total original price of items", example = "4999.0")
        double totalMrpPrice,

        @Schema(description = "Total discounted price to be paid", example = "3999")
        int totalSellingPrice,

        @Schema(description = "Current status of the order", example = "PENDING")
        String orderStatus,

        @Schema(description = "Total count of items in the order", example = "3")
        int totalItem,

        @Schema(description = "Status of the payment", example = "PENDING")
        String paymentStatus,

        @Schema(description = "Date and time the order was placed")
        LocalDateTime orderDate,

        @Schema(description = "Expected delivery date")
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