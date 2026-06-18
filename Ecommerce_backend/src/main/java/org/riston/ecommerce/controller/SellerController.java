package org.riston.ecommerce.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.riston.ecommerce.annotation.ApiNotFoundResponse;
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
    @Operation(
        summary = "Get seller profile",
        description = "Retrieves the authenticated seller's profile information"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Seller profile retrieved successfully"),
    })
    public ResponseEntity<SellerResponseDto> getSellerByJwt(
        @RequestHeader("Authorization") String jwt
    ) {
        Seller s = sellerService.getSellerProfile(jwt);
        return ResponseEntity.ok(SellerResponseDto.fromEntity(s));
    }

    @GetMapping("/report")
    @Operation(
        summary = "Get seller report",
        description = "Retrieves sales report and earnings information for the authenticated seller"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Seller report retrieved successfully"),
    })
    public ResponseEntity<SellerReportResponse> getSellerReport(
        @RequestHeader("Authorization") String jwt
    ) {
        Seller seller = sellerService.getSellerProfile(jwt);
        SellerReport report = sellerReportService.getSellerReport(seller);
        return ResponseEntity.ok(new SellerReportResponse(report));
    }

    @PatchMapping
    @Operation(
        summary = "Update seller profile",
        description = "Updates the authenticated seller's profile information"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Seller profile updated successfully"),
    })
    public ResponseEntity<SellerResponseDto> updateSeller(
        @RequestHeader("Authorization") String jwt,
        @RequestBody Seller seller
    ) {
        Seller profile = sellerService.getSellerProfile(jwt);
        Seller updated = sellerService.updateSeller(profile.getId(), seller);
        return ResponseEntity.ok(SellerResponseDto.fromEntity(updated));
    }
    @PostMapping("/verify/{otp}")
    @Operation(
        summary = "Verify seller email",
        description = "Verifies the seller's email address using OTP"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Email verified successfully"),
    })
    @ApiNotFoundResponse
    public ResponseEntity<SellerResponseDto> verifyEmail(
        @Parameter(description = "Email address to verify", required = true)
        @RequestParam String email,
        @Parameter(description = "OTP sent to the email", required = true)
        @PathVariable String otp
    ) {
        Seller verifiedSeller = sellerService.verifyEmail(email, otp);
        return ResponseEntity.ok(SellerResponseDto.fromEntity(verifiedSeller));
    }
}