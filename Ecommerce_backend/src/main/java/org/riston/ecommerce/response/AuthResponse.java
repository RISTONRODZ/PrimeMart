package org.riston.ecommerce.response;

import lombok.Data;
import org.riston.ecommerce.domain.USER_ROLE;
@Data
public class AuthResponse {
    private  String jwt;
    private String message;
    private USER_ROLE role;
}
