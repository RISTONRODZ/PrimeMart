package org.riston.ecommerce.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.riston.ecommerce.model.VerificationCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class VerificationCodeRepositoryTest {

    @SuppressWarnings("resource")
    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16")
                    .withDatabaseName("testdb")
                    .withUsername("test")
                    .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("Should find verification code by email")
    void shouldFindVerificationCodeByEmail() {
        VerificationCode code = new VerificationCode();
        code.setEmail("user@example.com");
        code.setOtp("123456");
        entityManager.persist(code);
        entityManager.flush();
        entityManager.clear();

        VerificationCode found = verificationCodeRepository.findByEmail("user@example.com");

        assertNotNull(found);
        assertEquals("123456", found.getOtp());
        assertEquals(code.getId(), found.getId());
    }

    @Test
    @DisplayName("Should return null when finding verification code by non-existent email")
    void shouldReturnNullWhenEmailDoesNotExist() {
        VerificationCode found = verificationCodeRepository.findByEmail("notfound@example.com");
        assertNull(found);
    }

    @Test
    @DisplayName("Should find verification code by OTP string")
    void shouldFindVerificationCodeByOtp() {
        VerificationCode code = new VerificationCode();
        code.setEmail("otpuser@example.com");
        code.setOtp("987654");
        entityManager.persist(code);
        entityManager.flush();
        entityManager.clear();

        VerificationCode found = verificationCodeRepository.findByOtp("987654");

        assertNotNull(found);
        assertEquals("otpuser@example.com", found.getEmail());
        assertEquals(code.getId(), found.getId());
    }

    @Test
    @DisplayName("Should return null when looking up an invalid OTP string")
    void shouldReturnNullWhenOtpDoesNotExist() {
        VerificationCode found = verificationCodeRepository.findByOtp("000000");
        assertNull(found);
    }
    @Test
    @DisplayName("Should handle empty string lookups gracefully and return null")
    void shouldReturnNullWhenSearchingForEmptyStrings() {
        VerificationCode foundEmail = verificationCodeRepository.findByEmail("");
        VerificationCode foundOtp = verificationCodeRepository.findByOtp("   ");

        assertNull(foundEmail);
        assertNull(foundOtp);
    }
}