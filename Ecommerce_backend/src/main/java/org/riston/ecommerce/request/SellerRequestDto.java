package org.riston.ecommerce.request;

public record SellerRequestDto(
        String sellerName,
        String email,
        String password,
        String mobile,
        String gstin
) {}