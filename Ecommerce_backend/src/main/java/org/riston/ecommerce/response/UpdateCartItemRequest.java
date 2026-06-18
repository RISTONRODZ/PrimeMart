package org.riston.ecommerce.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;

@Schema(description = "Request body for updating a cart item")
public record UpdateCartItemRequest(
        @Schema(description = "The updated quantity of the item", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
        @Min(value = 1, message = "Quantity must be at least 1")
        int quantity,

        @Schema(description = "The size of the item, if applicable", example = "M")
        String size
) {}