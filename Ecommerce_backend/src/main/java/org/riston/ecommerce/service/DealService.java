package org.riston.ecommerce.service;

import org.riston.ecommerce.model.Deal;

import java.util.List;

public interface DealService {

    List<Deal> getDeals();
    Deal createDeal(Deal deal);
    Deal updateDeal(Deal deal,Long id);
    void deleteDeal(Long id);
}
