package org.riston.ecommerce.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.riston.ecommerce.response.ProductResponse;
import org.riston.ecommerce.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Tag(name = "Product Catalog", description = "Public endpoints for browsing, searching, and filtering products.")
public class ProductController {
    private final ProductService productService;

    @GetMapping("/{productId}")
    @Operation(summary = "Get product by ID", description = "Retrieves detailed information for a single product using its unique ID.")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long productId) {
        return ResponseEntity.ok(new ProductResponse(productService.findProductById(productId)));
    }

    @GetMapping("/search")
    @Operation(summary = "Search products", description = "Performs a text-based search or category lookup with paginated results.")
    public ResponseEntity<Page<ProductResponse>> searchProducts(
            @Parameter(description = "Search keyword") @RequestParam(required = false) String query,
            @Parameter(description = "Category name to filter by") @RequestParam(required = false) String category,
            @PageableDefault(size = 20) Pageable pageable) {

        return ResponseEntity.ok(productService.searchAndFilter(query, category, pageable)
                .map(ProductResponse::new));
    }
    @GetMapping
    @Operation(summary = "Filter products", description = "Retrieves a paginated list of products based on comprehensive filter criteria including price range, brand, and discounts.")
    public ResponseEntity<Page<ProductResponse>> getAllProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) String productSize, 
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) Integer minDiscount,
            @RequestParam(required = false) String stock,
            @PageableDefault(size = 10, sort = "sellingPrice", direction = Sort.Direction.DESC) Pageable pageable) {

        return ResponseEntity.ok(productService.getAllProducts(category, brand, color, productSize,
                        minPrice, maxPrice, minDiscount, stock, pageable)
                .map(ProductResponse::new));
    }
}