package org.riston.ecommerce.request;

import jakarta.validation.constraints.*;
import java.util.List;

public record CreateReviewRequestDto(
        @NotBlank(message = "Review must not be blank")
        String reviewText,

        @NotNull
        @DecimalMin(value = "0.5", message = "Rating must be at least 0.5 stars")
        @DecimalMax(value = "5.0", message = "Rating cannot exceed 5.0 stars")
        Double reviewRating,

        @NotEmpty(message = "The product must have at least one image")
        List<String> productImages
) {}