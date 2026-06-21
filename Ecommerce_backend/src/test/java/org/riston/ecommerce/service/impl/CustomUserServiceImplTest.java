package org.riston.ecommerce.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.riston.ecommerce.domain.USER_ROLE;
import org.riston.ecommerce.model.Seller;
import org.riston.ecommerce.model.User;
import org.riston.ecommerce.repository.SellerRepository;
import org.riston.ecommerce.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUserServiceImplTest {

    @Mock private UserRepository userRepository;
    @Mock private SellerRepository sellerRepository;
    @InjectMocks private CustomUserServiceImpl customUserService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(customUserService, "adminEmail", "admin@test.com");
    }

    @Test
    @DisplayName("Should return UserDetails when Seller exists")
    void loadUserByUsername_SellerFound() {
        Seller seller = new Seller();
        seller.setEmail("test@seller.com");
        seller.setPassword("pass");
        seller.setRole(USER_ROLE.ROLE_SELLER);
        when(sellerRepository.findByEmail("test@seller.com")).thenReturn(seller);

        UserDetails user = customUserService.loadUserByUsername("seller_test@seller.com");
        assertEquals("test@seller.com", user.getUsername());
    }

    @Test
    @DisplayName("Should return ROLE_ADMIN when email matches adminEmail")
    void loadUserByUsername_UserIsAdmin() {
        User user = new User();
        user.setEmail("admin@test.com");
        user.setPassword("pass");
        user.setRole(USER_ROLE.ROLE_CUSTOMER);
        when(userRepository.findByEmail("admin@test.com")).thenReturn(user);

        UserDetails result = customUserService.loadUserByUsername("admin@test.com");
        assertTrue(result.getAuthorities().stream().anyMatch(a -> Objects.equals(a.getAuthority(), "ROLE_ADMIN")));
    }

    @Test
    @DisplayName("Should throw IllegalStateException when role is null")
    void loadUserByUsername_RoleIsNull() {
        User user = new User();
        user.setEmail("user@test.com");
        user.setRole(null);
        when(userRepository.findByEmail("user@test.com")).thenReturn(user);

        assertThrows(IllegalStateException.class, () -> customUserService.loadUserByUsername("user@test.com"));
    }

    @Test
    @DisplayName("Should handle case-insensitive Admin email check")
    void loadUserByUsername_AdminEmailCaseInsensitive() {
        User user = new User();
        user.setEmail("ADMIN@test.com");
        user.setRole(USER_ROLE.ROLE_CUSTOMER);
        when(userRepository.findByEmail("ADMIN@test.com")).thenReturn(user);

        UserDetails result = customUserService.loadUserByUsername("ADMIN@test.com");
        assertEquals("ROLE_ADMIN", result.getAuthorities().iterator().next().getAuthority());
    }

    @Test
    @DisplayName("Should throw UsernameNotFoundException for empty prefix")
    void loadUserByUsername_OnlyPrefixProvided() {
        when(sellerRepository.findByEmail("")).thenReturn(null);
        assertThrows(UsernameNotFoundException.class, () -> customUserService.loadUserByUsername("seller_"));
    }

    @Test
    @DisplayName("Should throw UsernameNotFoundException when user does not exist")
    void loadUserByUsername_NotFound() {
        when(userRepository.findByEmail("ghost@test.com")).thenReturn(null);
        assertThrows(UsernameNotFoundException.class, () -> customUserService.loadUserByUsername("ghost@test.com"));
    }
}