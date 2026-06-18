package org.riston.ecommerce.request;

public record DealRequestDto(
        Integer discount,
        Long homeCategoryId
) {}
