package org.riston.ecommerce.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.riston.ecommerce.model.Coupon;
import org.riston.ecommerce.request.CouponRequest;
import org.riston.ecommerce.response.ApiResponseDto;
import org.riston.ecommerce.service.CouponService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/coupons")
public class AdminCouponController {
    private final CouponService couponService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponseDto<Coupon>> createCoupon(@Valid @RequestBody CouponRequest request) {
        return ResponseEntity.ok(ApiResponseDto.success("Coupon created", couponService.createCoupon(request)));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponseDto<Void>> deleteCoupon(@PathVariable Long id) {
        couponService.deleteCoupon(id);
        return ResponseEntity.ok(ApiResponseDto.success("Coupon deleted successfully", null));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponseDto<List<Coupon>>> getAllCoupons() {
        List<Coupon> coupons = couponService.findAllCoupons();
        return ResponseEntity.ok(ApiResponseDto.success("All coupons retrieved successfully", coupons));
    }
}