package org.riston.ecommerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.riston.ecommerce.domain.AccountStatus;
import org.riston.ecommerce.domain.USER_ROLE;
import org.riston.ecommerce.model.Seller;
import org.riston.ecommerce.request.LoginOtpRequestDto;
import org.riston.ecommerce.request.LoginRequestDto;
import org.riston.ecommerce.request.SellerRequestDto;
import org.riston.ecommerce.request.SignupRequest;
import org.riston.ecommerce.response.AuthResponseDto;
import org.riston.ecommerce.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    private static final String BASE = "/api/v1/auth";

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private AuthService authService;

    @Nested
    @DisplayName("POST /signup")
    class SignupTests {

        @Test
        @DisplayName("returns 200 and JWT response on successful signup")
        void signup_success() throws Exception {
            SignupRequest request = new SignupRequest("john@example.com", "John Doe", "123456");
            AuthResponseDto response = new AuthResponseDto(
                    "mock.jwt.token", "Registration successful", USER_ROLE.ROLE_CUSTOMER);

            when(authService.registerUser(any(SignupRequest.class))).thenReturn(response);

            mockMvc.perform(post(BASE + "/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.jwt").value("mock.jwt.token"))
                    .andExpect(jsonPath("$.message").value("Registration successful"))
                    .andExpect(jsonPath("$.role").value("ROLE_CUSTOMER"));

            verify(authService, times(1)).registerUser(request);
            verifyNoMoreInteractions(authService);
        }

        @Test
        @DisplayName("passes the exact request payload through to the service unmodified")
        void signup_passesExactPayloadToService() throws Exception {
            SignupRequest request = new SignupRequest("jane@example.com", "Jane Roe", "654321");
            when(authService.registerUser(any())).thenReturn(
                    new AuthResponseDto("jwt", "ok", USER_ROLE.ROLE_CUSTOMER));

            mockMvc.perform(post(BASE + "/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());

            verify(authService).registerUser(eq(request));
        }

        @Test
        @DisplayName("malformed JSON falls through GlobalExceptionHandler's catch-all as 500")
        void signup_malformedJson_returns500ViaCatchAll() throws Exception {
            mockMvc.perform(post(BASE + "/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{ this is not valid json "))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.message").value(
                            org.hamcrest.Matchers.startsWith("An unexpected error occurred:")));

            verifyNoInteractions(authService);
        }

        @Test
        @DisplayName("missing/unsupported content type is rejected (exact status depends on whether GlobalExceptionHandler intercepts it)")
        void signup_missingContentType_isRejected() throws Exception {
            mockMvc.perform(post(BASE + "/signup")
                            .content(objectMapper.writeValueAsString(
                                    new SignupRequest("a@b.com", "A B", "111111"))))
                    .andExpect(result -> {
                        int status = result.getResponse().getStatus();
                        org.junit.jupiter.api.Assertions.assertTrue(status >= 400,
                                "Expected an error status (4xx or 5xx) for unsupported content type, got " + status);
                    });

            verifyNoInteractions(authService);
        }

        @Test
        @DisplayName("service exception is caught by GlobalExceptionHandler's catch-all and returns 500 with error envelope")
        void signup_serviceThrows_handledAsInternalServerError() throws Exception {
            SignupRequest request = new SignupRequest("err@example.com", "Err User", "999999");
            when(authService.registerUser(any()))
                    .thenThrow(new RuntimeException("OTP expired or invalid"));

            mockMvc.perform(post(BASE + "/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.message").value(
                            "An unexpected error occurred: OTP expired or invalid"));
        }
    }

    @Nested
    @DisplayName("POST /send-otp")
    class SendOtpTests {

        @Test
        @DisplayName("returns 200 with success envelope on valid email")
        void sendOtp_success() throws Exception {
            LoginOtpRequestDto request = new LoginOtpRequestDto("john@example.com", "000000");

            doNothing().when(authService).sendVerificationOtp("john@example.com");

            mockMvc.perform(post(BASE + "/send-otp")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("Otp sent successfully"))
                    .andExpect(jsonPath("$.data").doesNotExist());

            verify(authService, times(1)).sendVerificationOtp("john@example.com");
            verifyNoMoreInteractions(authService);
        }

        @Test
        @DisplayName("malformed JSON falls through GlobalExceptionHandler's catch-all as 500")
        void sendOtp_malformedJson_returns500ViaCatchAll() throws Exception {
            mockMvc.perform(post(BASE + "/send-otp")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("not json at all"))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.message").value(
                            org.hamcrest.Matchers.startsWith("An unexpected error occurred:")));

            verifyNoInteractions(authService);
        }

        @Test
        @DisplayName("generic service exception is handled as 500 with error envelope")
        void sendOtp_genericServiceException_returns500() throws Exception {
            LoginOtpRequestDto request = new LoginOtpRequestDto("unknown@example.com", "123456");
            doThrow(new RuntimeException("Unexpected failure sending OTP"))
                    .when(authService).sendVerificationOtp("unknown@example.com");

            mockMvc.perform(post(BASE + "/send-otp")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.message").value(
                            "An unexpected error occurred: Unexpected failure sending OTP"));
        }
    }

    @Nested
    @DisplayName("POST /login")
    class LoginTests {

        @Test
        @DisplayName("returns 200 and JWT for a valid customer login")
        void login_customer_success() throws Exception {
            LoginRequestDto request = new LoginRequestDto("john@example.com", "123456");
            AuthResponseDto response = new AuthResponseDto(
                    "customer.jwt.token", "Login successful", USER_ROLE.ROLE_CUSTOMER);

            when(authService.loginUser(request)).thenReturn(response);

            mockMvc.perform(post(BASE + "/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.jwt").value("customer.jwt.token"))
                    .andExpect(jsonPath("$.role").value("ROLE_CUSTOMER"));

            verify(authService).loginUser(request);
        }

        @Test
        @DisplayName("returns 200 and JWT for a valid seller login (seller_ prefixed email)")
        void login_seller_success() throws Exception {
            LoginRequestDto request = new LoginRequestDto("seller_nike@example.com", "654321");
            AuthResponseDto response = new AuthResponseDto(
                    "seller.jwt.token", "Login successful", USER_ROLE.ROLE_SELLER);

            when(authService.loginUser(request)).thenReturn(response);

            mockMvc.perform(post(BASE + "/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.role").value("ROLE_SELLER"));

            verify(authService).loginUser(request);
        }

        @Test
        @DisplayName("malformed JSON falls through GlobalExceptionHandler's catch-all as 500")
        void login_malformedJson_returns500ViaCatchAll() throws Exception {
            mockMvc.perform(post(BASE + "/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{ broken"))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.message").value(
                            org.hamcrest.Matchers.startsWith("An unexpected error occurred:")));

            verifyNoInteractions(authService);
        }

        @Test
        @DisplayName("BadCredentialsException from service is mapped to 403 by GlobalExceptionHandler")
        void login_badCredentials_returns403() throws Exception {
            LoginRequestDto request = new LoginRequestDto("john@example.com", "999999");
            when(authService.loginUser(request))
                    .thenThrow(new org.springframework.security.authentication.BadCredentialsException(
                            "Invalid credentials"));

            mockMvc.perform(post(BASE + "/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.message").value("Invalid credentials"));
        }

        @Test
        @DisplayName("unrecognized service exception falls through to catch-all 500")
        void login_unexpectedServiceException_returns500() throws Exception {
            LoginRequestDto request = new LoginRequestDto("john@example.com", "000000");
            when(authService.loginUser(request))
                    .thenThrow(new RuntimeException("Unexpected failure"));

            mockMvc.perform(post(BASE + "/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.message").value(
                            "An unexpected error occurred: Unexpected failure"));
        }
    }

    @Nested
    @DisplayName("POST /signup/seller")
    class SignupSellerTests {

        @Test
        @DisplayName("returns 201 with wrapped seller response on successful registration")
        void signupSeller_success() throws Exception {
            SellerRequestDto request = new SellerRequestDto(
                    "Nike Official Store", "seller_nike@example.com", "P@ssw0rd!", "+919988776655", "27AAAAA0000A1Z5");

            Seller savedSeller = new Seller();
            savedSeller.setId(1L);
            savedSeller.setSellerName("Nike Official Store");
            savedSeller.setEmail("seller_nike@example.com");
            savedSeller.setMobile("+919988776655");
            savedSeller.setGSTIN("27AAAAA0000A1Z5");
            savedSeller.setAccountStatus(AccountStatus.PENDING_VERIFICATION);
            savedSeller.setEmailVerified(false);

            when(authService.registerSeller(request)).thenReturn(savedSeller);

            mockMvc.perform(post(BASE + "/signup/seller")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("Seller registered successfully."))
                    .andExpect(jsonPath("$.data.id").value(1))
                    .andExpect(jsonPath("$.data.sellerName").value("Nike Official Store"))
                    .andExpect(jsonPath("$.data.email").value("seller_nike@example.com"))
                    .andExpect(jsonPath("$.data.mobile").value("+919988776655"))
                    .andExpect(jsonPath("$.data.gstin").value("27AAAAA0000A1Z5"))
                    .andExpect(jsonPath("$.data.accountStatus").value("PENDING_VERIFICATION"))
                    .andExpect(jsonPath("$.data.emailVerified").value(false))
                    .andExpect(jsonPath("$.data.password").doesNotExist());

            verify(authService).registerSeller(request);
        }

        @Test
        @DisplayName("malformed JSON falls through GlobalExceptionHandler's catch-all as 500")
        void signupSeller_malformedJson_returns500ViaCatchAll() throws Exception {
            mockMvc.perform(post(BASE + "/signup/seller")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{ not: valid"))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.message").value(
                            org.hamcrest.Matchers.startsWith("An unexpected error occurred:")));

            verifyNoInteractions(authService);
        }

        @Test
        @DisplayName("unrecognized service exception (e.g. duplicate email) falls through to catch-all 500")
        void signupSeller_duplicateEmail_returns500ViaCatchAll() throws Exception {
            SellerRequestDto request = new SellerRequestDto(
                    "Dupe Store", "dupe@example.com", "pass", "+910000000000", "GSTIN123");

            when(authService.registerSeller(request))
                    .thenThrow(new RuntimeException("Email already registered"));

            mockMvc.perform(post(BASE + "/signup/seller")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.message").value(
                            "An unexpected error occurred: Email already registered"));
        }
    }

    @Nested
    @DisplayName("Validation gap (documents current unvalidated behavior)")
    class ValidationGapTests {

        @Test
        @DisplayName("KNOWN GAP: /login accepts an invalid email format and still reaches the service")
        void login_invalidEmailFormat_stillReachesService() throws Exception {
            LoginRequestDto request = new LoginRequestDto("not-an-email", "123456");
            when(authService.loginUser(request)).thenReturn(
                    new AuthResponseDto("jwt", "Login successful", USER_ROLE.ROLE_CUSTOMER));

            mockMvc.perform(post(BASE + "/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());

            verify(authService).loginUser(request);
        }

        @Test
        @DisplayName("KNOWN GAP: /login accepts a malformed (non-6-digit) OTP and still reaches the service")
        void login_malformedOtp_stillReachesService() throws Exception {
            LoginRequestDto request = new LoginRequestDto("john@example.com", "12");
            when(authService.loginUser(request)).thenReturn(
                    new AuthResponseDto("jwt", "Login successful", USER_ROLE.ROLE_CUSTOMER));

            mockMvc.perform(post(BASE + "/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());

            verify(authService).loginUser(request);
        }

        @Test
        @DisplayName("KNOWN GAP: /send-otp accepts an invalid email format and still reaches the service")
        void sendOtp_invalidEmailFormat_stillReachesService() throws Exception {
            LoginOtpRequestDto request = new LoginOtpRequestDto("not-an-email", "123456");
            doNothing().when(authService).sendVerificationOtp("not-an-email");

            mockMvc.perform(post(BASE + "/send-otp")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());

            verify(authService).sendVerificationOtp("not-an-email");
        }
    }
}