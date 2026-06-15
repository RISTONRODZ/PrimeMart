package org.riston.ecommerce.controller;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.riston.ecommerce.domain.AccountStatus;
import org.riston.ecommerce.exception.InvalidOtpException;
import org.riston.ecommerce.model.Seller;
import org.riston.ecommerce.model.SellerReport;
import org.riston.ecommerce.model.VerificationCode;
import org.riston.ecommerce.repository.VerificationCodeRepository;
import org.riston.ecommerce.request.LoginRequestDto;
import org.riston.ecommerce.response.AuthResponseDto;
import org.riston.ecommerce.response.SellerReportResponse;
import org.riston.ecommerce.response.SellerResponseDto;
import org.riston.ecommerce.service.AuthService;
import org.riston.ecommerce.service.SellerReportService;
import org.riston.ecommerce.service.SellerService;
import org.riston.ecommerce.service.impl.EmailServiceImpl;
import org.riston.ecommerce.util.OtpUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/seller")
public class SellerController {
    private final SellerService sellerService;
    private final VerificationCodeRepository verificationCodeRepository;
    private final AuthService authService;
    private final EmailServiceImpl emailServiceImpl;
    private final SellerReportService sellerReportService;

    @Value("${app.frontend.url}")
    private String frontendBaseUrl;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> loginSeller(@RequestBody LoginRequestDto req) {
        String email = req.email();
        LoginRequestDto modifiedReq = new LoginRequestDto("seller_" + email, req.otp());
        AuthResponseDto authResponse = authService.loginUser(modifiedReq);
        return ResponseEntity.ok(authResponse);
    }
    @PatchMapping("/verify/{otp}")
    public ResponseEntity<SellerResponseDto> verifySellerEmail(@PathVariable String otp) {
        VerificationCode verificationCode = verificationCodeRepository.findByOtp(otp);
        if (verificationCode == null) {
            throw new InvalidOtpException("Invalid Verification Code");
        }
        if (verificationCode.getExpiryDate().isBefore(LocalDateTime.now())) {
            verificationCodeRepository.delete(verificationCode);
            throw new InvalidOtpException("Verification code has expired");
        }

        Seller seller = sellerService.verifyEmail(verificationCode.getEmail(), otp);
        verificationCodeRepository.delete(verificationCode);

        SellerResponseDto response = new SellerResponseDto(
                seller.getId(),
                seller.getSellerName(),
                seller.getEmail(),
                seller.getMobile(),
                seller.getGSTIN(),
                seller.getAccountStatus(),
                seller.getEmailVerified()
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<SellerResponseDto> createSeller(@RequestBody Seller seller) {
        Seller savedSeller = sellerService.createSeller(seller);
        String otp = OtpUtil.generateOtp();
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setOtp(otp);
        verificationCode.setEmail(seller.getEmail());
        verificationCode.setExpiryDate(LocalDateTime.now().plusMinutes(15));
        verificationCodeRepository.save(verificationCode);

        String subject = "Welcome to PrimeMart - Verify Your Seller Account";
        String text = getString(seller, otp);

        emailServiceImpl.sendVerificationOtpEmail(seller.getEmail(), subject, text);

        SellerResponseDto response = new SellerResponseDto(
                savedSeller.getId(),
                savedSeller.getSellerName(),
                savedSeller.getEmail(),
                savedSeller.getMobile(),
                savedSeller.getGSTIN(),
                savedSeller.getAccountStatus(),
                savedSeller.getEmailVerified()
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    private @NonNull String getString(Seller seller, String otp) {
        String verificationUrl = frontendBaseUrl + "/verify-seller?otp=" + otp;
        return "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e5e7eb; border-radius: 10px;'>" +
                "<div style='text-align:center; margin-bottom:20px;'><h1 style='color:#111827; margin:0;'>PrimeMart</h1><p style='color:#6b7280;'>Seller Registration</p></div>" +
                "<h2 style='color:#111827;'>Welcome, " + seller.getSellerName() + " 👋</h2>" +
                "<p style='font-size:16px; color:#4b5563; line-height:1.6;'>Thank you for registering as a seller on <strong>PrimeMart</strong>. To activate your account, please verify your email.</p>" +
                "<div style='text-align:center; margin:35px 0;'><a href='" + verificationUrl + "' style='background:#4F46E5; color:white; text-decoration:none; padding:14px 28px; border-radius:8px; font-size:16px; font-weight:bold;'>Verify My Account</a></div>" +
                "<p style='font-size:14px; color:#6b7280;'>If the button doesn't work, copy and paste the following link:</p>" +
                "<p style='word-break:break-all; color:#4F46E5;'>" + verificationUrl + "</p>" +
                "</div>";
    }


    @GetMapping("/{id}")
    public ResponseEntity<SellerResponseDto> getSellerById(@PathVariable Long id) {
        Seller s = sellerService.getSellerById(id);
        return ResponseEntity.ok(new SellerResponseDto(s.getId(), s.getSellerName(), s.getEmail(), s.getMobile(), s.getGSTIN(), s.getAccountStatus(), s.getEmailVerified()));
    }

    @GetMapping("/profile")
    public ResponseEntity<SellerResponseDto> getSellerByJwt(@RequestHeader("Authorization") String jwt) {
        Seller s = sellerService.getSellerProfile(jwt);
        return ResponseEntity.ok(new SellerResponseDto(s.getId(), s.getSellerName(), s.getEmail(), s.getMobile(), s.getGSTIN(), s.getAccountStatus(), s.getEmailVerified()));
    }

    @GetMapping("/report")
    public ResponseEntity<SellerReportResponse> getSellerReport(@RequestHeader("Authorization") String jwt) {
        Seller seller = sellerService.getSellerProfile(jwt);
        SellerReport report = sellerReportService.getSellerReport(seller);
        return ResponseEntity.ok(new SellerReportResponse(report));
    }

    @GetMapping
    public ResponseEntity<List<SellerResponseDto>> getAllSeller(@RequestParam(required = false) AccountStatus status) {
        List<SellerResponseDto> responses = sellerService.getAllSellers(status).stream()
                .map(s -> new SellerResponseDto(s.getId(), s.getSellerName(), s.getEmail(), s.getMobile(), s.getGSTIN(), s.getAccountStatus(), s.getEmailVerified()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @PatchMapping
    public ResponseEntity<SellerResponseDto> updateSeller(@RequestHeader("Authorization") String jwt, @RequestBody Seller seller) {
        Seller profile = sellerService.getSellerProfile(jwt);
        Seller updated = sellerService.updateSeller(profile.getId(), seller);
        return ResponseEntity.ok(new SellerResponseDto(updated.getId(), updated.getSellerName(), updated.getEmail(), updated.getMobile(), updated.getGSTIN(), updated.getAccountStatus(), updated.getEmailVerified()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeller(@PathVariable Long id) {
        sellerService.deleteSeller(id);
        return ResponseEntity.noContent().build();
    }
}