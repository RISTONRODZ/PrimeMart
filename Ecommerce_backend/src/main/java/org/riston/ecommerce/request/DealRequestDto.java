package org.riston.ecommerce.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record DealRequestDto(
        @NotNull(message = "Discount is required")
        @Min(value = 0, message = "Discount must be greater than or equal to 0")
        Integer discount,

        @NotNull(message = "Home category ID is required")
        @Min(value = 1, message = "Home category ID must be positive")
        Long homeCategoryId
) {}
