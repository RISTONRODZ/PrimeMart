package org.riston.ecommerce.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.riston.ecommerce.model.Seller;
import org.riston.ecommerce.model.Transaction;
import org.riston.ecommerce.response.ApiResponseDto;
import org.riston.ecommerce.response.TransactionResponseDto;
import org.riston.ecommerce.service.SellerService;
import org.riston.ecommerce.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transactions")
@Tag(name = "Transaction Management", description = "Endpoints for retrieving transaction history for sellers and administrators.")
public class TransactionController {

    private final TransactionService transactionService;
    private final SellerService sellerService;

    @GetMapping("/seller")
    @Operation(summary = "Get seller transactions", description = "Retrieves all transactions associated with the authenticated seller.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponseDto.class))),

    })
    public ResponseEntity<ApiResponseDto<List<TransactionResponseDto>>> getTransactionBySeller(
            @Parameter(description = "JWT token", required = true) @RequestHeader("Authorization") String jwt) {

        Seller seller = sellerService.getSellerProfile(jwt);
        List<Transaction> transactions = transactionService.getTransactionsBySellerId(seller);
        List<TransactionResponseDto> dtoList = transactions.stream().map(this::convertToDto).toList();

        return ResponseEntity.ok(ApiResponseDto.success("Seller transactions retrieved successfully", dtoList));
    }

    @GetMapping
    @Operation(summary = "Get all transactions", description = "Retrieves a list of all transactions in the system. Typically for admin use.")
    @ApiResponse(responseCode = "200", description = "All transactions retrieved successfully",
            content = @Content(schema = @Schema(implementation = ApiResponseDto.class)))
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
        return new TransactionResponseDto(
                transaction.getId(),
                transaction.getOrder() != null ? transaction.getOrder().getId() : null,
                transaction.getOrder() != null ? transaction.getOrder().getOrderId() : null,
                transaction.getSeller() != null ? transaction.getSeller().getId() : null,
                transaction.getSeller() != null ? transaction.getSeller().getSellerName() : null,
                transaction.getOrder() != null ? transaction.getOrder().getTotalSellingPrice() : 0,
                transaction.getOrder() != null && transaction.getOrder().getPaymentStatus() != null ? transaction.getOrder().getPaymentStatus().name() : "UNKNOWN",
                transaction.getDate());
    }
}