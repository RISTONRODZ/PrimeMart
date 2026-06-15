package org.riston.ecommerce.request;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.riston.ecommerce.domain.USER_ROLE;

@Data
public class LoginOtpRequestDto {
    @Email(message = "please enter a valid email")
    @NotBlank(message = "Email cannot be blank")
    private String email;
    @NotBlank(message = "OTP cannot be blank")
    @Pattern(regexp = "^\\d{6}$", message = "OTP must be exactly 6 digits")
    private String otp;
    @NotNull(message = "Role must be specified")
    private USER_ROLE role;
}
