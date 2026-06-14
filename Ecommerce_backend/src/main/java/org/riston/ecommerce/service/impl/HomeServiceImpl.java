package org.riston.ecommerce.service.impl;

import lombok.RequiredArgsConstructor;
import org.riston.ecommerce.domain.HomeCategorySection;
import org.riston.ecommerce.model.Deal;
import org.riston.ecommerce.model.Home;
import org.riston.ecommerce.model.HomeCategory;
import org.riston.ecommerce.model.Product;
import org.riston.ecommerce.repository.DealRepository;
import org.riston.ecommerce.repository.ProductRepository;
import org.riston.ecommerce.service.HomeService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {
    private final DealRepository dealRepository;
    private final ProductRepository productRepository;

    @Override
    public Home createHomePageData(List<HomeCategory> allCategories) {
        Map<HomeCategorySection, List<HomeCategory>> categorized = allCategories.stream()
                .collect(Collectors.groupingBy(HomeCategory::getSection));

        // Ensure Deals exist
        List<Deal> deals = dealRepository.findAll();
        if (deals.isEmpty()) {
            deals = initializeDeals(categorized.getOrDefault(HomeCategorySection.DEALS, List.of()));
        }

        Home home = new Home();
        home.setGrid(categorized.getOrDefault(HomeCategorySection.GRID, List.of()));
        home.setShopByCategories(categorized.getOrDefault(HomeCategorySection.SHOP_BY_CATEGORIES, List.of()));
        home.setElectricCategories(categorized.getOrDefault(HomeCategorySection.ELECTRIC_CATEGORIES, List.of()));
        home.setDealCategories(categorized.getOrDefault(HomeCategorySection.DEALS, List.of()));
        home.setDeals(deals);

        return home;
    }

    private List<Deal> initializeDeals(List<HomeCategory> dealCategories) {
        List<Deal> deals = dealCategories.stream().map(cat -> {
            int maxDiscount = productRepository.findByCategoryId(cat.getId()).stream()
                    .mapToInt(Product::getDiscountPercent).max().orElse(0);
            return new Deal(null, maxDiscount, cat);
        }).collect(Collectors.toList());
        return dealRepository.saveAll(deals);
    }
}