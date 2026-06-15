package org.riston.ecommerce.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.riston.ecommerce.model.SignupRequest;
import org.riston.ecommerce.request.LoginOtpRequest;
import org.riston.ecommerce.request.LoginRequest;
import org.riston.ecommerce.response.ApiResponse;
import org.riston.ecommerce.response.AuthResponse;
import org.riston.ecommerce.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody SignupRequest request) {
        log.info("Processing signup request for email: {}", request.getEmail());

        AuthResponse res = authService.registerUser(request);

        return ResponseEntity.ok(res);
    }

    @PostMapping("/send-otp")
    public ResponseEntity<ApiResponse<String>> sendOtpHandler(@RequestBody LoginOtpRequest request) {
        log.info("Processing OTP request for email: {}", request.getEmail());
        authService.sendVerificationOtp(request.getEmail(), request.getRole());
        return ResponseEntity.ok(ApiResponse.success("Otp sent successfully", null));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginHandler(@RequestBody LoginRequest request) {
        AuthResponse authResponse = authService.loginUser(request);
        return ResponseEntity.ok(authResponse);
    }
}