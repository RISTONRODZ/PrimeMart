package org.riston.ecommerce.controller;

import org.junit.jupiter.api.Test;
import org.riston.ecommerce.domain.AccountStatus;
import org.riston.ecommerce.model.Seller;
import org.riston.ecommerce.response.SellerStatusResponse;
import org.riston.ecommerce.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminController.class)
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SellerService sellerService;

    @Test
    public void testUpdateSellerStatus_Success() throws Exception {
        Long sellerId = 1L;
        AccountStatus newStatus = AccountStatus.ACTIVE;

        Seller mockSeller = new Seller();
        mockSeller.setId(sellerId);
        mockSeller.setAccountStatus(newStatus);

        SellerStatusResponse mockResponse = new SellerStatusResponse(
                sellerId,
                "Alex J",
                "alex@test.com",
                newStatus
        );

        when(sellerService.updateSellerAccountStatus(sellerId, newStatus)).thenReturn(mockSeller);
        when(sellerService.mapToStatusResponse(mockSeller)).thenReturn(mockResponse);

        mockMvc.perform(patch("/api/v1/admin/seller/{id}/status/{status}", sellerId, newStatus))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(sellerId.intValue())))
                .andExpect(jsonPath("$.accountStatus", is(newStatus.name())));
    }

    @Test
    public void testUpdateSellerStatus_InvalidStatusEnum() throws Exception {
        mockMvc.perform(patch("/api/v1/admin/seller/1/status/INVALID_STATUS_NAME"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(sellerService);
    }

    @Test
    public void testAdminDeleteSeller_Success() throws Exception {
        Long sellerId = 1L;

        doNothing().when(sellerService).deleteSeller(sellerId);

        mockMvc.perform(delete("/api/v1/admin/seller/{id}", sellerId))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetAllSeller_WithStatusFilter_PopulatedList() throws Exception {
        AccountStatus filterStatus = AccountStatus.PENDING_VERIFICATION;

        Seller seller1 = new Seller();
        seller1.setId(1L);
        seller1.setSellerName("Alex J");
        seller1.setEmail("alex@test.com");
        seller1.setMobile("9876543210");
        seller1.setGSTIN("22AAAAA0000A1Z5");
        seller1.setAccountStatus(filterStatus);
        seller1.setEmailVerified(true);

        Seller seller2 = new Seller();
        seller2.setId(2L);
        seller2.setSellerName("John D");
        seller2.setEmail("john@test.com");
        seller2.setMobile("9999999999");
        seller2.setGSTIN("27BBBBB1111B2Z2");
        seller2.setAccountStatus(filterStatus);
        seller2.setEmailVerified(false);

        when(sellerService.getAllSellers(filterStatus)).thenReturn(List.of(seller1, seller2));

        mockMvc.perform(get("/api/v1/admin").param("status", filterStatus.name()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].sellerName", is("Alex J")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].sellerName", is("John D")));
    }

    @Test
    public void testGetAllSeller_NoFilter_EmptyList() throws Exception {
        when(sellerService.getAllSellers(null)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/admin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void testGetAllSeller_InvalidFilterParam() throws Exception {
        mockMvc.perform(get("/api/v1/admin").param("status", "NOT_A_VALID_ENUM"))
                .andExpect(status().isBadRequest());
    }
}