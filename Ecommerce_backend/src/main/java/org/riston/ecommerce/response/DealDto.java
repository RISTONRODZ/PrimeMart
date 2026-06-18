package org.riston.ecommerce.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Data transfer object representing a promotional deal")
public record DealDto(
        @Schema(description = "Unique identifier of the deal", example = "1")
        Long id,

        @Schema(description = "Discount percentage offered in the deal", example = "20")
        int discount,

        @Schema(description = "The category associated with this deal")
        HomeCategoryDto homeCategory
) {}