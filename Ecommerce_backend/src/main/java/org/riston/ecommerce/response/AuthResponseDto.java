package org.riston.ecommerce.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.riston.ecommerce.domain.USER_ROLE;

@Schema(description = "Response payload after successful authentication")
public record AuthResponseDto(
        @Schema(description = "The JWT access token", example = "eyJhbGciOiJIUzI1Ni...")
        String jwt,

        @Schema(description = "Status message", example = "Login successful")
        String message,

        @Schema(description = "The role assigned to the user", example = "CUSTOMER")
        USER_ROLE role
) {}