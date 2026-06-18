package org.riston.ecommerce.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Response payload representing the full layout of the homepage")
@SuppressWarnings("unused")
public record HomepageLayoutResponse(
        @Schema(description = "List of categories featured in deals")
        List<HomeCategoryDto> dealCategories,

        @Schema(description = "List of promotional deals")
        List<DealDto> deals,

        @Schema(description = "List of electronics-specific categories")
        List<HomeCategoryDto> electricCategories,

        @Schema(description = "Layout grid items (generic objects)")
        List<Object> grid,

        @Schema(description = "Categories available for shopping")
        List<HomeCategoryDto> shopByCategories
) {
}