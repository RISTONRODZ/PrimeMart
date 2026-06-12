package org.riston.ecommerce.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.riston.ecommerce.service.EmailService;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender javaMailSender;

    public void sendVerificationOtpEmail(String userEmail, String subject, String text) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(
                    mimeMessage, "utf-8"
            );
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setTo(userEmail);
            mimeMessageHelper.setText(text, true);
            javaMailSender.send(mimeMessage);
        } catch (MailException | MessagingException e) {
            log.error("Exception caught while sending email: ", e);
            throw new MailSendException("failed to send email");
        }
    }
}
