package org.riston.ecommerce.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.riston.ecommerce.annotation.ApiNotFoundResponse;
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
@Tag(name = "Admin Deal Management", description = "Endpoints for managing promotional deals.")
public class DealController {
    private final DealService dealService;

    @PostMapping
    @Operation(summary = "Create a new deal")
    @ApiResponse(responseCode = "201", description = "Deal created",
            content = @Content(schema = @Schema(implementation = ApiResponseDto.class)))
    public ResponseEntity<ApiResponseDto<Deal>> createDeals(@RequestBody Deal deals) {
        Deal createdDeals = dealService.createDeal(deals);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDto.success("Deal created successfully", createdDeals));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update a deal")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Updated", content = @Content(schema = @Schema(implementation = ApiResponseDto.class))),
    })
    @ApiNotFoundResponse
    public ResponseEntity<ApiResponseDto<Deal>> updateDeal(@PathVariable Long id, @RequestBody Deal deal) {
        Deal updatedDeal = dealService.updateDeal(deal, id);
        return ResponseEntity.ok(ApiResponseDto.success("Deal updated successfully", updatedDeal));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a deal")
    @ApiResponse(responseCode = "200", description = "Deleted", content = @Content(schema = @Schema(implementation = ApiResponseDto.class)))
    public ResponseEntity<ApiResponseDto<String>> deleteDeals(@PathVariable Long id) {
        dealService.deleteDeal(id);
        return ResponseEntity.ok(ApiResponseDto.success("Deal deleted successfully", null));
    }

    @GetMapping
    @Operation(summary = "Get all deals")
    @ApiResponse(responseCode = "200", description = "Fetched", content = @Content(schema = @Schema(implementation = ApiResponseDto.class)))
    public ResponseEntity<ApiResponseDto<List<Deal>>> getDeals() {
        return ResponseEntity.ok(ApiResponseDto.success("Deals fetched successfully", dealService.getDeals()));
    }
}