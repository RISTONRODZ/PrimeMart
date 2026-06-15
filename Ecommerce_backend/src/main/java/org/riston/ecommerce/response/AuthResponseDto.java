package org.riston.ecommerce.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor; // Add this
import org.riston.ecommerce.domain.USER_ROLE;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDto {
    private String jwt;
    private String message;
    private USER_ROLE role;
}