package org.riston.ecommerce.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.riston.ecommerce.model.Product;
import org.riston.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    private Product buildProduct() {
        Product product = new Product();
        product.setId(1L);
        product.setTitle("Nike T-Shirt");
        product.setDescription("Premium cotton t-shirt");
        product.setColor("Red");
        product.setSellingPrice(800);
        product.setMrpPrice(1000);
        product.setDiscountPercent(20);
        product.setSizes("M");
        return product;
    }

    @Nested
    @DisplayName("GET /api/v1/products/{id}")
    class GetProductByIdTests {

        @Test
        void getProductById_Success() throws Exception {
            Product product = buildProduct();
            when(productService.findProductById(1L)).thenReturn(product);

            mockMvc.perform(get("/api/v1/products/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.title").value("Nike T-Shirt"))
                    .andExpect(jsonPath("$.sellingPrice").value(800));

            verify(productService).findProductById(1L);
        }
    }

    @Nested
    @DisplayName("GET /api/v1/products")
    class GetAllProductsTests {

        @Test
        void getAllProducts_WithFilters() throws Exception {
            Product product = buildProduct();
            Page<Product> page = new PageImpl<>(List.of(product));

            when(productService.getAllProducts(
                    anyString(),
                    anyString(),
                    anyString(),
                    anyString(),
                    anyInt(),
                    anyInt(),
                    anyInt(),
                    anyString(),
                    any(Pageable.class)))
                    .thenReturn(page);

            mockMvc.perform(get("/api/v1/products")
                            .param("category", "T-Shirts")
                            .param("brand", "Nike")
                            .param("color", "Red")
                            .param("productSize", "M")
                            .param("minPrice", "500")
                            .param("maxPrice", "1500")
                            .param("minDiscount", "10")
                            .param("stock", "in_stock")
                            .param("page", "0")
                            .param("size", "10") 
                            .param("sort", "sellingPrice,desc"))
                    .andExpect(status().isOk());
            verify(productService).getAllProducts(
                    eq("T-Shirts"),
                    eq("Nike"),
                    eq("Red"),
                    eq("M"),
                    eq(500),
                    eq(1500),
                    eq(10),
                    eq("in_stock"),
                    any(Pageable.class)
            );
        }

        @Test
        void getAllProducts_WithoutFilters() throws Exception {
            when(productService.getAllProducts(
                    isNull(), isNull(), isNull(), isNull(),
                    isNull(), isNull(), isNull(), isNull(),
                    any(Pageable.class)))
                    .thenReturn(Page.empty());

            mockMvc.perform(get("/api/v1/products"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").isEmpty());
        }
    }
}