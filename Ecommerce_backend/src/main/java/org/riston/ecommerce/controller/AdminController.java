package org.riston.ecommerce.controller;

import lombok.RequiredArgsConstructor;
import org.riston.ecommerce.domain.AccountStatus;
import org.riston.ecommerce.model.Seller;
import org.riston.ecommerce.response.SellerStatusResponse;
import org.riston.ecommerce.service.SellerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {
    private final SellerService sellerService;
    
    @PatchMapping("/seller/{id}/status/{status}")
    public ResponseEntity<SellerStatusResponse> updateSellerStatus(@PathVariable Long id, @PathVariable AccountStatus status) {
        Seller updatedSeller = sellerService.updateSellerAccountStatus(id, status);
        SellerStatusResponse response = sellerService.mapToStatusResponse(updatedSeller);

        return ResponseEntity.ok(response);
    }
}