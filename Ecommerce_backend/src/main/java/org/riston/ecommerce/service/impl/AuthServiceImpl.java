package org.riston.ecommerce.service.impl;

import io.github.bucket4j.Bucket;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.riston.ecommerce.config.JwtProvider;
import org.riston.ecommerce.domain.USER_ROLE;
import org.riston.ecommerce.exception.SellerNotFoundException;
import org.riston.ecommerce.model.*;
import org.riston.ecommerce.repository.CartRepository;
import org.riston.ecommerce.repository.SellerRepository;
import org.riston.ecommerce.repository.UserRepository;
import org.riston.ecommerce.repository.VerificationCodeRepository;
import org.riston.ecommerce.request.LoginRequestDto;
import org.riston.ecommerce.response.AuthResponseDto;
import org.riston.ecommerce.service.AuthService;
import org.riston.ecommerce.service.SellerService;
import org.riston.ecommerce.util.OtpUtil;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private static final String SIGNIN_PREFIX = "signin_";
    private static final String SELLER_PREFIX = "seller_";
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;
    private final JwtProvider jwtProvider;
    private final VerificationCodeRepository verificationCodeRepository;
    private final EmailServiceImpl emailServiceImpl;
    private final CustomUserServiceImpl customUserService;
    private final SellerRepository sellerRepository;
    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();
    private final SellerService sellerService;

    private Bucket createNewBucket() {
        return Bucket.builder().addLimit(limit -> limit.capacity(3).refillIntervally(1, Duration.ofMinutes(1))).build();
    }

    private String cleanEmailAddress(String email) {
        if (email == null) return null;
        if (email.startsWith(SIGNIN_PREFIX)) {
            return email.substring(SIGNIN_PREFIX.length());
        } else if (email.startsWith(SELLER_PREFIX)) {
            return email.substring(SELLER_PREFIX.length());
        }
        return email;
    }

    @Override
    @Transactional
    public void sendVerificationOtp(String email, USER_ROLE role) {
        Bucket bucket = cache.computeIfAbsent(email, k -> createNewBucket());

        if (!bucket.tryConsume(1)) {
            throw new IllegalStateException("Too many OTP requests. Please wait a minute before trying again.");
        }

        String cleanEmail = cleanEmailAddress(email);

        if (role.equals(USER_ROLE.ROLE_SELLER)) {
            Seller seller = sellerRepository.findByEmail(cleanEmail);
            if (seller == null) {
                throw new SellerNotFoundException("seller not found with the provided email");
            }
        } else {
            User user = userRepository.findByEmail(cleanEmail);
            if (user == null) {
                throw new IllegalArgumentException("User doesn't exist for the provided email");
            }
        }

        VerificationCode exists = verificationCodeRepository.findByEmail(cleanEmail);
        if (exists != null) {
            verificationCodeRepository.delete(exists);
        }

        String otp = OtpUtil.generateOtp();
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setOtp(otp);
        verificationCode.setEmail(cleanEmail);
        verificationCode.setRole(role);
        verificationCode.setExpiryDate(LocalDateTime.now().plusMinutes(15));
        verificationCodeRepository.save(verificationCode);

        String subject = "PrimeMart | Your Verification Code";

        String text = """
                <div style="font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; max-width: 500px; margin: 0 auto; padding: 30px; border: 1px solid #e4e4e7; border-radius: 12px; box-shadow: 0 4px 6px -1px rgba(0,0,0,0.05);">
                    <div style="text-align: center; margin-bottom: 24px;">
                        <h2 style="color: #0f172a; margin: 0; font-size: 24px; font-weight: 700; letter-spacing: -0.5px;">PrimeMart</h2>
                    </div>
                    <p style="color: #334155; font-size: 16px; line-height: 1.5; margin: 0 0 20px 0;">Hello,</p>
                    <p style="color: #334155; font-size: 15px; line-height: 1.5; margin: 0 0 24px 0;">Use the verification code below to securely sign in to your PrimeMart account. This code is valid for 15 minutes.</p>
                    <div style="text-align: center; margin: 32px 0;">
                        <span style="display: inline-block; font-size: 36px; font-weight: 700; letter-spacing: 6px; color: #2563eb; background-color: #eff6ff; padding: 12px 32px; border-radius: 8px; border: 1px solid #bfdbfe;">
                            %s
                        </span>
                    </div>
                
                    <p style="color: #64748b; font-size: 13px; line-height: 1.5; margin: 0 0 24px 0;">If you didn't request this code, you can safely ignore this email. Someone might have typed your email address by mistake.</p>
                    <hr style="border: none; border-top: 1px solid #e2e8f0; margin: 24px 0;">
                    <p style="color: #94a3b8; font-size: 12px; text-align: center; margin: 0;">&copy; 2026 PrimeMart. All rights reserved.</p>
                </div>
                """.formatted(otp);
        emailServiceImpl.sendVerificationOtpEmail(cleanEmail, subject, text);
    }

    @Override
    public AuthResponseDto registerUser(SignupRequest req) {
        VerificationCode verificationCode = verificationCodeRepository.findByEmail(req.getEmail());

        if (verificationCode == null || !verificationCode.getOtp().equals(req.getOtp())) {
            throw new IllegalArgumentException("Invalid OTP");
        }
        if (LocalDateTime.now().isAfter(verificationCode.getExpiryDate())) {
            verificationCodeRepository.delete(verificationCode);
            throw new IllegalArgumentException("OTP has expired");
        }
        verificationCodeRepository.delete(verificationCode);
        User existingUser = userRepository.findByEmail(req.getEmail());
        if (existingUser != null) {
            throw new IllegalArgumentException("User already exists with this email");
        }

        User createdUser = new User();
        createdUser.setEmail(req.getEmail());
        createdUser.setFullName(req.getFullName());
        createdUser.setMobile("8786543456");
        createdUser.setPassword(passwordEncoder.encode(req.getOtp()));
        createdUser.setRole(USER_ROLE.ROLE_CUSTOMER);
        User user = userRepository.save(createdUser);

        Cart cart = new Cart();
        cart.setUser(user);
        cartRepository.save(cart);

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(USER_ROLE.ROLE_CUSTOMER.toString()));

        Authentication authentication = new UsernamePasswordAuthenticationToken(req.getEmail(), null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateToken(authentication);

        return new AuthResponseDto(jwt, "Register success", USER_ROLE.ROLE_CUSTOMER);
    }

    @Override
    public AuthResponseDto loginUser(LoginRequestDto req) {
        Authentication authentication = authenticate(req.email(), req.otp());
        String token = jwtProvider.generateToken(authentication);
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String roleName = authorities.isEmpty() ? null : authorities.iterator().next().getAuthority();
        USER_ROLE userRole = roleName != null ? USER_ROLE.valueOf(roleName) : null;

        return new AuthResponseDto(token, "Login success", userRole);
    }

    private Authentication authenticate(String email, String otp) {
        String cleanEmail = cleanEmailAddress(email);
        String lookupUsername = (email != null && email.startsWith(SELLER_PREFIX)) ? email : cleanEmail;

        UserDetails userDetails;
        try {
            userDetails = customUserService.loadUserByUsername(lookupUsername);
        } catch (org.springframework.security.core.userdetails.UsernameNotFoundException e) {
            throw new BadCredentialsException("Account not found");
        }

        VerificationCode verificationCode = verificationCodeRepository.findByEmail(cleanEmail);

        if (verificationCode == null || !verificationCode.getOtp().equals(otp)) {
            throw new BadCredentialsException("wrong otp");
        }

        if (LocalDateTime.now().isAfter(verificationCode.getExpiryDate())) {
            verificationCodeRepository.delete(verificationCode);
            throw new BadCredentialsException("wrong otp");
        }

        USER_ROLE role = verificationCode.getRole();
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role.toString()));

//        verificationCodeRepository.delete(verificationCode);

        return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
    }

    @Override
    @Transactional
    public Seller registerSeller(Seller seller) {
        return sellerService.createSeller(seller);
    }
}