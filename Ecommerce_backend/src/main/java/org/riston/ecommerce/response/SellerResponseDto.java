package org.riston.ecommerce.response;

import org.riston.ecommerce.domain.AccountStatus;

public record SellerResponseDto(
        Long id,
        String sellerName,
        String email,
        String mobile,
        String gstin,
        AccountStatus accountStatus,
        Boolean emailVerified
) {}