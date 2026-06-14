package org.riston.ecommerce.service;

import org.riston.ecommerce.model.HomeCategory;

import java.util.List;

public interface HomeCategoryService {
    @SuppressWarnings("unused")
    HomeCategory createHomeCategory(HomeCategory homeCategory);
    List<HomeCategory> createCategories(List<HomeCategory> homeCategories);
    HomeCategory updateHomeCategory(HomeCategory homeCategory, Long id);
    List<HomeCategory> getAllHomeCategories();
}
