package org.riston.ecommerce.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

public record ReviewResponseDto(
        @Schema(example = "101")
        Long id,

        @Schema(example = "This product exceeded my expectations!")
        String reviewText,

        @Schema(example = "5.0")
        Double rating,

        @Schema(example = "[\"https://cdn.example.com/image1.jpg\"]")
        List<String> productImages,

        UserSummaryDto user,

        @Schema(example = "2026-06-18T15:30:00")
        LocalDateTime createdAt
) {}