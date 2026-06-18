package org.riston.ecommerce.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Summary DTO containing essential user information")
public record UserSummaryDto(
        @Schema(description = "Unique user ID", example = "1")
        Long id,

        @Schema(description = "User email address", example = "user@example.com")
        String email,

        @Schema(description = "Full name of the user", example = "John Doe")
        String fullName,

        @Schema(description = "Registered mobile number", example = "9876543210")
        String mobile,

        @Schema(description = "User role in the system", example = "CUSTOMER")
        String role
) {}