package org.riston.ecommerce.response;

import java.time.LocalDateTime;

public record TransactionResponseDto(
        Long id,
        Long orderId,
        String orderTrackingId,
        Long sellerId,
        String sellerName,
        Integer totalSellingPrice,
        String paymentStatus,
        LocalDateTime date
) {
}