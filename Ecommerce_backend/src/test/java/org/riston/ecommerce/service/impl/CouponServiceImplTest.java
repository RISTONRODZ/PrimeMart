package org.riston.ecommerce.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.riston.ecommerce.domain.USER_ROLE;
import org.riston.ecommerce.exception.CouponAlreadyUsedException;
import org.riston.ecommerce.exception.CouponNotFoundException;
import org.riston.ecommerce.exception.InvalidCouponException;
import org.riston.ecommerce.mapper.CouponMapper;
import org.riston.ecommerce.model.Cart;
import org.riston.ecommerce.model.Coupon;
import org.riston.ecommerce.model.User;
import org.riston.ecommerce.repository.CartRepository;
import org.riston.ecommerce.repository.CouponRepository;
import org.riston.ecommerce.repository.UserRepository;
import org.riston.ecommerce.response.CouponResponseDto;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CouponServiceImplTest {

    @Mock
    private CouponRepository couponRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private UserRepository userRepository;
    @Spy
    private CouponMapper couponMapper;
    @InjectMocks
    private CouponServiceImpl couponService;

    @Test
    @DisplayName("Should successfully apply coupon and update cart total")
    void applyCoupon_Success() {
        String code = "SAVE10";
        double orderValue = 1234.5;
        User user = createValidUser();
        Coupon coupon = createValidCoupon();

        Cart cart = new Cart();
        cart.setTotalSellingPrice(2000L);
        cart.setCouponCode(code);

        when(couponRepository.findByCode(code)).thenReturn(coupon);
        when(cartRepository.findByUserId(1L)).thenReturn(cart);
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        Cart resultCart = couponService.applyCoupon(code, orderValue, user);

        assertNotNull(resultCart);
        assertEquals(1800.0, resultCart.getTotalSellingPrice());
        assertEquals("SAVE10", resultCart.getCouponCode());
        assertTrue(user.getUsedCoupons().contains(coupon));

        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("Should throw CouponNotFoundException when coupon code does not exist")
    void applyCoupon_WhenCouponNotFound_ThrowsException() {
        String code = "SAVE10";
        double orderValue = 1234.5;
        User user = new User();
        when(couponRepository.findByCode(code)).thenReturn(null);

        CouponNotFoundException exception = assertThrows(
                CouponNotFoundException.class,
                () -> couponService.applyCoupon(code, orderValue, user)
        );

        assertEquals("coupon not valid", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should check if the coupon was already used")
    void applyCoupon_WhenCouponAlreadyUsed_ThrowsException() {
        String code = "SAVE10";
        double orderValue = 1234.5;
        User user = createValidUser();
        Coupon coupon = createValidCoupon();
        user.getUsedCoupons().add(coupon);

        when(couponRepository.findByCode(code)).thenReturn(coupon);

        CouponAlreadyUsedException couponAlreadyUsedException = assertThrows(
                CouponAlreadyUsedException.class,
                () -> couponService.applyCoupon(code, orderValue, user)
        );

        assertEquals("coupon already used", couponAlreadyUsedException.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw Invalid coupon exception when order value lower than minimum")
    void applyCoupon_WhenLowerThanMinimumOrderValue_ThrowsException() {
        String code = "SAVE10";
        double orderValue = 199;
        User user = createValidUser();
        Coupon coupon = createValidCoupon();
        when(couponRepository.findByCode(code)).thenReturn(coupon);

        InvalidCouponException invalidCouponException = assertThrows(
                InvalidCouponException.class,
                () -> couponService.applyCoupon(code, orderValue, user)
        );

        assertEquals("valid for minimum order value 200.0", invalidCouponException.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw an Exception if an expired coupon is used")
    void applyCoupon_WhenExpired_ThrowsException() {
        String code = "SAVE10";
        double orderValue = 1234.5;
        User user = createValidUser();
        Coupon coupon = createValidCoupon();
        coupon.setValidityEndDate(LocalDate.now().plusDays(-1));
        when(couponRepository.findByCode(code)).thenReturn(coupon);

        Exception exception = assertThrows(
                InvalidCouponException.class,
                () -> couponService.applyCoupon(code, orderValue, user)
        );

        assertEquals("coupon expired or not active", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw InvalidCouponException when coupon is explicitly marked inactive")
    void applyCoupon_WhenNotActive_ThrowsException() {
        String code = "SAVE10";
        double orderValue = 1234.5;
        User user = createValidUser();
        Coupon coupon = createValidCoupon();
        coupon.setIsActive(false);

        when(couponRepository.findByCode(code)).thenReturn(coupon);

        Exception exception = assertThrows(InvalidCouponException.class,
                () -> couponService.applyCoupon(code, orderValue, user));

        assertEquals("coupon expired or not active", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw InvalidCouponException when current date is before validity start date")
    void applyCoupon_WhenBeforeValidityStart_ThrowsException() {
        String code = "SAVE10";
        double orderValue = 1234.5;
        User user = createValidUser();
        Coupon coupon = createValidCoupon();
        coupon.setValidityStartDate(LocalDate.now().plusDays(1));

        when(couponRepository.findByCode(code)).thenReturn(coupon);

        Exception exception = assertThrows(InvalidCouponException.class,
                () -> couponService.applyCoupon(code, orderValue, user));

        assertEquals("coupon expired or not active", exception.getMessage());
    }

    @Test
    @DisplayName("Should successfully remove coupon and revert cart total price")
    void removeCoupon_Success() {
        String code = "SAVE10";
        User user = createValidUser();
        Coupon coupon = createValidCoupon();
        Cart cart = new Cart();
        cart.setTotalSellingPrice(1800L);
        cart.setCouponCode(code);

        when(couponRepository.findByCode(code)).thenReturn(coupon);
        when(cartRepository.findByUserId(user.getId())).thenReturn(cart);
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Cart resultCart = couponService.removeCoupon(code, user);

        assertNotNull(resultCart);
        assertEquals(1980.0, resultCart.getTotalSellingPrice());
        assertNull(resultCart.getCouponCode());
    }

    @Test
    @DisplayName("Should throw CouponNotFoundException when removing a non-existent coupon")
    void removeCoupon_WhenCouponNotFound_ThrowsException() {
        String code = "INVALID";
        User user = createValidUser();

        when(couponRepository.findByCode(code)).thenReturn(null);

        CouponNotFoundException exception = assertThrows(CouponNotFoundException.class,
                () -> couponService.removeCoupon(code, user));

        assertEquals("coupon not found...", exception.getMessage());
    }

    @Test
    @DisplayName("Should find coupon by ID successfully")
    void findCouponById_Success() {
        Long id = 1L;
        Coupon coupon = createValidCoupon();
        when(couponRepository.findById(id)).thenReturn(Optional.of(coupon));

        Coupon result = couponService.findCouponById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    @DisplayName("Should throw CouponNotFoundException when coupon ID does not exist")
    void findCouponById_NotFound_ThrowsException() {
        Long id = 99L;
        when(couponRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(CouponNotFoundException.class, () -> couponService.findCouponById(id));
    }

    @Test
    @DisplayName("Should create and save a coupon successfully")
    void createCoupon_Success() {
        Coupon coupon = createValidCoupon();
        when(couponRepository.save(coupon)).thenReturn(coupon);

        Coupon savedCoupon = couponService.createCoupon(coupon);

        assertNotNull(savedCoupon);
        verify(couponRepository, times(1)).save(coupon);
    }

    @Test
    @DisplayName("Should find all coupons and map them to DTOs")
    void findAllCoupons_Success() {
        Coupon coupon = createValidCoupon();
        CouponResponseDto dto = new CouponResponseDto(
                1L,
                "SAVE10",
                "10.0",
                LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(1),
                200.0,
                true
        );

        when(couponRepository.findAll()).thenReturn(List.of(coupon));
        when(couponMapper.toDto(coupon)).thenReturn(dto);

        List<CouponResponseDto> result = couponService.findAllCoupons();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(couponMapper, times(1)).toDto(coupon);
    }

    @Test
    @DisplayName("Should delete coupon and un-assign it from all carts")
    void deleteCoupon_Success() {
        Long id = 1L;
        Coupon coupon = createValidCoupon();
        coupon.setCode("SAVE10");

        when(couponRepository.findById(id)).thenReturn(Optional.of(coupon));

        couponService.deleteCoupon(id);

        verify(cartRepository, times(1)).removeCouponFromAllCarts("SAVE10");
        verify(couponRepository, times(1)).deleteById(id);
    }

    private User createValidUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("xyz@gmail.com");
        user.setRole(USER_ROLE.ROLE_CUSTOMER);
        user.setUsedCoupons(new HashSet<>());
        return user;
    }

    private Coupon createValidCoupon() {
        Coupon coupon = new Coupon();
        coupon.setId(1L);
        coupon.setMinimumOrderValue(200);
        coupon.setIsActive(true);
        coupon.setDiscountPercentage("10.0");
        coupon.setValidityStartDate(LocalDate.now().minusDays(1));
        coupon.setValidityEndDate(LocalDate.now().plusDays(1));
        return coupon;
    }
}