package org.riston.ecommerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.riston.ecommerce.model.Seller;
import org.riston.ecommerce.model.SellerReport;
import org.riston.ecommerce.service.SellerReportService;
import org.riston.ecommerce.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SellerController.class)
class SellerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private SellerService sellerService;

    @MockitoBean
    private SellerReportService sellerReportService;

    private Seller buildSeller() {
        Seller seller = new Seller();
        seller.setId(1L);
        seller.setSellerName("Riston Store");
        seller.setEmail("seller@test.com");
        seller.setMobile("9876543210");
        return seller;
    }

    private SellerReport buildReport() {
        SellerReport report = new SellerReport();
        report.setTotalOrders(25L);
        report.setTotalSales(50000L);
        report.setTotalEarnings(12000L);
        return report;
    }

    @Nested
    @DisplayName("GET /api/v1/seller/profile")
    class GetSellerProfileTests {

        @Test
        @DisplayName("Should return authenticated seller profile")
        void getSellerProfile_Success() throws Exception {

            Seller seller = buildSeller();

            when(sellerService.getSellerProfile("Bearer jwt-token"))
                    .thenReturn(seller);

            mockMvc.perform(get("/api/v1/seller/profile")
                            .header("Authorization", "Bearer jwt-token"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.sellerName")
                            .value("Riston Store"))
                    .andExpect(jsonPath("$.email")
                            .value("seller@test.com"))
                    .andExpect(jsonPath("$.mobile")
                            .value("9876543210"));

            verify(sellerService)
                    .getSellerProfile("Bearer jwt-token");
        }

        @Test
        @DisplayName("Should return 400 when Authorization header missing")
        void getSellerProfile_MissingHeader() throws Exception {

            mockMvc.perform(get("/api/v1/seller/profile"))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(sellerService);
        }
    }

    @Nested
    @DisplayName("GET /api/v1/seller/report")
    class GetSellerReportTests {

        @Test
        @DisplayName("Should return seller report")
        void getSellerReport_Success() throws Exception {

            Seller seller = buildSeller();
            SellerReport report = buildReport();

            when(sellerService.getSellerProfile("Bearer jwt-token"))
                    .thenReturn(seller);

            when(sellerReportService.getSellerReport(seller))
                    .thenReturn(report);

            mockMvc.perform(get("/api/v1/seller/report")
                            .header("Authorization", "Bearer jwt-token"))
                    .andExpect(status().isOk());

            verify(sellerService)
                    .getSellerProfile("Bearer jwt-token");

            verify(sellerReportService)
                    .getSellerReport(seller);
        }

        @Test
        @DisplayName("Should return 400 when Authorization header missing")
        void getSellerReport_MissingHeader() throws Exception {

            mockMvc.perform(get("/api/v1/seller/report"))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(sellerService);
            verifyNoInteractions(sellerReportService);
        }
    }

    @Nested
    @DisplayName("PATCH /api/v1/seller")
    class UpdateSellerTests {

        @Test
        @DisplayName("Should update seller profile")
        void updateSeller_Success() throws Exception {

            Seller existing = buildSeller();

            Seller request = new Seller();
            request.setSellerName("Updated Store");

            Seller updated = buildSeller();
            updated.setSellerName("Updated Store");

            when(sellerService.getSellerProfile("Bearer jwt-token"))
                    .thenReturn(existing);

            when(sellerService.updateSeller(1L, request))
                    .thenReturn(updated);

            mockMvc.perform(patch("/api/v1/seller")
                            .header("Authorization", "Bearer jwt-token")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.sellerName")
                            .value("Updated Store"));

            verify(sellerService)
                    .getSellerProfile("Bearer jwt-token");

            verify(sellerService)
                    .updateSeller(eq(1L), any(Seller.class));
        }

        @Test
        @DisplayName("Should return 400 when Authorization header missing")
        void updateSeller_MissingHeader() throws Exception {

            Seller request = new Seller();
            request.setSellerName("Updated");

            mockMvc.perform(patch("/api/v1/seller")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(sellerService);
        }
    }

    @Nested
    @DisplayName("POST /api/v1/seller/verify/{otp}")
    class VerifyEmailTests {

        @Test
        @DisplayName("Should verify email successfully")
        void verifyEmail_Success() throws Exception {

            Seller verifiedSeller = buildSeller();

            when(sellerService.verifyEmail(
                    "seller@test.com",
                    "123456"))
                    .thenReturn(verifiedSeller);

            mockMvc.perform(post("/api/v1/seller/verify/123456")
                            .param("email", "seller@test.com"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.email")
                            .value("seller@test.com"))
                    .andExpect(jsonPath("$.sellerName")
                            .value("Riston Store"));

            verify(sellerService)
                    .verifyEmail("seller@test.com", "123456");
        }

        @Test
        @DisplayName("Should return 400 when email parameter missing")
        void verifyEmail_MissingEmail() throws Exception {

            mockMvc.perform(post("/api/v1/seller/verify/123456"))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(sellerService);
        }
    }
}