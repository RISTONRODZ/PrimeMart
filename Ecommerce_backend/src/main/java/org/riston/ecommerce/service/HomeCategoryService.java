package org.riston.ecommerce.service;

import org.riston.ecommerce.model.HomeCategory;

import java.util.List;

public interface HomeCategoryService {
    List<HomeCategory> createCategories(List<HomeCategory> homeCategories);
    HomeCategory updateHomeCategory(HomeCategory homeCategory, Long id);
    List<HomeCategory> getAllHomeCategories();
    void deleteHomeCategory(Long id);
}
