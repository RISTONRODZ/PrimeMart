package org.riston.ecommerce.request;

import jakarta.validation.constraints.*;

import java.util.List;

public record CreateProductRequestDto(
        @NotBlank(message = "Product title is required") @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters") String title,

        @NotBlank(message = "Product description is required") @Size(max = 1000, message = "Description cannot exceed 1000 characters") String description,

        @NotNull(message = "MRP price is required") @Min(value = 1, message = "MRP price must be greater than 0") Integer mrpPrice,

        @NotNull(message = "Selling price is required") @Min(value = 1, message = "Selling price must be greater than 0") Integer sellingPrice,

        @NotEmpty(message = "At least one product image URL must be provided") List<String> images,

        @NotBlank(message = "Primary category is required") String category,

        @Size(max = 50, message = "Category name is too long") String category2,

        @Size(max = 50, message = "Category name is too long") String category3,

        @NotBlank(message = "Available sizes must be specified (e.g., S,M,L)") String sizes,

        @NotEmpty(message = "At least one product color must be provided") List<String> colors) {
}