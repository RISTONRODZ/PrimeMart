package org.riston.ecommerce.config;

import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

@Configuration
@Log4j2
public class SecurityBeanConfig {

    @Bean
    public SecretKey secretKey(@Value("${app.jwt.secret}") String secret) {
        log.info("Initializing SecretKey. Length of provided secret: {}", secret.length());

        if (secret.length() < 32) {
            log.error("CRITICAL: JWT Secret is too short! Expected >= 32, got {}", secret.length());
        }
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
}