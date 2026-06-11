package org.riston.ecommerce.request;
import lombok.Data;
import org.riston.ecommerce.domain.USER_ROLE;

@Data
public class LoginOtpRequest {
    private String email;
    private String otp;
    private USER_ROLE role;
}
