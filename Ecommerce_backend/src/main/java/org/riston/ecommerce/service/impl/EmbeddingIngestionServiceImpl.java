package org.riston.ecommerce.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.riston.ecommerce.model.Order;
import org.riston.ecommerce.model.Product;
import org.riston.ecommerce.service.EmbeddingIngestionService;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmbeddingIngestionServiceImpl implements EmbeddingIngestionService {

    private final VectorStore vectorStore;

    public void ingestProducts(List<Product> products) {
        log.info("Ingesting {} products...", products.size());
        List<Document> docs = products.stream().map(p -> {
            String content = "Product: " + p.getTitle()
                    + ". Category: " + p.getCategory().getName()
                    + ". Selling Price: " + p.getSellingPrice()
                    + ". MRP: " + p.getMrpPrice()
                    + ". Colors: " + String.join(", ", p.getColors())
                    + ". Description: " + p.getDescription();
            Map<String, Object> meta = Map.of("type", "product", "id", p.getId().toString());
            return new Document(content, meta);
        }).toList();

        vectorStore.add(docs);
    }

    public void ingestOrders(List<Order> orders) {
        List<Document> docs = orders.stream().map(o -> {
            String content = "Order #" + o.getId() + " by user " + o.getUser().getEmail() + ". Status: " + o.getOrderStatus();
            Map<String, Object> meta = Map.of("type", "order", "id", o.getId().toString());
            return new Document(content, meta);
        }).toList();

        vectorStore.add(docs);
    }
}
