package org.riston.ecommerce.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.riston.ecommerce.domain.AccountStatus;
import org.riston.ecommerce.model.Seller;

@Schema(description = "Response payload representing a seller's profile details")
public record SellerResponseDto(
        @Schema(description = "Unique identifier of the seller", example = "1")
        Long id,

        @Schema(description = "Name of the seller/business", example = "Nike Official Store")
        String sellerName,

        @Schema(description = "Registered email address", example = "seller@nike.com")
        String email,

        @Schema(description = "Contact mobile number", example = "+919988776655")
        String mobile,

        @Schema(description = "Goods and Services Tax Identification Number", example = "27AAAAA0000A1Z5")
        String gstin,

        @Schema(description = "Current account status", example = "ACTIVE")
        AccountStatus accountStatus,

        @Schema(description = "Whether the seller's email is verified", example = "true")
        Boolean emailVerified,

        @Schema(description = "Business details of the seller")
        BusinessDetailsDto businessDetails,

        @Schema(description = "Pickup address for order fulfillment")
        PickupAddressDto pickupAddress
) {
    public record BusinessDetailsDto(
            @Schema(description = "Business name", example = "Nike India Pvt Ltd")
            String businessName,

            @Schema(description = "Business email address", example = "business@nike.com")
            String businessEmail,

            @Schema(description = "Business contact number", example = "+919876543210")
            String businessMobile,

            @Schema(description = "Business address", example = "123 Business Park, Mumbai")
            String businessAddress,

            @Schema(description = "Business logo URL")
            String logo,

            @Schema(description = "Business banner URL")
            String banner
    ) {}

    public record PickupAddressDto(
            @Schema(description = "Contact person name", example = "John Doe")
            String name,

            @Schema(description = "Contact mobile number", example = "+919876543210")
            String mobile,

            @Schema(description = "Postal code", example = "400001")
            String pincode,

            @Schema(description = "Street address", example = "123 Main Street")
            String address,

            @Schema(description = "Locality/Area", example = "Andheri West")
            String locality,

            @Schema(description = "City", example = "Mumbai")
            String city,

            @Schema(description = "State", example = "Maharashtra")
            String state
    ) {}

    public static SellerResponseDto fromEntity(Seller seller) {
        if (seller == null) {
            return null;
        }
        BusinessDetailsDto businessDetailsDto = null;
        if (seller.getBusinessDetails() != null) {
            businessDetailsDto = new BusinessDetailsDto(
                    seller.getBusinessDetails().getBusinessName(),
                    seller.getBusinessDetails().getBusinessEmail(),
                    seller.getBusinessDetails().getBusinessMobile(),
                    seller.getBusinessDetails().getBusinessAddress(),
                    seller.getBusinessDetails().getLogo(),
                    seller.getBusinessDetails().getBanner()
            );
        }

        PickupAddressDto pickupAddressDto = null;
        if (seller.getPickupAddress() != null) {
            pickupAddressDto = new PickupAddressDto(
                    seller.getPickupAddress().getName(),
                    seller.getPickupAddress().getMobileNumber(),
                    seller.getPickupAddress().getPinCode(),
                    seller.getPickupAddress().getAddress(),
                    seller.getPickupAddress().getLocality(),
                    seller.getPickupAddress().getCity(),
                    seller.getPickupAddress().getState()
            );
        }

        return new SellerResponseDto(
                seller.getId(),
                seller.getSellerName(),
                seller.getEmail(),
                seller.getMobile(),
                seller.getGSTIN(),
                seller.getAccountStatus(),
                seller.getEmailVerified(),
                businessDetailsDto,
                pickupAddressDto
        );
    }
}