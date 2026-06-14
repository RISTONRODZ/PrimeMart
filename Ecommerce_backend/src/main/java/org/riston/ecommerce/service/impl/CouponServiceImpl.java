package org.riston.ecommerce.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.riston.ecommerce.exception.*;
import org.riston.ecommerce.model.*;
import org.riston.ecommerce.repository.*;
import org.riston.ecommerce.service.CouponService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {
    private final CouponRepository couponRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    @Override
    public Cart applyCoupon(String code, double orderValue, User user) {
        Coupon coupon = couponRepository.findByCode(code);
        Cart cart = cartRepository.findByUserId(user.getId());

        if (coupon == null) {
            throw new CouponNotFoundException("coupon not valid");
        }
        if (user.getUsedCoupons().contains(coupon)) {
            throw new CouponAlreadyUsedException("coupon already used");
        }
        if (orderValue < coupon.getMinimumOrderValue()) {
            throw new InvalidCouponException("valid for minimum order value " + coupon.getMinimumOrderValue());
        }

        if (coupon.isActive() &&
                java.time.LocalDate.now().isAfter(coupon.getValidityStartDate()) &&
                java.time.LocalDate.now().isBefore(coupon.getValidityEndDate())) {

            user.getUsedCoupons().add(coupon);
            userRepository.save(user);
            double discountPercentage = Double.parseDouble(coupon.getDiscountPercentage());
            double discountedPrice = (cart.getTotalSellingPrice() * discountPercentage) / 100;
            cart.setTotalSellingPrice(cart.getTotalSellingPrice() - discountedPrice);
            cart.setCouponCode(code);
            cartRepository.save(cart);

            return cart;
        }

        throw new InvalidCouponException("coupon expired or not active");
    }

    @Override
    public Cart removeCoupon(String code, User user) {
        Coupon coupon = couponRepository.findByCode(code);
        if (coupon == null) {
            throw new CouponNotFoundException("coupon not found...");
        }
        Cart cart = cartRepository.findByUserId(user.getId());

        double percentValue = Double.parseDouble(coupon.getDiscountPercentage());
        double discountedPrice = cart.getTotalSellingPrice() * (percentValue / 100.0);
        cart.setTotalSellingPrice(cart.getTotalSellingPrice() + discountedPrice);
        cart.setCouponCode(null);

        return cartRepository.save(cart);
    }

    @Override
    public Coupon findCouponById(Long id) {
        return couponRepository.findById(id)
                .orElseThrow(() -> new CouponNotFoundException("Coupon not found"));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Coupon createCoupon(Coupon coupon) {
        return couponRepository.save(coupon);
    }

    @Override
    public List<Coupon> findAllCoupons() {
        return couponRepository.findAll();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteCoupon(Long id) {
        Coupon coupon = findCouponById(id);
        cartRepository.removeCouponFromAllCarts(coupon.getCode());
        couponRepository.deleteById(id);
    }
}