package org.riston.ecommerce.response;

import org.riston.ecommerce.domain.AccountStatus;

public record SellerStatusResponse(
        Long id,
        String sellerName,
        String businessEmail,
        AccountStatus accountStatus
) {}