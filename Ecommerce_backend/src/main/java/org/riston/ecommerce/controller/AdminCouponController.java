package org.riston.ecommerce.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.riston.ecommerce.annotation.ApiNotFoundResponse;
import org.riston.ecommerce.mapper.CouponMapper;
import org.riston.ecommerce.model.Coupon;
import org.riston.ecommerce.request.CouponRequest;
import org.riston.ecommerce.response.ApiResponseDto;
import org.riston.ecommerce.response.CouponResponseDto;
import org.riston.ecommerce.service.CouponService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/coupons")
@Tag(
        name = "Coupon Management",
        description = "Endpoints for admin to create, manage, and delete discount coupons"
)
public class AdminCouponController {
    private final CouponService couponService;
    private final CouponMapper couponMapper;

    @PostMapping("/create")
    @Operation(summary = "Create a new coupon", description = "Admin-only endpoint to register a new discount code")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Coupon created successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request payload")
    })
    public ResponseEntity<ApiResponseDto<CouponResponseDto>> createCoupon(@Valid @RequestBody CouponRequest request) {
        Coupon couponEntity = couponMapper.toEntity(request);
        Coupon savedCoupon = couponService.createCoupon(couponEntity);
        CouponResponseDto response = couponMapper.toDto(savedCoupon);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDto.success("Coupon created", response));
    }
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete a coupon", description = "Permanently removes a coupon from the system by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Coupon deleted successfully"),
    })
    @ApiNotFoundResponse
    public ResponseEntity<ApiResponseDto<Void>> deleteCoupon(@PathVariable Long id) {
        couponService.deleteCoupon(id);
        return ResponseEntity.ok(ApiResponseDto.success("Coupon deleted successfully", null));
    }

    @GetMapping("/all")
    @Operation(summary = "Get all coupons", description = "Retrieves a complete list of all active discount coupons")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Coupons retrieved successfully")
    })
    public ResponseEntity<ApiResponseDto<List<CouponResponseDto>>> getAllCoupons() {
        List<CouponResponseDto> response = couponService.findAllCoupons();
        return ResponseEntity.ok(ApiResponseDto.success("All coupons retrieved", response));
    }
}