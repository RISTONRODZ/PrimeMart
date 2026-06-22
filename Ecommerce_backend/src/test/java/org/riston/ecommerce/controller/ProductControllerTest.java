package org.riston.ecommerce.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.riston.ecommerce.model.Product;
import org.riston.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
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
        @DisplayName("Should return product details when valid id is provided")
        void getProductById_Success() throws Exception {

            Product product = buildProduct();

            when(productService.findProductById(1L))
                    .thenReturn(product);

            mockMvc.perform(get("/api/v1/products/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.title")
                            .value("Nike T-Shirt"))
                    .andExpect(jsonPath("$.sellingPrice")
                            .value(800))
                    .andExpect(jsonPath("$.mrpPrice")
                            .value(1000))
                    .andExpect(jsonPath("$.color")
                            .value("Red"));

            verify(productService)
                    .findProductById(1L);
        }
    }

    @Nested
    @DisplayName("GET /api/v1/products/search")
    class SearchProductsTests {

        @Test
        @DisplayName("Should search products using query and category")
        void searchProducts_WithQueryAndCategory() throws Exception {

            Product product = buildProduct();

            Page<Product> page =
                    new PageImpl<>(java.util.List.of(product));

            ArgumentCaptor<Pageable> pageableCaptor =
                    ArgumentCaptor.forClass(Pageable.class);

            when(productService.searchAndFilter(
                    eq("shirt"),
                    eq("Clothing"),
                    pageableCaptor.capture()))
                    .thenReturn(page);

            mockMvc.perform(get("/api/v1/products/search")
                            .param("query", "shirt")
                            .param("category", "Clothing")
                            .param("page", "0")
                            .param("size", "20"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].title")
                            .value("Nike T-Shirt"))
                    .andExpect(jsonPath("$.content[0].sellingPrice")
                            .value(800))
                    .andExpect(jsonPath("$.totalElements")
                            .value(1));

            Pageable pageable = pageableCaptor.getValue();

            assertThat(pageable.getPageNumber()).isEqualTo(0);
            assertThat(pageable.getPageSize()).isEqualTo(20);

            verify(productService)
                    .searchAndFilter(
                            eq("shirt"),
                            eq("Clothing"),
                            any(Pageable.class));
        }

        @Test
        @DisplayName("Should return empty page when no products found")
        void searchProducts_EmptyResult() throws Exception {

            when(productService.searchAndFilter(
                    any(),
                    any(),
                    any(Pageable.class)))
                    .thenReturn(Page.empty());

            mockMvc.perform(get("/api/v1/products/search"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").isArray())
                    .andExpect(jsonPath("$.content").isEmpty())
                    .andExpect(jsonPath("$.totalElements")
                            .value(0));
        }
    }

    @Nested
    @DisplayName("GET /api/v1/products")
    class GetAllProductsTests {

        @Test
        @DisplayName("Should filter products with all parameters")
        void getAllProducts_WithFilters() throws Exception {

            Product product = buildProduct();

            Page<Product> page =
                    new PageImpl<>(java.util.List.of(product));

            when(productService.getAllProducts(
                    "T-Shirts",
                    "Nike",
                    "Red",
                    "M",
                    500,
                    1500,
                    10,
                    "price_low",
                    "in_stock",
                    0))
                    .thenReturn(page);

            mockMvc.perform(get("/api/v1/products")
                            .param("category", "T-Shirts")
                            .param("brand", "Nike")
                            .param("color", "Red")
                            .param("size", "M")
                            .param("minPrice", "500")
                            .param("maxPrice", "1500")
                            .param("minDiscount", "10")
                            .param("sort", "price_low")
                            .param("stock", "in_stock")
                            .param("pageNumber", "0"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].title")
                            .value("Nike T-Shirt"))
                    .andExpect(jsonPath("$.content[0].color")
                            .value("Red"))
                    .andExpect(jsonPath("$.totalElements")
                            .value(1));

            verify(productService).getAllProducts(
                    "T-Shirts",
                    "Nike",
                    "Red",
                    "M",
                    500,
                    1500,
                    10,
                    "price_low",
                    "in_stock",
                    0
            );
        }

        @Test
        @DisplayName("Should work without optional filters")
        void getAllProducts_WithoutFilters() throws Exception {

            when(productService.getAllProducts(
                    isNull(),
                    isNull(),
                    isNull(),
                    isNull(),
                    isNull(),
                    isNull(),
                    isNull(),
                    isNull(),
                    isNull(),
                    eq(0)))
                    .thenReturn(Page.empty());

            mockMvc.perform(get("/api/v1/products"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").isEmpty());

            verify(productService).getAllProducts(
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    0
            );
        }
    }
}