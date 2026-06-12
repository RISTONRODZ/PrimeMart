package org.riston.ecommerce.service;

public interface EmailService {
    void sendVerificationOtpEmail(String userEmail, String subject, String text);
}
