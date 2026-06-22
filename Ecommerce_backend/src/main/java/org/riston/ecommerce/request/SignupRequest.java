package org.riston.ecommerce.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SignupRequest(
        @Email(message = "please enter a valid email")
        @NotBlank(message = "Email cannot be blank")
        String email,

        @NotBlank(message = "Full name cannot be blank")
        String fullName,

        @NotBlank(message = "OTP cannot be blank")
        @Pattern(regexp = "^\\d{6}$", message = "OTP must be exactly 6 digits")
        String otp
) {}