package org.riston.ecommerce.service.impl;

import lombok.RequiredArgsConstructor;
import org.riston.ecommerce.exception.ResourceNotFoundException;
import org.riston.ecommerce.model.Deal;
import org.riston.ecommerce.model.HomeCategory;
import org.riston.ecommerce.repository.DealRepository;
import org.riston.ecommerce.repository.HomeCategoryRepository;
import org.riston.ecommerce.request.DealRequestDto;
import org.riston.ecommerce.service.DealService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public Deal createDeal(DealRequestDto request) {
        HomeCategory category = homeCategoryRepository.findById(request.homeCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));Deal deal = new Deal();
        deal.setDiscount(request.discount());
        deal.setHomeCategory(category);
        return dealRepository.save(deal);
    }

    @Override
    @Transactional
    public Deal updateDeal(DealRequestDto request, Long id) {
        Deal existingDeal = dealRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deal not found"));

        if (request.discount() != null) {
            existingDeal.setDiscount(request.discount());
        }

        if (request.homeCategoryId() != null) {
            HomeCategory category = homeCategoryRepository.findById(request.homeCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + request.homeCategoryId()));
            existingDeal.setHomeCategory(category);
        }

        return dealRepository.save(existingDeal);
    }
    @Override
    @Transactional
    public void deleteDeal(Long id) {
        Deal deal = dealRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("deal not found"));
        dealRepository.delete(deal);
    }
}
