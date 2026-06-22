package org.riston.ecommerce.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.riston.ecommerce.domain.PaymentStatus;
import org.riston.ecommerce.model.Order;
import org.riston.ecommerce.model.Seller;
import org.riston.ecommerce.model.Transaction;
import org.riston.ecommerce.service.SellerService;
import org.riston.ecommerce.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TransactionService transactionService;

    @MockitoBean
    private SellerService sellerService;

    private Transaction buildTransaction() {
        Seller seller = new Seller();
        seller.setId(1L);
        seller.setSellerName("Tech Store");

        Order order = new Order();
        order.setId(100L);
        order.setOrderId("ORD-123");
        order.setTotalSellingPrice(4999);
        order.setPaymentStatus(PaymentStatus.COMPLETED);

        Transaction tx = new Transaction();
        tx.setId(10L);
        tx.setSeller(seller);
        tx.setOrder(order);
        tx.setDate(LocalDateTime.now());

        return tx;
    }

    @Test
    @DisplayName("GET /api/v1/transactions/seller - Success")
    void getTransactionBySeller_ShouldReturnTransactions() throws Exception {

        String jwt = "Bearer mock.jwt";

        Seller seller = new Seller();
        seller.setId(1L);
        seller.setSellerName("Tech Store");

        List<Transaction> transactions = List.of(buildTransaction());

        when(sellerService.getSellerProfile(jwt))
                .thenReturn(seller);

        when(transactionService.getTransactionsBySellerId(seller))
                .thenReturn(transactions);

        mockMvc.perform(get("/api/v1/transactions/seller")
                        .header(HttpHeaders.AUTHORIZATION, jwt)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message")
                        .value("Seller transactions retrieved successfully"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].id").value(10))
                .andExpect(jsonPath("$.data[0].orderId").value(100))
                .andExpect(jsonPath("$.data[0].orderTrackingId")
                        .value("ORD-123"))
                .andExpect(jsonPath("$.data[0].sellerId").value(1))
                .andExpect(jsonPath("$.data[0].sellerName")
                        .value("Tech Store"))
                .andExpect(jsonPath("$.data[0].totalSellingPrice")
                        .value(4999))
                .andExpect(jsonPath("$.data[0].paymentStatus")
                        .value("COMPLETED"));
        verify(sellerService).getSellerProfile(jwt);
        verify(transactionService).getTransactionsBySellerId(seller);
    }

    @Test
    @DisplayName("GET /api/v1/transactions/seller - Empty list")
    void getTransactionBySeller_ShouldReturnEmptyList() throws Exception {

        String jwt = "Bearer jwt";

        Seller seller = new Seller();
        seller.setId(1L);

        when(sellerService.getSellerProfile(jwt))
                .thenReturn(seller);

        when(transactionService.getTransactionsBySellerId(seller))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/transactions/seller")
                        .header(HttpHeaders.AUTHORIZATION, jwt))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data").isEmpty());

        verify(transactionService).getTransactionsBySellerId(seller);
    }

    @Test
    @DisplayName("GET /api/v1/transactions/seller - Missing Authorization header")
    void getTransactionBySeller_ShouldReturnBadRequest_WhenHeaderMissing()
            throws Exception {

        mockMvc.perform(get("/api/v1/transactions/seller"))
                .andExpect(status().isBadRequest());

        verify(sellerService, never()).getSellerProfile(any());
    }

    @Test
    @DisplayName("GET /api/v1/transactions - Success")
    void getAllTransactions_ShouldReturnAllTransactions() throws Exception {

        when(transactionService.getAllTransactions())
                .thenReturn(List.of(buildTransaction()));

        mockMvc.perform(get("/api/v1/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message")
                        .value("All transactions retrieved successfully"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].id").value(10))
                .andExpect(jsonPath("$.data[0].sellerName")
                        .value("Tech Store"));

        verify(transactionService).getAllTransactions();
    }

    @Test
    @DisplayName("GET /api/v1/transactions - Empty list")
    void getAllTransactions_ShouldReturnEmptyList() throws Exception {

        when(transactionService.getAllTransactions())
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data").isEmpty());

        verify(transactionService).getAllTransactions();
    }
}