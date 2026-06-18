package org.riston.ecommerce.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.riston.ecommerce.model.HomeCategory;
@Schema(description = "Data transfer object representing a home page category")
public record HomeCategoryDto(
        @Schema(description = "Unique identifier of the category", example = "1")
        Long id,

        @Schema(description = "Name of the category", example = "Electronics")
        String name,

        @Schema(description = "URL of the category image")
        String image,

        @Schema(description = "Internal category ID string", example = "cat_001")
        String categoryId,

        @Schema(description = "The section on the home page this category belongs to", example = "TOP_DEALS")
        String section
) {
    public static HomeCategoryDto fromEntity(HomeCategory entity) {
        if (entity == null) {
            return null;
        }
        return new HomeCategoryDto(
                entity.getId(),
                entity.getName(),
                entity.getImage(),
                entity.getCategoryId(),
                entity.getSection() != null ? entity.getSection().name() : null
        );
    }
}
