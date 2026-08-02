package org.riston.ecommerce.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.riston.ecommerce.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mail.MailSendException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    private static final String BREVO_API_URL = "https://api.brevo.com/v3/smtp/email";

    private final RestTemplate restTemplate;

    @Value("${app.mail.from}")
    private String fromEmail;

    @Value("${app.mail.from-name}")
    private String fromName;

    @Value("${brevo.api.key}")
    private String brevoApiKey;

    public EmailServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendVerificationOtpEmail(String userEmail, String subject, String text) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("api-key", brevoApiKey);
            headers.set("accept", "application/json");

            Map<String, Object> sender = new HashMap<>();
            sender.put("email", fromEmail);
            sender.put("name", fromName);

            Map<String, Object> recipient = new HashMap<>();
            recipient.put("email", userEmail);

            Map<String, Object> body = new HashMap<>();
            body.put("sender", sender);
            body.put("to", new Object[]{recipient});
            body.put("subject", subject);
            body.put("htmlContent", text);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

            var response = restTemplate.postForEntity(BREVO_API_URL, request, String.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                log.error("Brevo API returned non-success status: {}", response.getStatusCode());
                throw new MailSendException("failed to send email: unexpected status " + response.getStatusCode());
            }

            log.info("Email sent successfully to {} via Brevo API", userEmail);
        } catch (RestClientException e) {
            log.error("Exception caught while sending email via Brevo API: ", e);
            throw new MailSendException("failed to send email", e);
        }
    }
}