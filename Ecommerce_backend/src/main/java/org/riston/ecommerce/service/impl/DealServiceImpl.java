package org.riston.ecommerce.service.impl;

import lombok.RequiredArgsConstructor;
import org.riston.ecommerce.exception.ResourceNotFoundException;
import org.riston.ecommerce.model.Deal;
import org.riston.ecommerce.model.HomeCategory;
import org.riston.ecommerce.repository.DealRepository;
import org.riston.ecommerce.repository.HomeCategoryRepository;
import org.riston.ecommerce.service.DealService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DealServiceImpl implements DealService {
    private final DealRepository dealRepository;
    private final HomeCategoryRepository homeCategoryRepository;

    @Override
    public List<Deal> getDeals() {
        return dealRepository.findAll();
    }

    @Override
    public Deal createDeal(Deal deal) {
        if (deal.getHomeCategory() == null || deal.getHomeCategory().getId() == null) {
            throw new IllegalArgumentException("Home Category ID must be provided to create a deal");
        }

        HomeCategory category = homeCategoryRepository.findById(deal.getHomeCategory().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        deal.setHomeCategory(category);
        return dealRepository.save(deal); 
    }

    @Override
    public Deal updateDeal(Deal deal, Long id) {
        Deal existingDeal = dealRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deal not found"));

        if (deal.getDiscount() != null) {
            existingDeal.setDiscount(deal.getDiscount());
        }

        if (deal.getHomeCategory() != null && deal.getHomeCategory().getId() != null) {
            HomeCategory category = homeCategoryRepository.findById(deal.getHomeCategory().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + deal.getHomeCategory().getId()));
            existingDeal.setHomeCategory(category);
        }

        return dealRepository.save(existingDeal);
    }
    @Override
    public void deleteDeal(Long id) {
        Deal deal = dealRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("deal not found"));
        dealRepository.delete(deal);
    }
}
