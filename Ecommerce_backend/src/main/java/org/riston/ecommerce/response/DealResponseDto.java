package org.riston.ecommerce.response;

public record DealResponseDto(
        Long id,
        Integer discount,
        String categoryName
) {}