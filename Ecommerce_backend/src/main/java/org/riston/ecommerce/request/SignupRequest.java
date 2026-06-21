package org.riston.ecommerce.request;


public record SignupRequest(
        String email,
        String fullName,
        String otp
) {}