package org.riston.ecommerce.response;

import java.util.List;
@SuppressWarnings("unused")
public record HomepageLayoutResponse(
        List<HomeCategoryDto> dealCategories,
        List<DealDto> deals,
        List<HomeCategoryDto> electricCategories,
        List<Object> grid,
        List<Object> shopByCategories
) {}
