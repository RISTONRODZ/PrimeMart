package org.riston.ecommerce.mapper;
import org.riston.ecommerce.model.Coupon;
import org.riston.ecommerce.request.CouponRequest;
import org.riston.ecommerce.response.CouponResponseDto;
import org.springframework.stereotype.Component;

@Component
public class CouponMapper {

    public CouponResponseDto toDto(Coupon coupon) {
        return new CouponResponseDto(
                coupon.getId(),
                coupon.getCode(),
                coupon.getDiscountPercentage(),
                coupon.getValidityStartDate(),
                coupon.getValidityEndDate(),
                coupon.getMinimumOrderValue(),
                coupon.getIsActive()
        );
    }
    public Coupon toEntity(CouponRequest request) {
        Coupon coupon = new Coupon();
        coupon.setCode(request.code());
        coupon.setDiscountPercentage(String.valueOf(request.discountPercentage()));
        coupon.setMinimumOrderValue(request.minimumOrderValue());
        coupon.setIsActive(request.isActive());
        coupon.setValidityStartDate(request.validityStartDate().toLocalDate());
        coupon.setValidityEndDate(request.validityEndDate().toLocalDate());
        return coupon;
    }
}