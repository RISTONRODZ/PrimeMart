package org.riston.ecommerce.response;

import jakarta.validation.constraints.Min;

public record UpdateCartItemRequest(
        @Min(value = 1, message = "Quantity must be at least 1")
        int quantity,

        String size
) {}