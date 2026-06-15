package org.riston.ecommerce.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;
@Data
public class CreateReviewRequest {
    @NotBlank(message = "Review must not be blank")
    private String reviewText;
    @NotNull
    @DecimalMin(value = "0.5",message = "Rating must be at least 0.5 stars")
    @DecimalMax(value = "5.0", message = "Rating cannot exceed 5.0 stars")
    private Double reviewRating;
    @NotEmpty(message = "The product must have at least one image")
    private List<String> productImages;
}
