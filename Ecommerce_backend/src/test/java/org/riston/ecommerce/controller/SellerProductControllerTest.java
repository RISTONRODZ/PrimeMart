package org.riston.ecommerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.riston.ecommerce.model.Category;
import org.riston.ecommerce.model.Product;
import org.riston.ecommerce.model.Seller;
import org.riston.ecommerce.request.CreateProductRequestDto;
import org.riston.ecommerce.service.ProductService;
import org.riston.ecommerce.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SellerProductController.class)
class SellerProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private SellerService sellerService;

    private static final String JWT = "Bearer mock.jwt";

    private Seller buildSeller(Long id) {
        Seller seller = new Seller();
        seller.setId(id);
        seller.setSellerName("Tech Store");
        return seller;
    }

    private Product buildProduct(Long id, Seller seller) {

        Category category = new Category();
        category.setCategoryId("electronics");

        Product product = new Product();
        product.setId(id);
        product.setTitle("MacBook Pro");
        product.setDescription("Apple Laptop");
        product.setSellingPrice(150000);
        product.setMrpPrice(180000);
        product.setDiscountPercent(20);
        product.setQuantity(10);
        product.setColor("Space Gray");
        product.setImages(List.of("img1.jpg", "img2.jpg"));
        product.setCategory(category);
        product.setSeller(seller);
        product.setSizes("M,L,XL");
        product.setNumRatings(0);

        return product;
    }

    private Product buildUpdateRequest() {

        Product product = new Product();

        product.setTitle("Updated Laptop");
        product.setDescription("Updated Description");
        product.setSellingPrice(100000);
        product.setMrpPrice(120000);
        product.setDiscountPercent(20);
        product.setQuantity(5);
        product.setColor("Silver");
        product.setImages(List.of("updated.jpg"));
        product.setSizes("M,L");
        product.setNumRatings(0);

        return product;
    }

    // =====================================================
    // GET PRODUCTS
    // =====================================================

    @Test
    @DisplayName("GET /seller/products - Success")
    void getProductBySellerId_ShouldReturnProducts() throws Exception {

        Seller seller = buildSeller(1L);

        when(sellerService.getSellerProfile(JWT))
                .thenReturn(seller);

        when(productService.getProductBySellerId(1L))
                .thenReturn(List.of(
                        buildProduct(1L, seller),
                        buildProduct(2L, seller)
                ));

        mockMvc.perform(get("/api/v1/seller/products")
                        .header(HttpHeaders.AUTHORIZATION, JWT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title")
                        .value("MacBook Pro"));

        verify(sellerService).getSellerProfile(JWT);
        verify(productService).getProductBySellerId(1L);
    }

    @Test
    @DisplayName("GET /seller/products - Empty list")
    void getProductBySellerId_ShouldReturnEmptyList() throws Exception {

        Seller seller = buildSeller(1L);

        when(sellerService.getSellerProfile(JWT))
                .thenReturn(seller);

        when(productService.getProductBySellerId(1L))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/v1/seller/products")
                        .header(HttpHeaders.AUTHORIZATION, JWT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(productService).getProductBySellerId(1L);
    }

    @Test
    @DisplayName("GET /seller/products - Missing Authorization Header")
    void getProductBySellerId_ShouldReturnBadRequest_WhenHeaderMissing()
            throws Exception {

        mockMvc.perform(get("/api/v1/seller/products"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(productService);
    }

    // =====================================================
    // CREATE PRODUCT
    // =====================================================

    @Test
    @DisplayName("POST /seller/products - Success")
    void createProduct_ShouldReturnCreatedProduct() throws Exception {

        Seller seller = buildSeller(1L);

        CreateProductRequestDto request =
                new CreateProductRequestDto(
                        "MacBook Pro",
                        "Apple Laptop",
                        180000,
                        150000,
                        List.of("img1.jpg"),
                        "Electronics",
                        "Computers",
                        "Laptops",
                        "M,L,XL",
                        "Space Gray"
                );

        Product createdProduct = buildProduct(10L, seller);

        when(sellerService.getSellerProfile(JWT))
                .thenReturn(seller);

        when(productService.createProduct(any(CreateProductRequestDto.class), eq(seller)))
                .thenReturn(createdProduct);

        mockMvc.perform(post("/api/v1/seller/products")
                        .header(HttpHeaders.AUTHORIZATION, JWT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.title")
                        .value("MacBook Pro"))
                .andExpect(jsonPath("$.sellerName")
                        .value("Tech Store"));

        verify(productService)
                .createProduct(any(CreateProductRequestDto.class), eq(seller));
    }

    @Test
    @DisplayName("POST /seller/products - Invalid Request")
    void createProduct_ShouldReturnBadRequest_WhenRequestInvalid()
            throws Exception {

        String invalidRequest = "{}";

        mockMvc.perform(post("/api/v1/seller/products")
                        .header(HttpHeaders.AUTHORIZATION, JWT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(productService);
    }

    // =====================================================
    // DELETE PRODUCT
    // =====================================================

    @Test
    @DisplayName("DELETE /seller/products/{id} - Owner Can Delete")
    void deleteProduct_ShouldDelete_WhenOwner() throws Exception {

        Seller seller = buildSeller(1L);
        Product product = buildProduct(10L, seller);

        when(sellerService.getSellerProfile(JWT))
                .thenReturn(seller);

        when(productService.findProductById(10L))
                .thenReturn(product);

        mockMvc.perform(delete("/api/v1/seller/products/10")
                        .header(HttpHeaders.AUTHORIZATION, JWT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status")
                        .value("success"))
                .andExpect(jsonPath("$.message")
                        .value("Product with ID 10 was successfully deleted."));

        verify(productService).deleteProduct(10L);
    }

    @Test
    @DisplayName("DELETE /seller/products/{id} - Forbidden When Not Owner")
    void deleteProduct_ShouldReturnForbidden_WhenNotOwner()
            throws Exception {

        Seller authenticatedSeller = buildSeller(1L);
        Seller owner = buildSeller(2L);

        Product product = buildProduct(10L, owner);

        when(sellerService.getSellerProfile(JWT))
                .thenReturn(authenticatedSeller);

        when(productService.findProductById(10L))
                .thenReturn(product);

        mockMvc.perform(delete("/api/v1/seller/products/10")
                        .header(HttpHeaders.AUTHORIZATION, JWT))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error")
                        .value("Access Denied"));

        verify(productService, never()).deleteProduct(anyLong());
    }

    // =====================================================
    // UPDATE PRODUCT
    // =====================================================

    @Test
    @DisplayName("PUT /seller/products/{id} - Success")
    void updateProduct_ShouldUpdate_WhenOwner() throws Exception {

        Seller seller = buildSeller(1L);

        Product existingProduct = buildProduct(10L, seller);
        Product updatedProduct = buildProduct(10L, seller);
        updatedProduct.setTitle("Updated Laptop");

        when(sellerService.getSellerProfile(JWT))
                .thenReturn(seller);

        when(productService.findProductById(10L))
                .thenReturn(existingProduct);

        when(productService.updateProduct(eq(10L), any(Product.class)))
                .thenReturn(updatedProduct);

        mockMvc.perform(put("/api/v1/seller/products/10")
                        .header(HttpHeaders.AUTHORIZATION, JWT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildUpdateRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title")
                        .value("Updated Laptop"));

        verify(productService)
                .updateProduct(eq(10L), any(Product.class));
    }

    @Test
    @DisplayName("PUT /seller/products/{id} - Forbidden When Not Owner")
    void updateProduct_ShouldReturnForbidden_WhenNotOwner()
            throws Exception {

        Seller authenticatedSeller = buildSeller(1L);
        Seller owner = buildSeller(2L);

        Product existingProduct = buildProduct(10L, owner);

        when(sellerService.getSellerProfile(JWT))
                .thenReturn(authenticatedSeller);

        when(productService.findProductById(10L))
                .thenReturn(existingProduct);

        mockMvc.perform(put("/api/v1/seller/products/10")
                        .header(HttpHeaders.AUTHORIZATION, JWT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildUpdateRequest())))
                .andExpect(status().isForbidden());

        verify(productService, never())
                .updateProduct(anyLong(), any(Product.class));
    }
}