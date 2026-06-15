package org.riston.ecommerce.response;


public record UserSummaryDto(
        Long id,
        String email,
        String fullName,
        String mobile,
        String role
) {}