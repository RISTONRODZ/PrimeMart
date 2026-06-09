package org.riston.ecommerce.config;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

@Configuration
public class SecurityBeanConfig {

    @Bean
    public SecretKey secretKey(@Value("${app.jwt.secret}") String secret) {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
}