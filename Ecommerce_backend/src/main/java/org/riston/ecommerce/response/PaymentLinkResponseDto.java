package org.riston.ecommerce.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response payload containing Razorpay order details for checkout")
public record PaymentLinkResponseDto(
        @Schema(description = "The Razorpay order ID for checkout", example = "order_123456789")
        @JsonProperty("order_id")
        String orderId,

        @Schema(description = "Payment amount in paise", example = "10000")
        @JsonProperty("amount")
        Long amount,

        @Schema(description = "Currency code", example = "INR")
        @JsonProperty("currency")
        String currency,

        @Schema(description = "Store name", example = "Ecommerce Store")
        @JsonProperty("name")
        String name,

        @Schema(description = "Payment description", example = "Payment for order #123")
        @JsonProperty("description")
        String description,

        @Schema(description = "Customer details")
        @JsonProperty("customer")
        CustomerDto customer,

        @Schema(description = "Callback URL after payment", example = "https://example.com/success/123")
        @JsonProperty("callback_url")
        String callbackUrl
) {
    @Schema(description = "Customer details for payment")
    public record CustomerDto(
            @Schema(description = "Customer name", example = "John Doe")
            @JsonProperty("name")
            String name,

            @Schema(description = "Customer email", example = "john@example.com")
            @JsonProperty("email")
            String email
    ) {}
}