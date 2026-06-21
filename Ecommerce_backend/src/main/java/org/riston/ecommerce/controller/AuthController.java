package org.riston.ecommerce.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.riston.ecommerce.annotation.ApiNotFoundResponse;
import org.riston.ecommerce.model.Seller;
import org.riston.ecommerce.request.SignupRequest;
import org.riston.ecommerce.request.LoginOtpRequestDto;
import org.riston.ecommerce.request.LoginRequestDto;
import org.riston.ecommerce.request.SellerRequestDto;
import org.riston.ecommerce.response.ApiResponseDto;
import org.riston.ecommerce.response.AuthResponseDto;
import org.riston.ecommerce.response.SellerResponseDto;
import org.riston.ecommerce.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication", description = "Endpoints for user and seller registration and authentication")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    @Operation(summary = "Complete User Registration",
            description = "Finalizes registration using validated credentials.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registration successful"),
            @ApiResponse(responseCode = "400", description = "Invalid registration data")
    })
    public ResponseEntity<AuthResponseDto> createUserHandler(@RequestBody SignupRequest request) {
        log.info("Processing signup request for email: {}", request.email());
        return ResponseEntity.ok(authService.registerUser(request));
    }

    @PostMapping("/send-otp")
    @Operation(summary = "Send Authentication OTP",
            description = "Triggers an email containing a 6-digit OTP for authentication.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OTP sent successfully"),
    })
    @ApiNotFoundResponse
    public ResponseEntity<ApiResponseDto<String>> sendOtpHandler(@RequestBody LoginOtpRequestDto request) {
        log.info("Processing OTP request for email: {}", request.email());
        authService.sendVerificationOtp(request.email());
        return ResponseEntity.ok(ApiResponseDto.success("Otp sent successfully", null));
    }

    @PostMapping("/login")
    @Operation(
            summary = "Authenticate user or seller",
            description = "Authenticates by verifying email and OTP. Note: Seller accounts require a 'seller_' prefix in the email."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentication successful, returns JWT")
    })
    public ResponseEntity<AuthResponseDto> loginHandler(@RequestBody LoginRequestDto request) {
        return ResponseEntity.ok(authService.loginUser(request));
    }

    @PostMapping("/signup/seller")
    @Operation(
            summary = "Sign up a new seller account",
            description = "Registers a new seller profile."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Seller registered successfully"),
    })
    public ResponseEntity<ApiResponseDto<SellerResponseDto>> createSellerHandler(@RequestBody SellerRequestDto request) {
        Seller savedSeller = authService.registerSeller(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDto.success("Seller registered successfully.", SellerResponseDto.fromEntity(savedSeller)));
    }
}