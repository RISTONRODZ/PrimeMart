package org.riston.ecommerce.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.riston.ecommerce.exception.ProductException;
import org.riston.ecommerce.model.Category;
import org.riston.ecommerce.model.Product;
import org.riston.ecommerce.model.Seller;
import org.riston.ecommerce.repository.CategoryRepository;
import org.riston.ecommerce.repository.ProductRepository;
import org.riston.ecommerce.request.CreateProductRequestDto;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private CreateProductRequestDto requestDto;
    private Seller seller;

    @BeforeEach
    void setUp() {
        seller = new Seller();
        seller.setId(1L);

        requestDto = new CreateProductRequestDto("Test Product", "Description", 1000, 800, List.of("img1.jpg"), "cat1", "cat2", "cat3", "S,M,L", "Red");
    }

    @Test
    @DisplayName("Should successfully create a product")
    void createProduct_Success() {
        when(productRepository.findByTitleAndSeller(anyString(), any())).thenReturn(null);
        when(categoryRepository.findByCategoryId(anyString())).thenReturn(new Category());
        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArguments()[0]);

        Product savedProduct = productService.createProduct(requestDto, seller);

        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getSellingPrice()).isEqualTo(800);
        assertThat(savedProduct.getDiscountPercent()).isEqualTo(20);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Should throw exception when product title exists")
    void createProduct_DuplicateTitle() {
        when(productRepository.findByTitleAndSeller(anyString(), any())).thenReturn(new Product());

        assertThrows(RuntimeException.class, () -> productService.createProduct(requestDto, seller));
    }

    @Test
    @DisplayName("Should throw ProductException when finding non-existent product")
    void findProductById_NotFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ProductException.class, () -> productService.findProductById(99L));
    }

    @Test
    @DisplayName("Should correctly calculate discount percentage")
    void calculateDiscountPercentage_CorrectMath() {
        when(productRepository.findByTitleAndSeller(any(), any())).thenReturn(null);
        when(categoryRepository.findByCategoryId(any())).thenReturn(new Category());
        when(productRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        CreateProductRequestDto discountReq = new CreateProductRequestDto("Discount Test", "Desc", 200, 100, List.of("img.jpg"), "c1", "c2", "c3", "M", "Blue");

        Product result = productService.createProduct(discountReq, seller);
        assertThat(result.getDiscountPercent()).isEqualTo(50);
    }
}