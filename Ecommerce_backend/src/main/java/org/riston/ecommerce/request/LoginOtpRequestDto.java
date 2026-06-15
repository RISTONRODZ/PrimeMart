package org.riston.ecommerce.request;

import jakarta.validation.constraints.*;
import org.riston.ecommerce.domain.USER_ROLE;

public record LoginOtpRequestDto(
        @Email(message = "please enter a valid email")
        @NotBlank(message = "Email cannot be blank")
        String email,

        @NotBlank(message = "OTP cannot be blank")
        @Pattern(regexp = "^\\d{6}$", message = "OTP must be exactly 6 digits")
        String otp,

        @NotNull(message = "Role must be specified")
        USER_ROLE role
) {}