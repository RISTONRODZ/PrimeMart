package org.riston.ecommerce.controller;

import lombok.RequiredArgsConstructor;
import org.riston.ecommerce.domain.AccountStatus;
import org.riston.ecommerce.exception.InvalidOtpException;
import org.riston.ecommerce.model.Seller;
import org.riston.ecommerce.model.VerificationCode;
import org.riston.ecommerce.repository.VerificationCodeRepository;
import org.riston.ecommerce.request.LoginRequest;
import org.riston.ecommerce.response.AuthResponse;
import org.riston.ecommerce.service.AuthService;
import org.riston.ecommerce.service.impl.EmailServiceImpl;
import org.riston.ecommerce.service.SellerService;
import org.riston.ecommerce.util.OtpUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/seller")
public class SellerController {
    private final SellerService sellerService;
    private final VerificationCodeRepository verificationCodeRepository;
    private final AuthService authService;
    private final EmailServiceImpl emailServiceImpl;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginSeller(@RequestBody LoginRequest req) {
        String email = req.getEmail();
        req.setEmail("seller_" + email);
        AuthResponse authResponse = authService.loginUser(req);
        return ResponseEntity.ok(authResponse);

    }

    @PatchMapping("/verify/{otp}")
    public ResponseEntity<Seller> verifySellerEmail(@PathVariable String otp) {
        VerificationCode verificationCode = verificationCodeRepository.findByOtp(otp);
        if (verificationCode == null) {
            throw new InvalidOtpException("Invalid Verification Code");
        }
        Seller seller = sellerService.verifyEmail(verificationCode.getEmail(), otp);
        return new ResponseEntity<>(seller, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Seller> createSeller(@RequestBody Seller seller) {
        Seller savedSeller = sellerService.createSeller(seller);
        String otp = OtpUtil.generateOtp();
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setOtp(otp);
        verificationCode.setEmail(seller.getEmail());
        verificationCode.setExpiryDate(LocalDateTime.now().plusMinutes(15));
        verificationCodeRepository.save(verificationCode);
        String subject = "Welcome to PrimeMart - Verify Your Seller Account";

        String verificationUrl =
                "http://localhost:3000/verify-seller?otp=" + otp;

        String text =
                "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; " +
                        "padding: 20px; border: 1px solid #e5e7eb; border-radius: 10px;'>" +

                        "<div style='text-align:center; margin-bottom:20px;'>" +
                        "<h1 style='color:#111827; margin:0;'>PrimeMart</h1>" +
                        "<p style='color:#6b7280;'>Seller Registration</p>" +
                        "</div>" +

                        "<h2 style='color:#111827;'>Welcome, " + seller.getSellerName() + " 👋</h2>" +

                        "<p style='font-size:16px; color:#4b5563; line-height:1.6;'>" +
                        "Thank you for registering as a seller on <strong>PrimeMart</strong>. " +
                        "To activate your seller account and start listing products, please verify your email address." +
                        "</p>" +

                        "<div style='text-align:center; margin:35px 0;'>" +
                        "<a href='" + verificationUrl + "' " +
                        "style='background:#4F46E5; color:white; text-decoration:none; " +
                        "padding:14px 28px; border-radius:8px; font-size:16px; font-weight:bold;'>" +
                        "Verify My Account" +
                        "</a>" +
                        "</div>" +

                        "<p style='font-size:14px; color:#6b7280;'>" +
                        "If the button above doesn't work, copy and paste the following link into your browser:" +
                        "</p>" +

                        "<p style='word-break:break-all; color:#4F46E5;'>" +
                        verificationUrl +
                        "</p>" +

                        "<div style='background:#F9FAFB; padding:15px; border-radius:8px; margin-top:25px;'>" +
                        "<p style='margin:0; color:#374151;'>" +
                        "<strong>Verification Code:</strong> " + otp +
                        "</p>" +
                        "<p style='margin-top:8px; color:#6b7280; font-size:13px;'>" +
                        "This verification link and code will expire in 15 minutes." +
                        "</p>" +
                        "</div>" +

                        "<hr style='border:none; border-top:1px solid #e5e7eb; margin:30px 0;'/>" +

                        "<p style='font-size:12px; color:#9CA3AF; text-align:center;'>" +
                        "If you did not create a seller account on PrimeMart, please ignore this email." +
                        "</p>" +

                        "</div>";

        emailServiceImpl.sendVerificationOtpEmail(
                seller.getEmail(),
                subject,
                text
        );
        return new ResponseEntity<>(savedSeller, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Seller> getSellerById(@PathVariable Long id) {
        Seller seller = sellerService.getSellerById(id);
        return new ResponseEntity<>(seller, HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<Seller> getSellerByJwt(
            @RequestHeader("Authorization") String jwt) {
        Seller seller = sellerService.getSellerProfile(jwt);
        return new ResponseEntity<>(seller, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Seller>> getAllSeller(
            @RequestParam(required = false) AccountStatus status
    ) {
        List<Seller> sellers = sellerService.getAllSellers(status);
        return ResponseEntity.ok(sellers);
    }

    @PatchMapping
    public ResponseEntity<Seller> updateSeller(
            @RequestHeader("Authorization") String jwt, @RequestBody Seller seller
    ) {
        Seller profile = sellerService.getSellerProfile(jwt);
        Seller updatedSeller = sellerService.updateSeller(profile.getId(), seller);
        return ResponseEntity.ok(updatedSeller);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeller(@PathVariable Long id) {
        sellerService.deleteSeller(id);
        return ResponseEntity.noContent().build();
    }


}
