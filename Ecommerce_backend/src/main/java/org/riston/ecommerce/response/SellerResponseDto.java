package org.riston.ecommerce.response;

import org.riston.ecommerce.domain.AccountStatus;
import org.riston.ecommerce.model.Seller;

public record SellerResponseDto(
        Long id,
        String sellerName,
        String email,
        String mobile,
        String gstin,
        AccountStatus accountStatus,
        Boolean emailVerified
) {
    public static SellerResponseDto fromEntity(Seller seller) {
        if (seller == null) {
            return null;
        }
        return new SellerResponseDto(
                seller.getId(),
                seller.getSellerName(),
                seller.getEmail(),
                seller.getMobile(),
                seller.getGSTIN(),
                seller.getAccountStatus(),
                seller.getEmailVerified()
        );
    }
}