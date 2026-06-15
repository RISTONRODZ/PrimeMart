package org.riston.ecommerce.controller;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.riston.ecommerce.model.Seller;
import org.riston.ecommerce.model.Transaction;
import org.riston.ecommerce.response.ApiResponseDto;
import org.riston.ecommerce.response.TransactionResponseDto;
import org.riston.ecommerce.service.SellerService;
import org.riston.ecommerce.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final SellerService sellerService;

    @GetMapping("/seller")
    public ResponseEntity<ApiResponseDto<List<TransactionResponseDto>>> getTransactionBySeller(@RequestHeader("Authorization") String jwt) {

        Seller seller = sellerService.getSellerProfile(jwt);
        List<Transaction> transactions = transactionService.getTransactionsBySellerId(seller);

        // Convert Entities smoothly to lightweight Records using Stream API
        List<TransactionResponseDto> dtoList = transactions.stream().map(this::convertToDto).toList();

        return ResponseEntity.ok(ApiResponseDto.success("Seller transactions retrieved successfully", dtoList));
    }

    @GetMapping
    public ResponseEntity<ApiResponseDto<List<TransactionResponseDto>>> getAllTransactions() {
        List<Transaction> transactions = transactionService.getAllTransactions();

        List<TransactionResponseDto> dtoList = transactions.stream().map(this::convertToDto).toList();

        return ResponseEntity.ok(ApiResponseDto.success("All transactions retrieved successfully", dtoList));
    }

    private TransactionResponseDto convertToDto(Transaction transaction) {
        return getTransactionResponseDto(transaction);
    }

    @NonNull
    public static TransactionResponseDto getTransactionResponseDto(Transaction transaction) {
        return new TransactionResponseDto(transaction.getId(), transaction.getOrder() != null ? transaction.getOrder().getId() : null, transaction.getOrder() != null ? transaction.getOrder().getOrderId() : null, transaction.getSeller() != null ? transaction.getSeller().getId() : null, transaction.getSeller() != null ? transaction.getSeller().getSellerName() : null, transaction.getOrder() != null ? transaction.getOrder().getTotalSellingPrice() : 0,

                transaction.getOrder() != null && transaction.getOrder().getPaymentStatus() != null ? transaction.getOrder().getPaymentStatus().name() : "UNKNOWN",

                transaction.getDate());
    }
}