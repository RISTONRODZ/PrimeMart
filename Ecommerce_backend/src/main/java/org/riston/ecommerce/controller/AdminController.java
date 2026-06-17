package org.riston.ecommerce.controller;

import lombok.RequiredArgsConstructor;
import org.riston.ecommerce.domain.AccountStatus;
import org.riston.ecommerce.model.Seller;
import org.riston.ecommerce.response.SellerResponseDto;
import org.riston.ecommerce.response.SellerStatusResponse;
import org.riston.ecommerce.service.SellerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
    @DeleteMapping("/seller/{id}")
    public ResponseEntity<Void> adminDeleteSeller(@PathVariable Long id) {
        sellerService.deleteSeller(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping
    public ResponseEntity<List<SellerResponseDto>> getAllSeller(@RequestParam(required = false) AccountStatus status) {
        List<SellerResponseDto> responses = sellerService.getAllSellers(status).stream()
                .map(s -> new SellerResponseDto(s.getId(), s.getSellerName(), s.getEmail(), s.getMobile(), s.getGSTIN(), s.getAccountStatus(), s.getEmailVerified()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
}