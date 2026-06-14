package org.riston.ecommerce.service;

import org.riston.ecommerce.model.Order;
import org.riston.ecommerce.model.Seller;
import org.riston.ecommerce.model.Transaction;

import java.util.List;

public interface TransactionService {
    @SuppressWarnings("UnusedReturnValue")
    Transaction createTransaction(Order order);
    List<Transaction> getTransactionsBySellerId(Seller seller);
    List<Transaction> getAllTransactions();
}
