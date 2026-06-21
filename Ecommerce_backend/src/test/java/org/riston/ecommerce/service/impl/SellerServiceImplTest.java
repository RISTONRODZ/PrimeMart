package org.riston.ecommerce.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.riston.ecommerce.domain.AccountStatus;
import org.riston.ecommerce.model.Seller;
import org.riston.ecommerce.repository.SellerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SellerServiceImplTest {

    @Mock
    private SellerRepository sellerRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private SellerServiceImpl sellerService;

    private Seller testSeller;

    @BeforeEach
    void setUp() {
        testSeller = new Seller();
        testSeller.setId(1L);
        testSeller.setEmail("test@seller.com");
        testSeller.setPassword("password123");
        testSeller.setSellerName("Test Seller");
    }

    @Test
    @DisplayName("Should create a new seller successfully")
    void createSeller_Success() {
        when(sellerRepository.findByEmail(anyString())).thenReturn(null);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(sellerRepository.save(any(Seller.class))).thenReturn(testSeller);

        Seller created = sellerService.createSeller(testSeller);

        assertNotNull(created);
        verify(sellerRepository, times(1)).save(any(Seller.class));
        verify(passwordEncoder, times(1)).encode("password123");
    }

    @Test
    @DisplayName("Should throw exception when email already exists")
    void createSeller_EmailExists() {
        when(sellerRepository.findByEmail(anyString())).thenReturn(testSeller);

        assertThrows(RuntimeException.class, () -> sellerService.createSeller(testSeller));
    }

    @Test
    @DisplayName("Should return seller by ID")
    void getSellerById_Success() {
        when(sellerRepository.findById(1L)).thenReturn(Optional.of(testSeller));

        Seller found = sellerService.getSellerById(1L);

        assertEquals("test@seller.com", found.getEmail());
    }

    @Test
    @DisplayName("Should update seller status successfully")
    void updateSellerAccountStatus_Success() {
        when(sellerRepository.findById(1L)).thenReturn(Optional.of(testSeller));
        when(sellerRepository.save(any(Seller.class))).thenReturn(testSeller);

        Seller updated = sellerService.updateSellerAccountStatus(1L, AccountStatus.ACTIVE);

        assertEquals(AccountStatus.ACTIVE, updated.getAccountStatus());
        verify(sellerRepository).save(testSeller);
    }
}