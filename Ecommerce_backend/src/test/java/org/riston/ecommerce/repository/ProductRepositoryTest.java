package org.riston.ecommerce.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.riston.ecommerce.model.Category;
import org.riston.ecommerce.model.Product;
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
public class ProductRepositoryTest {

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
    private ProductRepository productRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("Should find products by title and seller")
    void shouldFindProductByTitleAndSeller(){
        Seller seller = new Seller();
        seller.setSellerName("xuz");
        seller.setEmail("test-seller@example.com");

        Product product = new Product();
        product.setTitle("xyz");
        product.setSeller(seller);

        entityManager.persist(seller);
        entityManager.persist(product);
        entityManager.flush();
        entityManager.clear();

        Product productFound = productRepository.findByTitleAndSeller("xyz", seller);

        assertNotNull(productFound, "The product should not be null");
        assertEquals("xyz", productFound.getTitle(), "The product title should match");
        assertEquals(seller.getId(), productFound.getSeller().getId(), "The seller ID should match");
        assertEquals(product.getId(), productFound.getId(), "The found product ID should match the original product ID");
    }

    @Test
    @DisplayName("Should find all products belonging to a specific seller ID")
    void shouldFindProductsBySellerId() {
        Seller seller = new Seller();
        seller.setSellerName("Alpha Sellers");
        seller.setEmail("alpha@test.com");
        entityManager.persist(seller);

        Product p1 = new Product();
        p1.setTitle("Laptop");
        p1.setSeller(seller);
        entityManager.persist(p1);

        Product p2 = new Product();
        p2.setTitle("Mouse");
        p2.setSeller(seller);
        entityManager.persist(p2);

        entityManager.flush();
        entityManager.clear();

        List<Product> products = productRepository.findBySellerId(seller.getId());

        assertNotNull(products);
        assertEquals(2, products.size());
        assertEquals("Laptop", products.get(0).getTitle());
    }

    @Test
    @DisplayName("Should return empty list when seller ID has no products")
    void shouldReturnEmptyListWhenSellerHasNoProducts() {
        List<Product> products = productRepository.findBySellerId(999L);
        assertNotNull(products);
        assertTrue(products.isEmpty());
    }

    @Test
    @DisplayName("Should find all products mapped to a specific category ID")
    void shouldFindProductsByCategoryId() {
        Category electronics = new Category();
        electronics.setCategoryId("ELECTRONICS_01");
        electronics.setLevel(1);
        entityManager.persist(electronics);

        Seller seller = new Seller();
        seller.setSellerName("Beta Tech");
        seller.setEmail("beta@test.com");
        entityManager.persist(seller);

        Product product = new Product();
        product.setTitle("Smartphone");
        product.setCategory(electronics);
        product.setSeller(seller);
        entityManager.persist(product);

        entityManager.flush();
        entityManager.clear();

        List<Product> products = productRepository.findByCategoryId(electronics.getId());

        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals("Smartphone", products.get(0).getTitle());
    }

    @Test
    @DisplayName("Should return null when the product title exists but belongs to a different seller")
    void shouldReturnNullWhenTitleExistsButSellerMismatches() {
        Seller sellerA = new Seller();
        sellerA.setSellerName("Seller A");
        sellerA.setEmail("a@test.com");
        entityManager.persist(sellerA);

        Seller sellerB = new Seller();
        sellerB.setSellerName("Seller B");
        sellerB.setEmail("b@test.com");
        entityManager.persist(sellerB);

        Product productA = new Product();
        productA.setTitle("Shared Title");
        productA.setSeller(sellerA);
        entityManager.persist(productA);

        entityManager.flush();
        entityManager.clear();

        Product foundProduct = productRepository.findByTitleAndSeller("Shared Title", sellerB);

        assertNull(foundProduct);
    }
    @Test
    @DisplayName("Should return null when querying with mismatched title casing due to case sensitivity")
    void shouldReturnNullWhenTitleCasingMismatches() {
        Seller seller = new Seller();
        seller.setEmail("case@test.com");
        seller.setSellerName("Case Seller");
        entityManager.persist(seller);

        Product product = new Product();
        product.setTitle("PlayStation");
        product.setSeller(seller);
        entityManager.persist(product);

        entityManager.flush();
        entityManager.clear();

        Product found = productRepository.findByTitleAndSeller("playstation", seller);

        assertNull(found);
    }
}