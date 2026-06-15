package org.riston.ecommerce.response;

import org.riston.ecommerce.domain.USER_ROLE;

public record AuthResponseDto(
        String jwt,
        String message,
        USER_ROLE role
) {}