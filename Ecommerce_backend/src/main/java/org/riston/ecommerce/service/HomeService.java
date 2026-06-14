package org.riston.ecommerce.service;

import org.riston.ecommerce.model.Home;
import org.riston.ecommerce.model.HomeCategory;

import java.util.List;

public interface HomeService {
    Home createHomePageData(List<HomeCategory> allCategories);
}
