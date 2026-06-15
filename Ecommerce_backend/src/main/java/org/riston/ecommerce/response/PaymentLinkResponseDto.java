package org.riston.ecommerce.response;

import lombok.Data;

@Data
public class PaymentLinkResponseDto {
    private String payment_link_url;
    private String payment_link_id;
}
