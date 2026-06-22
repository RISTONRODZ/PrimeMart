package org.riston.ecommerce.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.riston.ecommerce.mapper.CouponMapper;
import org.riston.ecommerce.model.Coupon;
import org.riston.ecommerce.request.CouponRequest;
import org.riston.ecommerce.response.CouponResponseDto;
import org.riston.ecommerce.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminCouponController.class)
class AdminCouponControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CouponService couponService;

    @MockitoBean
    private CouponMapper couponMapper;

    private Coupon coupon;
    private CouponResponseDto couponResponseDto;

    @BeforeEach
    void setUp() {
        coupon = new Coupon();
        coupon.setId(1L);
        coupon.setCode("SAVE20");

        couponResponseDto = createDefaultCouponDto("SAVE20");
    }

    private CouponResponseDto createDefaultCouponDto(String code) {
        return new CouponResponseDto(
                1L,
                code,
                "20",
                LocalDate.now(),
                LocalDate.now().plusDays(7),
                100.0,
                true
        );
    }

    @Test
    @DisplayName("Should successfully create a new coupon and return 201 Created")
    void createCoupon_ShouldReturnCreated() throws Exception {
        when(couponMapper.toEntity(any(CouponRequest.class))).thenReturn(coupon);
        when(couponService.createCoupon(any(Coupon.class))).thenReturn(coupon);
        when(couponMapper.toDto(any(Coupon.class))).thenReturn(couponResponseDto);

        String jsonRequest = """
                {
                    "code": "SAVE20",
                    "discountPercentage": 20,
                    "minimumOrderValue": 100.0,
                    "isActive": true,
                    "validityStartDate": "2026-06-21T20:00:00",
                    "validityEndDate": "2026-06-28T20:00:00"
                }
                """;

        mockMvc.perform(post("/api/v1/coupons/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Coupon created"))
                .andExpect(jsonPath("$.data.code").value("SAVE20"));
    }

    @Test
    @DisplayName("Should successfully delete a coupon by ID and return 200 OK")
    void deleteCoupon_ShouldReturnOk() throws Exception {
        doNothing().when(couponService).deleteCoupon(1L);

        mockMvc.perform(delete("/api/v1/coupons/delete/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Coupon deleted successfully"));
    }

    @Test
    @DisplayName("Should return a list of all existing coupons")
    void getAllCoupons_ShouldReturnList() throws Exception {
        List<CouponResponseDto> list = Collections.singletonList(couponResponseDto);
        when(couponService.findAllCoupons()).thenReturn(list);

        mockMvc.perform(get("/api/v1/coupons/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("All coupons retrieved"))
                .andExpect(jsonPath("$.data[0].code").value("SAVE20"));
    }
    @Test
    @DisplayName("Should return 400 Bad Request when coupon code is blank")
    void createCoupon_InvalidInput_ReturnsBadRequest() throws Exception {
        String invalidJson = "{\"code\":\"\", \"discountPercentage\": 20}";

        mockMvc.perform(post("/api/v1/coupons/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }
}