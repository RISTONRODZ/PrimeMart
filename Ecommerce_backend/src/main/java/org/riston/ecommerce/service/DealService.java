package org.riston.ecommerce.service;

import org.riston.ecommerce.model.Deal;
import org.riston.ecommerce.request.DealRequestDto;

import java.util.List;

public interface DealService {

    List<Deal> getDeals();
    Deal createDeal(
            DealRequestDto deal);
    Deal updateDeal(DealRequestDto deal,Long id);
    void deleteDeal(Long id);
}
