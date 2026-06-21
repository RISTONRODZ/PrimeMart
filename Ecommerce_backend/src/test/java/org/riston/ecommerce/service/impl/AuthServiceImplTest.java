package org.riston.ecommerce.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.riston.ecommerce.config.JwtProvider;
import org.riston.ecommerce.model.Cart;
import org.riston.ecommerce.request.SignupRequest;
import org.riston.ecommerce.model.User;
import org.riston.ecommerce.model.VerificationCode;
import org.riston.ecommerce.repository.CartRepository;
import org.riston.ecommerce.repository.UserRepository;
import org.riston.ecommerce.repository.VerificationCodeRepository;
import org.riston.ecommerce.response.AuthResponseDto;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import java.time.LocalDateTime;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private VerificationCodeRepository verificationCodeRepository;
    @Mock
    private EmailServiceImpl emailServiceImpl;
    @Mock
    private JwtProvider jwtProvider;
    @Spy
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @InjectMocks
    private AuthServiceImpl authServiceImpl;
    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(authServiceImpl, "adminEmail", "admin@primemart.com");
    }
    @Test
    @DisplayName("this should verify and send otp")
    void shouldSendVerificationOtp(){
        String email = "fresh-email@gmail.com";
        assertDoesNotThrow(() -> authServiceImpl.sendVerificationOtp(email));

        verify(verificationCodeRepository).save(any(VerificationCode.class));

        verify(emailServiceImpl).sendVerificationOtpEmail(
                eq(email),
                eq("PrimeMart | Your Verification Code"),
                anyString()
        );
    }
    @Test
    @DisplayName("this should check if the old verification code is invalid")
    void sendVerificationOtp_Success_DeletesExistingCodeFirst(){
        String email = "old-code-email@gmail.com";
        VerificationCode oldCode = new VerificationCode();
        oldCode.setEmail(email);
        oldCode.setOtp("000000");

        when(verificationCodeRepository.findByEmail(email)).thenReturn(oldCode);

        authServiceImpl.sendVerificationOtp(email);
        verify(verificationCodeRepository).delete(oldCode);
        verify(verificationCodeRepository).save(any(VerificationCode.class));
    }

    @Test
    @DisplayName("this should check the rate limit function")
    void sendVerificationOtp_RateLimitExceeded_ThrowsException(){
        String email = "xyz@gmail.com";
        when(verificationCodeRepository.findByEmail(email)).thenReturn(null);
        authServiceImpl.sendVerificationOtp(email);
        authServiceImpl.sendVerificationOtp(email);
        authServiceImpl.sendVerificationOtp(email);

        IllegalStateException exception = assertThrows(IllegalStateException.class,() ->
            authServiceImpl.sendVerificationOtp(email)
        );

        assertEquals("Too many OTP requests.",exception.getMessage());
        verify(verificationCodeRepository, times(3)).save(any(VerificationCode.class));

    }
    @Test
    @DisplayName("this should register a user")
    void registerUser_Success(){
      SignupRequest request = new SignupRequest("xyz@gmail.com","xyz","123456");

        VerificationCode mockCode = new VerificationCode();
        mockCode.setEmail("xyz@gmail.com");
        mockCode.setOtp("123456");
        mockCode.setExpiryDate(LocalDateTime.now().plusMinutes(15));

        when(verificationCodeRepository.findByEmail("xyz@gmail.com")).thenReturn(mockCode);
        when(userRepository.findByEmail("xyz@gmail.com")).thenReturn(null);
        when(passwordEncoder.encode(anyString())).thenReturn("password");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));
        when(jwtProvider.generateToken(any())).thenReturn("mocked-jwt");

        AuthResponseDto response = authServiceImpl.registerUser(request);

        assertNotNull(response);
        assertEquals("Register success", response.message());
        assertEquals("mocked-jwt", response.jwt());

        verify(userRepository, times(1)).save(any(User.class));
        verify(cartRepository, times(1)).save(any(Cart.class));
    }
    @Test
    @DisplayName("registerUser should throw exception when OTP is invalid")
    void registerUser_InvalidOtp_ThrowsException() {
        SignupRequest request = new SignupRequest("xyz@gmail.com","xyz","wrong_otp");

        VerificationCode mockCode = new VerificationCode();
        mockCode.setEmail("xyz@gmail.com");
        mockCode.setOtp("123456");

        when(verificationCodeRepository.findByEmail("xyz@gmail.com")).thenReturn(mockCode);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            authServiceImpl.registerUser(request)
        );

        assertEquals("Invalid OTP", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("registerUser should throw exception when OTP has expired")
    void registerUser_ExpiredOtp_ThrowsException() {
        SignupRequest request = new SignupRequest("xyz@gmail.com","xyz","123456");

        VerificationCode mockCode = new VerificationCode();
        mockCode.setEmail("xyz@gmail.com");
        mockCode.setOtp("123456");
        mockCode.setExpiryDate(LocalDateTime.now().minusMinutes(1));

        when(verificationCodeRepository.findByEmail("xyz@gmail.com")).thenReturn(mockCode);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            authServiceImpl.registerUser(request)
        );

        assertEquals("OTP has expired", exception.getMessage());
        verify(verificationCodeRepository).delete(mockCode);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("registerUser should throw exception when user already exists")
    void registerUser_UserAlreadyExists_ThrowsException() {
        SignupRequest request = new SignupRequest("xyz@gmail.com","xyz","123456");

        VerificationCode mockCode = new VerificationCode();
        mockCode.setEmail("xyz@gmail.com");
        mockCode.setOtp("123456");
        mockCode.setExpiryDate(LocalDateTime.now().plusMinutes(15));

        when(verificationCodeRepository.findByEmail("xyz@gmail.com")).thenReturn(mockCode);
        when(userRepository.findByEmail("xyz@gmail.com")).thenReturn(new User());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            authServiceImpl.registerUser(request)
        );

        assertEquals("User already exists with this email", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

}
