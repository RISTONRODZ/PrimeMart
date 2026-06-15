package org.riston.ecommerce.response;

import java.time.LocalDateTime;
import java.util.List;

public record ReviewResponseDto(
        Long id,
        String reviewText,
        Double rating,
        List<String> productImages,
        UserSummaryDto user,
        LocalDateTime createdAt
) {}