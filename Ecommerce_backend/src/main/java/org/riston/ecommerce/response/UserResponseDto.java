package org.riston.ecommerce.response;

import org.riston.ecommerce.model.User;

import java.util.List;

public record UserResponseDto(
        Long id,
        String email,
        String fullName,
        String mobile,
        String role,
        List<AddressDto> addresses
) {
    public record AddressDto(
            Long id,
            String address,
            String city,
            String state,
            String pinCode,
            String mobileNumber
    ) {}
    
    public static UserResponseDto fromEntity(User user) {
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