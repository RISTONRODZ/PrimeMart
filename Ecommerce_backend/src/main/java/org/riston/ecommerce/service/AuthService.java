package org.riston.ecommerce.service;

import org.riston.ecommerce.domain.USER_ROLE;
import org.riston.ecommerce.model.Seller;
import org.riston.ecommerce.model.SignupRequest;
import org.riston.ecommerce.request.LoginRequestDto;
import org.riston.ecommerce.response.AuthResponseDto;

public interface AuthService {
    void sendVerificationOtp(String email, USER_ROLE role);

    AuthResponseDto registerUser(SignupRequest req);

    AuthResponseDto loginUser(LoginRequestDto res);

    Seller registerSeller(Seller seller);
}
