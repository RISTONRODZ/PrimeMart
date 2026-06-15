package org.riston.ecommerce.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.riston.ecommerce.model.SignupRequest;
import org.riston.ecommerce.request.LoginOtpRequestDto;
import org.riston.ecommerce.request.LoginRequestDto;
import org.riston.ecommerce.response.ApiResponseDto;
import org.riston.ecommerce.response.AuthResponseDto;
import org.riston.ecommerce.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponseDto> createUserHandler(@RequestBody SignupRequest request) {
        log.info("Processing signup request for email: {}", request.getEmail());

        AuthResponseDto res = authService.registerUser(request);

        return ResponseEntity.ok(res);
    }

    @PostMapping("/send-otp")
    public ResponseEntity<ApiResponseDto<String>> sendOtpHandler(@RequestBody LoginOtpRequestDto request) {
        log.info("Processing OTP request for email: {}", request.email());
        authService.sendVerificationOtp(request.email(), request.role());
        return ResponseEntity.ok(ApiResponseDto.success("Otp sent successfully", null));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> loginHandler(@RequestBody LoginRequestDto request) {
        AuthResponseDto authResponse = authService.loginUser(request);
        return ResponseEntity.ok(authResponse);
    }
}