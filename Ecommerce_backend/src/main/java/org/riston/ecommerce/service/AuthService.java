package org.riston.ecommerce.service;

import org.riston.ecommerce.domain.USER_ROLE;
import org.riston.ecommerce.modal.SignupRequest;
import org.riston.ecommerce.request.LoginRequest;
import org.riston.ecommerce.response.AuthResponse;

public interface AuthService {
    void sendVerificationOtp(String email, USER_ROLE role);

    AuthResponse registerUser(SignupRequest req);

    AuthResponse loginUser(LoginRequest res);
}
