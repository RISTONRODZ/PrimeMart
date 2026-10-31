package org.riston.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/system")
public class SystemHealthController {

    @Autowired
    private JavaMailSender mailSender;

    @PostMapping("/smtp-ping")
    public ResponseEntity<String> sendSmtpKeepAlive(
            @RequestHeader(value = "X-Cron-Secret", required = false) String cronSecret) {

        // Read secret securely from system environment
        String expectedSecret = System.getenv("CRON_SECRET_KEY");

        if (expectedSecret == null || !expectedSecret.equals(cronSecret)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo("ristontechnology@gmail.com");
            message.setSubject("PrimeMart - Brevo SMTP Keep-Alive");
            message.setText("Automated monthly heartbeat to keep Brevo SMTP active.");

            mailSender.send(message);
            return ResponseEntity.ok("SMTP ping email sent successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send email: " + e.getMessage());
        }
    }
}
