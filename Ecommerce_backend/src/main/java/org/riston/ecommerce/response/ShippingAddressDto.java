package org.riston.ecommerce.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.riston.ecommerce.model.Address;

@Schema(description = "Data Transfer Object for shipping address details")
public record ShippingAddressDto(
        @Schema(description = "Unique identifier of the address", example = "1")
        Long id,

        @Schema(description = "Street address details", example = "123, MG Road")
        String address,

        @Schema(description = "City name", example = "Mumbai")
        String city,

        @Schema(description = "State name", example = "Maharashtra")
        String state,

        @Schema(description = "Postal code", example = "400001")
        String pinCode,

        @Schema(description = "Contact mobile number", example = "9876543210")
        String mobileNumber) {

    public static ShippingAddressDto fromEntity(Address entity) {
        if (entity == null) {
            return null;
        }
        return new ShippingAddressDto(
                entity.getId(),
                entity.getAddress(),
                entity.getCity(),
                entity.getState(),
                entity.getPinCode(),
                entity.getMobileNumber()
        );
    }
}