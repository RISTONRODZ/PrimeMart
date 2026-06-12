package org.riston.ecommerce.service;

import org.riston.ecommerce.model.Product;
import org.riston.ecommerce.model.Seller;
import org.riston.ecommerce.request.CreateProductRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    Product createProduct(CreateProductRequest req, Seller seller);
    void deleteProduct(Long ProductId);
    Product updateProduct(Long productId,Product product);
    Product findProductById(Long productId);
    List<Product> searchProducts(String query);

    Page<Product> getAllProducts(
            String category,
            String brand,
            String colors,
            String size,
            Integer minPrice,
            Integer maxPrice,
            Integer minDiscount,
            String sort,
            String stock,
            Integer pageNumber
    );

    List<Product> getProductBySellerId(Long sellerId);

}
