package org.riston.ecommerce.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PaymentLinkResponseDto(
        @JsonProperty("payment_link_url") String paymentLinkUrl,
        @JsonProperty("payment_link_id") String paymentLinkId
) {}