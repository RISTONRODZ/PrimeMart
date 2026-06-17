package org.riston.ecommerce.controller;

import lombok.RequiredArgsConstructor;
import org.riston.ecommerce.model.Seller;
import org.riston.ecommerce.model.SellerReport;
import org.riston.ecommerce.response.SellerReportResponse;
import org.riston.ecommerce.response.SellerResponseDto;
import org.riston.ecommerce.service.SellerReportService;
import org.riston.ecommerce.service.SellerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/seller")
public class SellerController {
    private final SellerService sellerService;
    private final SellerReportService sellerReportService;

    @GetMapping("/profile")
    public ResponseEntity<SellerResponseDto> getSellerByJwt(@RequestHeader("Authorization") String jwt) {
        Seller s = sellerService.getSellerProfile(jwt);
        return ResponseEntity.ok(SellerResponseDto.fromEntity(s));
    }

    @GetMapping("/report")
    public ResponseEntity<SellerReportResponse> getSellerReport(@RequestHeader("Authorization") String jwt) {
        Seller seller = sellerService.getSellerProfile(jwt);
        SellerReport report = sellerReportService.getSellerReport(seller);
        return ResponseEntity.ok(new SellerReportResponse(report));
    }

    @PatchMapping
    public ResponseEntity<SellerResponseDto> updateSeller(@RequestHeader("Authorization") String jwt, @RequestBody Seller seller) {
        Seller profile = sellerService.getSellerProfile(jwt);
        Seller updated = sellerService.updateSeller(profile.getId(), seller);
        return ResponseEntity.ok(SellerResponseDto.fromEntity(updated));
    }
    @PostMapping("/verify/{otp}")
    public ResponseEntity<SellerResponseDto> verifyEmail(@RequestParam String email, @PathVariable String otp) {
        Seller verifiedSeller = sellerService.verifyEmail(email, otp);
        return ResponseEntity.ok(SellerResponseDto.fromEntity(verifiedSeller));
    }
}