package org.riston.ecommerce.request;

import jakarta.validation.constraints.*;

public record LoginOtpRequestDto(
        @Email(message = "please enter a valid email")
        @NotBlank(message = "Email cannot be blank")
        String email
) {}