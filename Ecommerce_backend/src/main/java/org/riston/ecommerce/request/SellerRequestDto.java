package org.riston.ecommerce.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SellerRequestDto(
        @NotBlank(message = "Seller name cannot be blank")
        @Size(min = 2, max = 100, message = "Seller name must be between 2 and 100 characters")
        String sellerName,

        @Email(message = "please enter a valid email")
        @NotBlank(message = "Email cannot be blank")
        String email,

        @NotBlank(message = "Password cannot be blank")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        String password,

        @NotBlank(message = "Mobile number cannot be blank")
        @Pattern(regexp = "^(\\+91)?\\d{10}$", message = "Mobile number must be 10 digits, with an optional +91 country code")
        String mobile,

        @NotBlank(message = "GSTIN cannot be blank")
        @Pattern(regexp = "^[0-9A-Z]{15}$", message = "GSTIN must be exactly 15 alphanumeric uppercase characters")
        String gstin,

        // Business Details (optional - can be updated later)
        BusinessDetailsDto businessDetails,

        // Bank Details (optional - can be updated later)
        BankDetailsDto bankDetails,

        // Pickup Address (optional - can be updated later)
        AddressDto pickupAddress
) {
    public record BusinessDetailsDto(
            String businessName,
            @Email(message = "please enter a valid business email")
            String businessEmail,
            String businessMobile,
            String businessAddress,
            String logo,
            String banner
    ) {}

    public record BankDetailsDto(
            String accountNumber,
            String accountHolderName,
            String ifscCode
    ) {}

    public record AddressDto(
            String name,
            String locality,
            String address,
            String city,
            String state,
            String pinCode,
            String mobileNumber
    ) {}
}