package org.riston.ecommerce.response;

import org.riston.ecommerce.model.Address;

public record ShippingAddressDto(Long id, String address, String city, String state, String pinCode,
                                 String mobileNumber) {

    public static ShippingAddressDto fromEntity(Address entity) {
        if (entity == null) {
            return null;
        }
        return new ShippingAddressDto(entity.getId(), entity.getAddress(), entity.getCity(), entity.getState(), entity.getPinCode(), entity.getMobileNumber());
    }
}