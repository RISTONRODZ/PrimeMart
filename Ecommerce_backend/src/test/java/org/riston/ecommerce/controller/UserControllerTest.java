package org.riston.ecommerce.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.riston.ecommerce.model.User;
import org.riston.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public static MethodValidationPostProcessor methodValidationPostProcessor() {
            return new MethodValidationPostProcessor();
        }
    }

    @Test
    @DisplayName("GET /api/v1/users/profile - Success (200)")
    void getUserHandler_ShouldReturnUserProfile_WhenTokenIsValid() throws Exception {
        String mockJwt = "Bearer mock.jwt.token";

        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("dev@example.com");
        mockUser.setFullName("John Doe");
        mockUser.setMobile("1234567890");
        mockUser.setAddresses(Collections.emptySet());

        when(userService.findUserByJwtToken(mockJwt)).thenReturn(mockUser);

        mockMvc.perform(get("/api/v1/users/profile")
                        .header(HttpHeaders.AUTHORIZATION, mockJwt)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("User profile retrieved successfully"))
                .andExpect(jsonPath("$.data.email").value("dev@example.com"))
                .andExpect(jsonPath("$.data.fullName").value("John Doe"));

        verify(userService).findUserByJwtToken(mockJwt);
    }

    @Test
    @DisplayName("GET /api/v1/users/profile - Missing Auth Header (400)")
    void getUserHandler_ShouldReturnBadRequest_WhenAuthorizationHeaderIsMissing() throws Exception {
        mockMvc.perform(get("/api/v1/users/profile")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(userService, Mockito.never()).findUserByJwtToken(anyString());
    }

    @Test
    @DisplayName("GET /api/v1/users/profile - Invalid Token (401 Exception Mapping)")
    void getUserHandler_ShouldReturnUnauthorized_WhenTokenIsInvalidOrExpired() throws Exception {
        String invalidJwt = "Bearer invalid.jwt.token";

        when(userService.findUserByJwtToken(invalidJwt))
                .thenThrow(new BadCredentialsException("User not found or token expired"));

        mockMvc.perform(get("/api/v1/users/profile")
                        .header(HttpHeaders.AUTHORIZATION, invalidJwt)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("User not found or token expired"));

        verify(userService).findUserByJwtToken(invalidJwt);
    }

    @Test
    @DisplayName("GET /api/v1/users/profile - Empty Token String (400)")
    void getUserHandler_ShouldReturnBadRequest_WhenTokenIsEmpty() throws Exception {
        mockMvc.perform(get("/api/v1/users/profile")
                        .header(HttpHeaders.AUTHORIZATION, "")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }
}