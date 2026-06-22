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
import org.riston.ecommerce.domain.AccountStatus;
import org.riston.ecommerce.domain.USER_ROLE;
import org.riston.ecommerce.model.Cart;
import org.riston.ecommerce.model.Seller;
import org.riston.ecommerce.model.User;
import org.riston.ecommerce.model.VerificationCode;
import org.riston.ecommerce.repository.CartRepository;
import org.riston.ecommerce.repository.SellerRepository;
import org.riston.ecommerce.repository.UserRepository;
import org.riston.ecommerce.repository.VerificationCodeRepository;
import org.riston.ecommerce.request.LoginRequestDto;
import org.riston.ecommerce.request.SellerRequestDto;
import org.riston.ecommerce.request.SignupRequest;
import org.riston.ecommerce.response.AuthResponseDto;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
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
    @Mock
    private CustomUserServiceImpl customUserService;
    @Mock
    private SellerRepository sellerRepository;
    @Spy
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @InjectMocks
    private AuthServiceImpl authServiceImpl;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(authServiceImpl, "adminEmail", "admin@primemart.com");
    }

    @Test
    @DisplayName("should verify and send otp")
    void shouldSendVerificationOtp() {
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
    @DisplayName("should check if the old verification code is invalid")
    void sendVerificationOtp_Success_DeletesExistingCodeFirst() {
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
    @DisplayName("should check the rate limit function")
    void sendVerificationOtp_RateLimitExceeded_ThrowsException() {
        String email = "xyz@gmail.com";
        when(verificationCodeRepository.findByEmail(email)).thenReturn(null);
        authServiceImpl.sendVerificationOtp(email);
        authServiceImpl.sendVerificationOtp(email);
        authServiceImpl.sendVerificationOtp(email);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                authServiceImpl.sendVerificationOtp(email)
        );

        assertEquals("Too many OTP requests.", exception.getMessage());
        verify(verificationCodeRepository, times(3)).save(any(VerificationCode.class));
    }

    @Test
    @DisplayName("sendVerificationOtp should handle dynamic prefix cleaning")
    void sendVerificationOtp_WithPrefix_CleansEmailCorrectly() {
        String email = "signin_prefixed-email@gmail.com";
        String cleanedEmail = "prefixed-email@gmail.com";

        assertDoesNotThrow(() -> authServiceImpl.sendVerificationOtp(email));

        verify(verificationCodeRepository).findByEmail(cleanedEmail);
        verify(emailServiceImpl).sendVerificationOtpEmail(eq(cleanedEmail), anyString(), anyString());
    }

    @Test
    @DisplayName("should register a user")
    void registerUser_Success() {
        SignupRequest request = new SignupRequest("xyz@gmail.com", "xyz", "123456");

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
        SignupRequest request = new SignupRequest("xyz@gmail.com", "xyz", "wrong_otp");

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
        SignupRequest request = new SignupRequest("xyz@gmail.com", "xyz", "123456");

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
        SignupRequest request = new SignupRequest("xyz@gmail.com", "xyz", "123456");

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

    @Test
    @DisplayName("loginUser should authenticate and login customer successfully")
    void loginUser_Customer_Success() {
        LoginRequestDto request = new LoginRequestDto("customer@gmail.com", "123456");
        UserDetails mockUserDetails = mock(UserDetails.class);
        VerificationCode mockCode = new VerificationCode();
        mockCode.setEmail("customer@gmail.com");
        mockCode.setOtp("123456");
        mockCode.setExpiryDate(LocalDateTime.now().plusMinutes(15));

        when(customUserService.loadUserByUsername("customer@gmail.com")).thenReturn(mockUserDetails);
        when(verificationCodeRepository.findByEmail("customer@gmail.com")).thenReturn(mockCode);
        when(jwtProvider.generateToken(any())).thenReturn("customer-jwt");

        AuthResponseDto response = authServiceImpl.loginUser(request);

        assertNotNull(response);
        assertEquals("Login success", response.message());
        assertEquals("customer-jwt", response.jwt());
        assertEquals(USER_ROLE.ROLE_CUSTOMER, response.role());
        verify(verificationCodeRepository).delete(mockCode);
    }

    @Test
    @DisplayName("loginUser should authenticate and login seller successfully via prefix")
    void loginUser_Seller_Success() {
        LoginRequestDto request = new LoginRequestDto("seller_store@gmail.com", "123456");
        UserDetails mockUserDetails = mock(UserDetails.class);
        VerificationCode mockCode = new VerificationCode();
        mockCode.setEmail("store@gmail.com");
        mockCode.setOtp("123456");
        mockCode.setExpiryDate(LocalDateTime.now().plusMinutes(15));

        when(customUserService.loadUserByUsername("seller_store@gmail.com")).thenReturn(mockUserDetails);
        when(verificationCodeRepository.findByEmail("store@gmail.com")).thenReturn(mockCode);
        when(jwtProvider.generateToken(any())).thenReturn("seller-jwt");

        AuthResponseDto response = authServiceImpl.loginUser(request);

        assertNotNull(response);
        assertEquals(USER_ROLE.ROLE_SELLER, response.role());
        assertEquals("seller-jwt", response.jwt());
    }

    @Test
    @DisplayName("loginUser should authenticate and login admin successfully")
    void loginUser_Admin_Success() {
        LoginRequestDto request = new LoginRequestDto("admin@primemart.com", "123456");
        UserDetails mockUserDetails = mock(UserDetails.class);
        VerificationCode mockCode = new VerificationCode();
        mockCode.setEmail("admin@primemart.com");
        mockCode.setOtp("123456");
        mockCode.setExpiryDate(LocalDateTime.now().plusMinutes(15));

        when(customUserService.loadUserByUsername("admin@primemart.com")).thenReturn(mockUserDetails);
        when(verificationCodeRepository.findByEmail("admin@primemart.com")).thenReturn(mockCode);
        when(jwtProvider.generateToken(any())).thenReturn("admin-jwt");

        AuthResponseDto response = authServiceImpl.loginUser(request);

        assertNotNull(response);
        assertEquals(USER_ROLE.ROLE_ADMIN, response.role());
        assertEquals("admin-jwt", response.jwt());
    }

    @Test
    @DisplayName("loginUser should throw BadCredentialsException when verification token code is missing or mismatch")
    void loginUser_InvalidOtp_ThrowsBadCredentialsException() {
        LoginRequestDto request = new LoginRequestDto("customer@gmail.com", "wrong");
        UserDetails mockUserDetails = mock(UserDetails.class);
        VerificationCode mockCode = new VerificationCode();
        mockCode.setEmail("customer@gmail.com");
        mockCode.setOtp("123456");

        when(customUserService.loadUserByUsername("customer@gmail.com")).thenReturn(mockUserDetails);
        when(verificationCodeRepository.findByEmail("customer@gmail.com")).thenReturn(mockCode);

        BadCredentialsException exception = assertThrows(BadCredentialsException.class, () ->
                authServiceImpl.loginUser(request)
        );

        assertEquals("Invalid OTP", exception.getMessage());
    }

    @Test
    @DisplayName("loginUser should throw BadCredentialsException when verification token has expired")
    void loginUser_ExpiredOtp_ThrowsBadCredentialsException() {
        LoginRequestDto request = new LoginRequestDto("customer@gmail.com", "123456");
        UserDetails mockUserDetails = mock(UserDetails.class);
        VerificationCode mockCode = new VerificationCode();
        mockCode.setEmail("customer@gmail.com");
        mockCode.setOtp("123456");
        mockCode.setExpiryDate(LocalDateTime.now().minusMinutes(1));

        when(customUserService.loadUserByUsername("customer@gmail.com")).thenReturn(mockUserDetails);
        when(verificationCodeRepository.findByEmail("customer@gmail.com")).thenReturn(mockCode);

        BadCredentialsException exception = assertThrows(BadCredentialsException.class, () ->
                authServiceImpl.loginUser(request)
        );

        assertEquals("OTP has expired", exception.getMessage());
        verify(verificationCodeRepository).delete(mockCode);
    }

    @Test
    @DisplayName("should register a seller with pending account status status configuration verification")
    void registerSeller_Success() {
        SellerRequestDto request = new SellerRequestDto("Seller Corp", "seller@corp.com", "securePass", "9876543210", "GSTIN12345");
        when(passwordEncoder.encode("securePass")).thenReturn("encodedPass");
        when(sellerRepository.save(any(Seller.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Seller result = authServiceImpl.registerSeller(request);

        assertNotNull(result);
        assertEquals("Seller Corp", result.getSellerName());
        assertEquals("seller@corp.com", result.getEmail());
        assertEquals("encodedPass", result.getPassword());
        assertEquals("9876543210", result.getMobile());
        assertEquals("GSTIN12345", result.getGSTIN());
        assertEquals(AccountStatus.PENDING_VERIFICATION, result.getAccountStatus());
        verify(sellerRepository, times(1)).save(any(Seller.class));
    }
}
