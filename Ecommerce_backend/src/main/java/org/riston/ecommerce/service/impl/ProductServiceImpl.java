package org.riston.ecommerce.service.impl;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.riston.ecommerce.exception.ProductException;
import org.riston.ecommerce.model.Category;
import org.riston.ecommerce.model.Product;
import org.riston.ecommerce.model.Seller;
import org.riston.ecommerce.repository.CategoryRepository;
import org.riston.ecommerce.repository.ProductRepository;
import org.riston.ecommerce.request.CreateProductRequestDto;
import org.riston.ecommerce.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    public final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final EmbeddingIngestionServiceImpl embeddingIngestionService;

    @Override
    @Transactional
    public Product createProduct(CreateProductRequestDto req, Seller seller) {
        Product existingProduct = productRepository.findByTitleAndSeller(req.title(), seller);
        if (existingProduct != null) {
            throw new RuntimeException("Product with title '" + req.title() + "' already exists.");
        }

        Category category = findOrCreateCategory(req.category(), 1, null);
        Category category1 = findOrCreateCategory(req.category2(), 2, category);
        Category category2 = findOrCreateCategory(req.category3(), 3, category1);

        Product product = new Product();
        product.setSeller(seller);
        product.setCategory(category2);
        product.setDescription(req.description());
        product.setCreatedAt(LocalDateTime.now());
        product.setTitle(req.title());
        product.setColor(req.color());
        product.setSellingPrice(req.sellingPrice());
        product.setImages(req.images());
        product.setMrpPrice(req.mrpPrice());
        product.setSizes(req.sizes());
        product.setDiscountPercent(calculateDiscountPercentage(req.mrpPrice(), req.sellingPrice()));
        Product savedProduct = productRepository.save(product);
        embeddingIngestionService.ingestProducts(List.of(savedProduct));
        return savedProduct;
    }

    private Category findOrCreateCategory(String categoryId, int level, Category parent) {
        Category category = categoryRepository.findByCategoryId(categoryId);
        if (category == null) {
            category = new Category();
            category.setCategoryId(categoryId);
            category.setLevel(level);
            category.setParentCategory(parent);
            category = categoryRepository.save(category);
        }
        return category;
    }

    private int calculateDiscountPercentage(Integer mrpPrice, Integer sellingPrice) {
        if (mrpPrice <= 0) {
            throw new IllegalArgumentException("Actual price must be greater than 0");
        }
        double discount = mrpPrice - sellingPrice;
        double discountPercentage = (discount / mrpPrice) * 100;
        return (int) discountPercentage;
    }

    @Override
    @Transactional
    public void deleteProduct(Long productId) {
        Product product = findProductById(productId);
        productRepository.delete(product);
    }

    @Override
    @Transactional
    public Product updateProduct(Long productId, Product productDetails) {
        Product existingProduct = findProductById(productId);
        if (productDetails.getQuantity() != 0) {
            existingProduct.setQuantity(productDetails.getQuantity());
        }
        return productRepository.save(existingProduct);
    }
    @Override
    public Product findProductById(Long productId) {
        return productRepository.findById(productId).orElseThrow(() -> new ProductException("product not found with id: " + productId));
    }
    @Override
    public Page<Product> getAllProducts(String category, String brand, String colors, String size,
                                        Integer minPrice, Integer maxPrice, Integer minDiscount,
                                        String stock, Pageable pageable) {
        return productRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (category != null && !category.isEmpty()) {
                Join<Product, Category> categoryJoin = root.join("category", JoinType.LEFT);
                predicates.add(cb.equal(categoryJoin.get("categoryId"), category));
            }
            if (minPrice != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("sellingPrice"), minPrice));
            }
            if (maxPrice != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("sellingPrice"), maxPrice));
            }
            if (minDiscount != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("discountPercent"), minDiscount));
            }
            if (colors != null && !colors.isEmpty()) {
                predicates.add(cb.equal(root.get("color"), colors));
            }
            if (size != null && !size.isEmpty()) {
                predicates.add(cb.equal(root.get("sizes"), size));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);
    }
    @Override
    public List<Product> getProductBySellerId(Long sellerId) {
        return productRepository.findBySellerId(sellerId);
    }

    @Override
    public Page<Product> searchAndFilter(String query, String category, Pageable pageable) {
        return productRepository.findAll((root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (query != null && !query.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("title")), "%" + query.toLowerCase() + "%"));
            }

            if (category != null && !category.isEmpty()) {
                Join<Product, Category> categoryJoin = root.join("category", JoinType.LEFT);
                predicates.add(cb.equal(categoryJoin.get("categoryId"), category));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);
    }

}