package org.riston.ecommerce.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.riston.ecommerce.model.Product;
import org.riston.ecommerce.model.Review;
import org.riston.ecommerce.model.Seller;
import org.riston.ecommerce.model.User;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ReviewRepositoryTest {

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
    private ReviewRepository reviewRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("Should return true when a review already exists for a specific user and product")
    void shouldReturnTrueWhenReviewExistsForUserAndProduct() {
        User user = new User();
        user.setEmail("user@test.com");
        entityManager.persist(user);

        Seller seller = new Seller();
        seller.setEmail("seller@test.com");
        entityManager.persist(seller);

        Product product = new Product();
        product.setTitle("Keyboard");
        product.setSeller(seller);
        entityManager.persist(product);

        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setReviewText("Great product!");
        entityManager.persist(review);

        entityManager.flush();
        entityManager.clear();

        boolean exists = reviewRepository.existsByUserIdAndProductId(user.getId(), product.getId());

        assertTrue(exists);
    }

    @Test
    @DisplayName("Should return false when no review exists for the given user and product")
    void shouldReturnFalseWhenReviewDoesNotExist() {
        boolean exists = reviewRepository.existsByUserIdAndProductId(999L, 888L);
        assertFalse(exists);
    }

    @Test
    @DisplayName("Should find all reviews matching a specific product ID")
    void shouldFindReviewsByProductId() {
        Seller seller = new Seller();
        seller.setEmail("vendor@test.com");
        entityManager.persist(seller);

        Product product = new Product();
        product.setTitle("Mouse");
        product.setSeller(seller);
        entityManager.persist(product);

        Review r1 = new Review();
        r1.setProduct(product);
        r1.setReviewText("Excellent");
        entityManager.persist(r1);

        Review r2 = new Review();
        r2.setProduct(product);
        r2.setReviewText("Bad");
        entityManager.persist(r2);

        entityManager.flush();
        entityManager.clear();

        List<Review> reviews = reviewRepository.findByProductId(product.getId());

        assertNotNull(reviews);
        assertEquals(2, reviews.size());
    }
}