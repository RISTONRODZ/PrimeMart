package org.riston.ecommerce.service;

import org.riston.ecommerce.model.Product;
import org.riston.ecommerce.model.Seller;
import org.riston.ecommerce.request.CreateProductRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    Product createProduct(CreateProductRequestDto req, Seller seller);
    void deleteProduct(Long ProductId);
    Product updateProduct(Long productId,Product product);
    Product findProductById(Long productId);
    Page<Product> getAllProducts(
            String category,
            String brand,
            String colors,
            String size,
            Integer minPrice,
            Integer maxPrice,
            Integer minDiscount,
            String stock,
            Pageable pageable
    );

    List<Product> getProductBySellerId(Long sellerId);
    Page<Product> searchAndFilter(String query, String category, Pageable pageable);

}
