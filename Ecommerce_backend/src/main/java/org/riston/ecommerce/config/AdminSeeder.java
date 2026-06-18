package org.riston.ecommerce.config;

import lombok.extern.slf4j.Slf4j;
import org.riston.ecommerce.domain.USER_ROLE;
import org.riston.ecommerce.model.User;
import org.riston.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Configuration
public class AdminSeeder {
    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.password}")
    private String adminPassword;
    @Bean
    public CommandLineRunner seedAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByEmail(adminEmail) == null) {
                User admin = new User();
                admin.setEmail(adminEmail);
                admin.setFullName("Super Admin");
                admin.setRole(USER_ROLE.ROLE_ADMIN);
                admin.setPassword(passwordEncoder.encode(adminPassword));

                userRepository.save(admin);
                log.info("admin account initialized");
            }
        };
    }
}