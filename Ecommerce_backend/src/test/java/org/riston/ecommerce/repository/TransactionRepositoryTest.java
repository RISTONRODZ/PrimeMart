package org.riston.ecommerce.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.riston.ecommerce.model.Seller;
import org.riston.ecommerce.model.Transaction;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TransactionRepositoryTest {

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
    private TransactionRepository transactionRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("Should find all transactions belonging to a specific seller ID")
    void shouldFindTransactionsBySellerId() {
        Seller seller = new Seller();
        seller.setEmail("tx-vendor@test.com");
        entityManager.persist(seller);

        Transaction t1 = new Transaction();
        t1.setSeller(seller);
        entityManager.persist(t1);

        Transaction t2 = new Transaction();
        t2.setSeller(seller);
        entityManager.persist(t2);

        entityManager.flush();
        entityManager.clear();

        List<Transaction> transactions = transactionRepository.findBySellerId(seller.getId());

        assertNotNull(transactions);
        assertEquals(2, transactions.size());
    }

    @Test
    @DisplayName("Should return empty list when seller ID has no transactions")
    void shouldReturnEmptyListWhenSellerHasNoTransactions() {
        List<Transaction> transactions = transactionRepository.findBySellerId(999L);
        assertNotNull(transactions);
        assertTrue(transactions.isEmpty());
    }
}