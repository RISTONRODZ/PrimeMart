package org.riston.ecommerce.service.impl;

import lombok.RequiredArgsConstructor;
import org.riston.ecommerce.model.Order;
import org.riston.ecommerce.model.Seller;
import org.riston.ecommerce.model.Transaction;
import org.riston.ecommerce.repository.SellerRepository;
import org.riston.ecommerce.repository.TransactionRepository;
import org.riston.ecommerce.service.TransactionService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final SellerRepository sellerRepository;

    @Override
    public Transaction createTransaction(Order order) {
        Seller seller = sellerRepository.findById(order.getSellerId())
                .orElseThrow(() -> new RuntimeException("Seller not found with ID: " + order.getSellerId()));

        Transaction transaction = new Transaction();
        transaction.setSeller(seller);
        transaction.setOrder(order);
        transaction.setDate(LocalDateTime.now());

        return transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> getTransactionsBySellerId(Seller seller) {
        return transactionRepository.findBySellerId(seller.getId());
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
}