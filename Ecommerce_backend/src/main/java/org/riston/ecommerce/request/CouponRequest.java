package org.riston.ecommerce.request;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public record CouponRequest(
        @NotBlank(message = "Coupon code is required")
        String code,

        @NotNull(message = "Discount percentage is required")
        @Min(0) @Max(100)
        Integer discountPercentage,

        @NotNull(message = "Minimum order value is required")
        @DecimalMin("0.0")
        Double minimumOrderValue,

        @NotNull(message = "Active status is required")
        Boolean isActive,

        @NotNull(message = "Start date is required")
        LocalDateTime validityStartDate,

        @NotNull(message = "End date is required")
        LocalDateTime validityEndDate
) {}