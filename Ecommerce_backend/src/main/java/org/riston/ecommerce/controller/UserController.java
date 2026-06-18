package org.riston.ecommerce.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.riston.ecommerce.annotation.ApiNotFoundResponse;
import org.riston.ecommerce.model.User;
import org.riston.ecommerce.response.ApiResponseDto;
import org.riston.ecommerce.response.UserResponseDto;
import org.riston.ecommerce.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(
    name = "User Management",
    description = "Endpoints for managing user profiles and account information"
)
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    @Operation(
        summary = "Get user profile",
        description = "Retrieves the authenticated user's profile information including email, full name, mobile number, and saved addresses"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "User profile retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))
        ),

    })
    @ApiNotFoundResponse
    public ResponseEntity<ApiResponseDto<UserResponseDto>> getUserHandler(
        @Parameter(
            description = "JWT token obtained from login endpoint",
            required = true,
            example = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
        )
        @RequestHeader("Authorization") String jwt
    ) {
        User user = userService.findUserByJwtToken(jwt);
        UserResponseDto profileData = UserResponseDto.fromEntity(user);
        return ResponseEntity.ok(ApiResponseDto.success("User profile retrieved successfully", profileData));
    }
}