package org.riston.ecommerce.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddItemRequestDto(
        @NotBlank(message = "Size is required")
        String size,

        @Min(value = 1, message = "Quantity must be at least 1")
        int quantity,

        @NotNull(message = "Product ID cannot be null")
        Long productId
) {}