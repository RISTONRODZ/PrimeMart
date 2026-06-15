package org.riston.ecommerce.controller;

import lombok.RequiredArgsConstructor;
import org.riston.ecommerce.model.Cart;
import org.riston.ecommerce.model.Coupon;
import org.riston.ecommerce.model.User;
import org.riston.ecommerce.response.ApiResponseDto; // ⚡ Added Envelope
import org.riston.ecommerce.service.CouponService;
import org.riston.ecommerce.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/coupons")
public class AdminCouponController {
    private final CouponService couponService;
    private final UserService userService;

    @PostMapping("/apply")
    public ResponseEntity<ApiResponseDto<Cart>> applyCoupon(@RequestParam String apply, @RequestParam String code, @RequestParam double orderValue, @RequestHeader("Authorization") String jwt) {
        User user = userService.findUserByJwtToken(jwt);
        Cart cart;

        if (apply.equals("true")) {
            cart = couponService.applyCoupon(code, orderValue, user);
        } else {
            cart = couponService.removeCoupon(code, user);
        }

        return ResponseEntity.ok(ApiResponseDto.success("Coupon processed successfully", cart));
    }

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