package org.riston.ecommerce.response;

public record DealDto(
        Long id,
        int discount,
        HomeCategoryDto homeCategory
) {}
