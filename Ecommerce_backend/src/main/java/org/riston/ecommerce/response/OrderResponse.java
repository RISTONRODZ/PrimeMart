package org.riston.ecommerce.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response payload providing a summary view of an order")
public record OrderResponse(
        @Schema(description = "The public-facing order identifier", example = "ORD-998877")
        String orderId,

        @Schema(description = "Current status of the order", example = "DELIVERED")
        String orderStatus,

        @Schema(description = "Total number of items in the order", example = "3")
        int totalItem,

        @Schema(description = "Total price paid for the order", example = "3999.0")
        double totalSellingPrice,

        @Schema(description = "The date the order was placed", example = "2026-06-18")
        String orderDate,

        @Schema(description = "The city of the shipping destination", example = "Mumbai")
        String shippingAddressCity
) {}