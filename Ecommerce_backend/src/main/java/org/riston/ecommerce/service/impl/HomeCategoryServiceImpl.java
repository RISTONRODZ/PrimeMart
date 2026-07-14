package org.riston.ecommerce.service.impl;

import org.riston.ecommerce.domain.HomeCategorySection;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.riston.ecommerce.exception.ResourceNotFoundException;
import org.riston.ecommerce.model.HomeCategory;
import org.riston.ecommerce.repository.DealRepository;
import org.riston.ecommerce.repository.HomeCategoryRepository;
import org.riston.ecommerce.service.HomeCategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class HomeCategoryServiceImpl implements HomeCategoryService {
    private final HomeCategoryRepository homeCategoryRepository;
    private final DealRepository dealRepository;

    @Transactional
    public List<HomeCategory> createCategories(List<HomeCategory> homeCategories) {
        for (HomeCategory cat : homeCategories) {
            if (cat.getCategoryId() == null) {
                throw new IllegalArgumentException("Category ID cannot be null");
            }
        }

        long incomingGridCount = homeCategories.stream()
                .filter(c -> c.getSection() == HomeCategorySection.GRID)
                .count();

        if (incomingGridCount > 0) {
            long existingGridCount = homeCategoryRepository.countBySection(HomeCategorySection.GRID);
            if (existingGridCount + incomingGridCount > 6) {
                throw new IllegalArgumentException(
                        "Grid section can only contain exactly 6 categories (currently has "
                                + existingGridCount + ", tried to add " + incomingGridCount + ")");
            }
        }

        return homeCategoryRepository.saveAll(homeCategories);
    }
    @Override
    @Transactional
    public HomeCategory updateHomeCategory(HomeCategory category, Long id) {
        HomeCategory existingCategory = homeCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        if (category.getImage() != null) {
            existingCategory.setImage(category.getImage());
        }

        if (category.getCategoryId() != null) {
            existingCategory.setCategoryId(category.getCategoryId());
        }

        if (category.getSection() != null) {
            existingCategory.setSection(category.getSection());
        }

        return homeCategoryRepository.save(existingCategory);
    }

    @Override
    public List<HomeCategory> getAllHomeCategories() {
        return homeCategoryRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteHomeCategory(Long id) {
        HomeCategory category = homeCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Home category not found with id: " + id));
        dealRepository.deleteByHomeCategoryId(id);
        homeCategoryRepository.delete(category);
    }
}
