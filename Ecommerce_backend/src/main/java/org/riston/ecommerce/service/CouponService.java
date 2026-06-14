package org.riston.ecommerce.service;

import org.riston.ecommerce.model.Cart;
import org.riston.ecommerce.model.Coupon;
import org.riston.ecommerce.model.User;

import java.util.List;

public interface CouponService {
    Cart applyCoupon(String code, double orderValue, User user);
    Cart removeCoupon(String code, User user);
    Coupon findCouponById(Long id);
    Coupon createCoupon(Coupon coupon);
    List<Coupon> findAllCoupons();
    void deleteCoupon(Long id);
}
