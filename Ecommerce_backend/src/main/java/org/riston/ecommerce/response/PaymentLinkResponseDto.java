package org.riston.ecommerce.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response payload containing payment link details")
public record PaymentLinkResponseDto(
        @Schema(description = "The URL to redirect the user for payment", example = "https://payment-gateway.com/pay/12345")
        @JsonProperty("payment_link_url")
        String paymentLinkUrl,

        @Schema(description = "The unique identifier for the payment link", example = "pl_123456789")
        @JsonProperty("payment_link_id")
        String paymentLinkId
) {}