package org.riston.ecommerce.service;

import org.riston.ecommerce.modal.Product;
import org.riston.ecommerce.modal.Seller;
import org.riston.ecommerce.request.CreateProductRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
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
