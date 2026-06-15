package org.riston.ecommerce.response;

import org.riston.ecommerce.model.HomeCategory;

public record HomeCategoryDto(
        Long id,
        String name,
        String image,
        String categoryId,
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
