package org.riston.ecommerce.response;

import java.time.LocalDate;

public record CouponResponseDto(
        Long id,
        String code,
        String discountPercentage,
        LocalDate validityStartDate,
        LocalDate validityEndDate,
        double minimumOrderValue,
        Boolean isActive
) {}