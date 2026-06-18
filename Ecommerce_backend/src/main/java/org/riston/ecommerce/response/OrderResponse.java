package org.riston.ecommerce.response;

public record OrderResponse(
        String orderId,
        String orderStatus,
        int totalItem,
        double totalSellingPrice,
        String orderDate,
        String shippingAddressCity
) {}