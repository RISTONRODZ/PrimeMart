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
        Boolean emailVerified
) {
    public static SellerResponseDto fromEntity(Seller seller) {
        if (seller == null) {
            return null;
        }
        return new SellerResponseDto(
                seller.getId(),
                seller.getSellerName(),
                seller.getEmail(),
                seller.getMobile(),
                seller.getGSTIN(),
                seller.getAccountStatus(),
                seller.getEmailVerified()
        );
    }
}