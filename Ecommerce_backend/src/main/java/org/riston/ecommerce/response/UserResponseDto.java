package org.riston.ecommerce.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.riston.ecommerce.model.User;
import java.util.List;

@Schema(description = "Response body for user account details")
public record UserResponseDto(
        @Schema(description = "Unique user ID", example = "1")
        Long id,

        @Schema(description = "User email address", example = "user@example.com")
        String email,

        @Schema(description = "Full name of the user", example = "John Doe")
        String fullName,

        @Schema(description = "Registered mobile number", example = "9876543210")
        String mobile,

        @Schema(description = "User role in the system", example = "CUSTOMER")
        String role,

        @Schema(description = "List of addresses saved by the user")
        List<AddressDto> addresses
) {
    @Schema(description = "Address details of the user")
    public record AddressDto(
            @Schema(description = "Address ID", example = "101")
            Long id,
            @Schema(description = "Street address", example = "123, Street Name")
            String address,
            @Schema(description = "City name", example = "Pune")
            String city,
            @Schema(description = "State name", example = "Maharashtra")
            String state,
            @Schema(description = "Postal code", example = "411001")
            String pinCode,
            @Schema(description = "Contact number for this address", example = "9876543210")
            String mobileNumber
    ) {}

    public static UserResponseDto fromEntity(User user) {
        if (user == null) return null;

        List<AddressDto> addressDtos = user.getAddresses() != null ? user.getAddresses().stream()
                .map(addr -> new AddressDto(
                        addr.getId(),
                        addr.getAddress(),
                        addr.getCity(),
                        addr.getState(),
                        addr.getPinCode(),
                        addr.getMobileNumber()
                )).toList() : List.of();

        return new UserResponseDto(
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                user.getMobile(),
                user.getRole() != null ? user.getRole().name() : null,
                addressDtos
        );
    }
}