package org.riston.ecommerce.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.riston.ecommerce.annotation.ApiNotFoundResponse;
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
@Tag(name = "Admin Endpoints", description = "Endpoints for administrative tasks like managing deals, coupons, and seller status")
public class AdminController {
    private final SellerService sellerService;
    
    @PatchMapping("/seller/{id}/status/{status}")
    @Operation(
        summary = "Update seller account status",
        description = "Updates the account status (ACTIVE, PENDING, SUSPENDED, BANNED, CLOSED) of a seller"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Seller status updated successfully"),
    })
    @ApiNotFoundResponse
    public ResponseEntity<SellerStatusResponse> updateSellerStatus(
        @Parameter(description = "Seller ID", required = true)
        @PathVariable Long id,
        @Parameter(description = "New account status", required = true)
        @PathVariable AccountStatus status
    ) {
        Seller updatedSeller = sellerService.updateSellerAccountStatus(id, status);
        SellerStatusResponse response = sellerService.mapToStatusResponse(updatedSeller);

        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/seller/{id}")
    @Operation(summary = "Delete seller", description = "Permanently removes a seller from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Seller deleted successfully"),
    })
    @ApiNotFoundResponse
    public ResponseEntity<Void> adminDeleteSeller(@PathVariable Long id) {
        sellerService.deleteSeller(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping
    @Operation(
        summary = "Get all sellers",
        description = "Retrieves a list of all sellers, optionally filtered by account status"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sellers retrieved successfully")
    })
    public ResponseEntity<List<SellerResponseDto>> getAllSeller(
        @Parameter(description = "Filter by account status (optional)")
        @RequestParam(required = false) AccountStatus status
    ) {
        List<SellerResponseDto> responses = sellerService.getAllSellers(status).stream()
                .map(SellerResponseDto::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
}