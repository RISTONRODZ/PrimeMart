package org.riston.ecommerce.controller;

import lombok.RequiredArgsConstructor;
import org.riston.ecommerce.model.Coupon;
import org.riston.ecommerce.response.ApiResponseDto; // ⚡ Added Envelope
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
    public ResponseEntity<ApiResponseDto<Coupon>> createCoupon(@RequestBody Coupon coupon) {
        Coupon createdCoupon = couponService.createCoupon(coupon);
        return ResponseEntity.ok(ApiResponseDto.success("Coupon created successfully", createdCoupon));
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