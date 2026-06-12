package org.riston.ecommerce.service.impl;

import io.github.bucket4j.Bucket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.riston.ecommerce.config.JwtProvider;
import org.riston.ecommerce.domain.USER_ROLE;
import org.riston.ecommerce.exception.SellerNotFoundException;
import org.riston.ecommerce.model.*;
import org.riston.ecommerce.repository.SellerRepository;
import org.riston.ecommerce.request.LoginRequest;
import org.riston.ecommerce.response.AuthResponse;
import org.riston.ecommerce.repository.CartRepository;
import org.riston.ecommerce.repository.UserRepository;
import org.riston.ecommerce.repository.VerificationCodeRepository;
import org.riston.ecommerce.service.AuthService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.riston.ecommerce.util.OtpUtil;

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
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;
    private final JwtProvider jwtProvider;
    private final VerificationCodeRepository verificationCodeRepository;
    private final EmailServiceImpl emailServiceImpl;
    private final CustomUserServiceImpl customUserService;
    private final SellerRepository sellerRepository;
    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    private Bucket createNewBucket() {
        return Bucket.builder()
                .addLimit(limit -> limit.capacity(3).refillIntervally(1, Duration.ofMinutes(1)))
                .build();
    }

    @Override
    public void sendVerificationOtp(String email, USER_ROLE role) {
        Bucket bucket = cache.computeIfAbsent(email, k -> createNewBucket());

        if (!bucket.tryConsume(1)) {
            throw new IllegalStateException("Too many OTP requests. Please wait a minute before trying again.");
        }

        String SIGNIN_PREFIX = "signin_";
        if (email.startsWith(SIGNIN_PREFIX)) {
            email = email.substring(SIGNIN_PREFIX.length());
            if (role.equals(USER_ROLE.ROLE_SELLER)) {
                Seller seller = sellerRepository.findByEmail(email);
                if (seller == null) {
                    throw new SellerNotFoundException("seller not found with the provided email");
                }
            } else {
                log.info("email: {}", email);
                User user = userRepository.findByEmail(email);
                if (user == null) {
                    throw new IllegalArgumentException("User doesn't exist for the provided email");
                }
            }
        }
        VerificationCode exists = verificationCodeRepository.findByEmail(email);
        if (exists != null) {
            verificationCodeRepository.delete(exists);
        }
        String otp = OtpUtil.generateOtp();
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setOtp(otp);
        verificationCode.setEmail(email);
        verificationCode.setExpiryDate(LocalDateTime.now().plusMinutes(15));
        verificationCodeRepository.save(verificationCode);

        String subject = "PrimeMart Verification Code";
        String text = "<div style='font-family: Arial, sans-serif; max-width: 500px; margin: 0 auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 8px;'>" +
                "  <h2 style='color: #111827; text-align: center;'>Verify Your Account</h2>" +
                "  <p style='font-size: 16px; color: #4B5563; line-height: 1.5;'>Please use the following One-Time Password (OTP) to complete your action. This code is valid for 5 minutes:</p>" +
                "  <div style='text-align: center; margin: 30px 0;'>" +
                "    <span style='font-size: 32px; font-weight: bold; letter-spacing: 5px; color: #4F46E5; background-color: #F3F4F6; padding: 12px 30px; border-radius: 6px; border: 1px dashed #4F46E5;'>" +
                otp +
                "    </span>" +
                "  </div>" +
                "  <p style='font-size: 12px; color: #9CA3AF; text-align: center; margin-top: 40px;'>If you did not request this code, please ignore this email.</p>" +
                "</div>";
        emailServiceImpl.sendVerificationOtpEmail(email, subject, text);
    }

    @Override
    public AuthResponse registerUser(SignupRequest req) {
        VerificationCode verificationCode = verificationCodeRepository.findByEmail(req.getEmail());

        if (verificationCode == null) {
            throw new IllegalArgumentException("invalid otp");
        }

        if (LocalDateTime.now().isAfter(verificationCode.getExpiryDate())) {
            verificationCodeRepository.delete(verificationCode);
            throw new IllegalArgumentException("invalid otp");
        }

        if (!verificationCode.getOtp().equals(req.getOtp())) {
            throw new IllegalArgumentException("invalid otp");
        }

        verificationCodeRepository.delete(verificationCode);

        User user = userRepository.findByEmail(req.getEmail());
        if (user == null) {
            User createdUser = new User();
            createdUser.setEmail(req.getEmail());
            createdUser.setFullName(req.getFullName());
            createdUser.setMobile("8786543456");
            createdUser.setPassword(passwordEncoder.encode(req.getOtp()));
            user = userRepository.save(createdUser);

            Cart cart = new Cart();
            cart.setUser(user);
            cartRepository.save(cart);
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(USER_ROLE.ROLE_CUSTOMER.toString()));

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(req.getEmail(), null, authorities);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateToken(authentication);

        AuthResponse res = new AuthResponse();
        res.setJwt(jwt);
        res.setMessage("register success");
        res.setRole(USER_ROLE.ROLE_CUSTOMER);

        return res;
    }

    @Override
    public AuthResponse loginUser(LoginRequest req) {
        String email = req.getEmail();
        String otp = req.getOtp();
        Authentication authentication = authenticate(email, otp);
        String token = jwtProvider.generateToken(authentication);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("Login success");
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String roleName = authorities.isEmpty() ? null : authorities.iterator().next().getAuthority();
        authResponse.setRole(USER_ROLE.valueOf(roleName));
        return authResponse;
    }

    private Authentication authenticate(String email, String otp) {
        UserDetails userDetails;
        try {
            userDetails = customUserService.loadUserByUsername(email);
        } catch (org.springframework.security.core.userdetails.UsernameNotFoundException e) {
            throw new BadCredentialsException("DEBUG: Account matching identifier '" + email + "' was not found.");
        }

        VerificationCode verificationCode = verificationCodeRepository.findByEmail(
                email.startsWith("seller_") ? email.substring("seller_".length()) : email
        );

        if (verificationCode == null) {
            throw new BadCredentialsException("wrong otp");
        }

        if (LocalDateTime.now().isAfter(verificationCode.getExpiryDate())) {
            verificationCodeRepository.delete(verificationCode);
            throw new BadCredentialsException("wrong otp");
        }

        if (!verificationCode.getOtp().equals(otp)) {
            throw new BadCredentialsException("wrong otp");
        }

//        verificationCodeRepository.delete(verificationCode);

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}