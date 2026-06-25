package org.riston.ecommerce.service;

import org.riston.ecommerce.model.Order;
import org.riston.ecommerce.model.Product;

import java.util.List;

public interface EmbeddingIngestionService {
    void ingestProducts(List<Product> products);
    void ingestOrders(List<Order> orders);
}
