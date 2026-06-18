package org.riston.ecommerce.mapper;

import org.riston.ecommerce.model.Deal;
import org.riston.ecommerce.response.DealResponseDto;
import org.springframework.stereotype.Component;

@Component
public class DealMapper {
    public DealResponseDto toDto(Deal deal) {
        return new DealResponseDto(
                deal.getId(),
                deal.getDiscount(),
                deal.getHomeCategory().getName()
        );
    }
}