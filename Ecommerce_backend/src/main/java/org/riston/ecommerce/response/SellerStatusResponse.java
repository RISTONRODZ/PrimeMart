package org.riston.ecommerce.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.riston.ecommerce.domain.AccountStatus;

@Schema(description = "Response payload for seller status lookup")
public record SellerStatusResponse(
        @Schema(example = "1") Long id,
        @Schema(example = "Nike Official Store") String sellerName,
        @Schema(example = "seller@nike.com") String businessEmail,
        @Schema(example = "ACTIVE") AccountStatus accountStatus
) {}