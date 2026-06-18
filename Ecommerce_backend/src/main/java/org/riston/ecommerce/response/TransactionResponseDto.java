package org.riston.ecommerce.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Data Transfer Object for transaction response details")
public record TransactionResponseDto(
        @Schema(description = "Unique identifier of the transaction", example = "101")
        Long id,

        @Schema(description = "ID of the associated order", example = "5005")
        Long orderId,

        @Schema(description = "Unique tracking ID for the order", example = "TRK-998877")
        String orderTrackingId,

        @Schema(description = "ID of the seller", example = "202")
        Long sellerId,

        @Schema(description = "Name of the seller", example = "Electronics Hub")
        String sellerName,

        @Schema(description = "Total price of the transaction", example = "1500")
        Integer totalSellingPrice,

        @Schema(description = "Current status of the payment", example = "SUCCESS")
        String paymentStatus,

        @Schema(description = "Timestamp of the transaction", type = "string", format = "date-time", example = "2026-06-18T17:55:00")
        LocalDateTime date
) {
}