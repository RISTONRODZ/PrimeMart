package org.riston.ecommerce.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.riston.ecommerce.model.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CartRepositoryTest {

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
    private CartRepository cartRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("Given carts with an active coupon, when removing the coupon from all carts, then their coupon codes should be null")
    void shouldRemoveCouponFromAllCarts(){
        Cart cart = new Cart();
        cart.setCouponCode("SAVE10");
        cartRepository.save(cart);

        cartRepository.removeCouponFromAllCarts("SAVE10");
        entityManager.clear();

        Cart updated = cartRepository.findById(cart.getId()).orElseThrow(() -> new RuntimeException("Cart not found"));
        assertNull(updated.getCouponCode());
    }

    @Test
    @DisplayName("Given multiple carts with the same coupon, when bulk removing, then all matching carts should be updated")
    void shouldRemoveCouponFromMultipleMatchingCarts() {
        Cart cart1 = new Cart();
        cart1.setCouponCode("SAVE10");
        cartRepository.save(cart1);

        Cart cart2 = new Cart();
        cart2.setCouponCode("SAVE10");
        cartRepository.save(cart2);

        cartRepository.removeCouponFromAllCarts("SAVE10");
        entityManager.clear();

        Cart updated1 = cartRepository.findById(cart1.getId()).orElseThrow();
        Cart updated2 = cartRepository.findById(cart2.getId()).orElseThrow();

        assertNull(updated1.getCouponCode());
        assertNull(updated2.getCouponCode());
    }

    @Test
    @DisplayName("Given carts with different coupons, when bulk removing one specific code, then other coupons must remain untouched")
    void shouldNotRemoveDifferentCouponsWhenBulkRemovingSpecificCode() {
        Cart matchingCart = new Cart();
        matchingCart.setCouponCode("SAVE10");
        cartRepository.save(matchingCart);

        Cart nonMatchingCart = new Cart();
        nonMatchingCart.setCouponCode("KEEP20");
        cartRepository.save(nonMatchingCart);

        cartRepository.removeCouponFromAllCarts("SAVE10");
        entityManager.clear();

        Cart updatedMatching = cartRepository.findById(matchingCart.getId()).orElseThrow();
        Cart updatedNonMatching = cartRepository.findById(nonMatchingCart.getId()).orElseThrow();

        assertNull(updatedMatching.getCouponCode());
        assertEquals("KEEP20", updatedNonMatching.getCouponCode());
    }

    @Test
    @DisplayName("Given carts with null coupon codes, when executing coupon removal query, then no exceptions or mutations occur")
    void shouldHandleCartsWithNullCouponsGracefully() {
        Cart cart = new Cart();
        cart.setCouponCode(null);
        cartRepository.save(cart);

        cartRepository.removeCouponFromAllCarts("SAVE10");
        entityManager.clear();

        Cart updated = cartRepository.findById(cart.getId()).orElseThrow();
        assertNull(updated.getCouponCode());
    }
    @Test
    @DisplayName("Given coupon codes containing SQL wildcard tokens, query boundaries remain restricted and secure")
    void shouldHandleWildcardCharactersSecurely() {
        Cart cart1 = new Cart();
        cart1.setCouponCode("SAVE10");
        cartRepository.save(cart1);

        Cart cart2 = new Cart();
        cart2.setCouponCode("SAVE_MATCH");
        cartRepository.save(cart2);

        cartRepository.removeCouponFromAllCarts("SAVE_MATCH");
        entityManager.clear();

        Cart untouched = cartRepository.findById(cart1.getId()).orElseThrow();
        Cart updated = cartRepository.findById(cart2.getId()).orElseThrow();

        assertEquals("SAVE10", untouched.getCouponCode());
        assertNull(updated.getCouponCode());
    }
}