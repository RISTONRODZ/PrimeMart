package org.riston.ecommerce.controller;

import lombok.RequiredArgsConstructor;
import org.riston.ecommerce.model.Deal;
import org.riston.ecommerce.response.ApiResponseDto;
import org.riston.ecommerce.service.DealService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/deals")
public class DealController {
    private final DealService dealService;

    @PostMapping
    public ResponseEntity<ApiResponseDto<Deal>> createDeals(@RequestBody Deal deals) {
        Deal createdDeals = dealService.createDeal(deals);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDto.success("Deal created successfully", createdDeals));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponseDto<Deal>> updateDeal(@PathVariable Long id, @RequestBody Deal deal) {
        Deal updatedDeal = dealService.updateDeal(deal, id);
        return ResponseEntity.ok(ApiResponseDto.success("Deal updated successfully", updatedDeal));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<String>> deleteDeals(@PathVariable Long id) {
        dealService.deleteDeal(id);
        return ResponseEntity.ok(ApiResponseDto.success("Deal deleted successfully", null));
    }

    @GetMapping
    public ResponseEntity<ApiResponseDto<List<Deal>>> getDeals() {
        List<Deal> deals = dealService.getDeals();
        return ResponseEntity.ok(ApiResponseDto.success("Deals fetched successfully", deals));
    }
}