package org.riston.ecommerce.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.riston.ecommerce.domain.AccountStatus;
import org.riston.ecommerce.model.Seller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SellerRepositoryTest {

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
    private SellerRepository sellerRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("Should find a seller by email address")
    void shouldFindSellerByEmail() {
        Seller seller = new Seller();
        seller.setSellerName("Super Mart");
        seller.setEmail("supermart@test.com");
        seller.setAccountStatus(AccountStatus.ACTIVE);

        entityManager.persist(seller);
        entityManager.flush();
        entityManager.clear();

        Seller found = sellerRepository.findByEmail("supermart@test.com");

        assertNotNull(found);
        assertEquals("Super Mart", found.getSellerName());
        assertEquals(seller.getId(), found.getId());
    }

    @Test
    @DisplayName("Should return null when finding a seller by a non-existent email")
    void shouldReturnNullWhenEmailDoesNotExist() {
        Seller found = sellerRepository.findByEmail("missing@test.com");
        assertNull(found);
    }

    @Test
    @DisplayName("Should find all sellers matching a specific account status")
    void shouldFindSellersByAccountStatus() {
        Seller seller1 = new Seller();
        seller1.setSellerName("Seller One");
        seller1.setEmail("one@test.com");
        seller1.setAccountStatus(AccountStatus.PENDING_VERIFICATION);
        entityManager.persist(seller1);

        Seller seller2 = new Seller();
        seller2.setSellerName("Seller Two");
        seller2.setEmail("two@test.com");
        seller2.setAccountStatus(AccountStatus.ACTIVE);
        entityManager.persist(seller2);

        Seller seller3 = new Seller();
        seller3.setSellerName("Seller Three");
        seller3.setEmail("three@test.com");
        seller3.setAccountStatus(AccountStatus.PENDING_VERIFICATION);
        entityManager.persist(seller3);

        entityManager.flush();
        entityManager.clear();

        List<Seller> pendingSellers = sellerRepository.findByAccountStatus(AccountStatus.PENDING_VERIFICATION);

        assertNotNull(pendingSellers);
        assertEquals(2, pendingSellers.size());
    }

    @Test
    @DisplayName("Should return empty list when no sellers match the requested account status")
    void shouldReturnEmptyListWhenNoSellersMatchStatus() {
        List<Seller> suspendedSellers = sellerRepository.findByAccountStatus(AccountStatus.SUSPENDED);
        assertNotNull(suspendedSellers);
        assertTrue(suspendedSellers.isEmpty());
    }
}