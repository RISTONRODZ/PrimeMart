package org.riston.ecommerce.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.riston.ecommerce.response.ProductResponse;
import org.riston.ecommerce.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) Integer minDiscount,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "20") int pageSize) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        return ResponseEntity.ok(productService.searchAndFilter(
                        query, category, color, minPrice, maxPrice, minDiscount, pageable)
                .map(ProductResponse::new));
    }
    @GetMapping
    @Operation(summary = "Filter products", description = "Retrieves a paginated list of products based on comprehensive filter criteria.")
    public ResponseEntity<Page<ProductResponse>> getAllProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) String productSize,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) Integer minDiscount,
            @RequestParam(required = false) String stock,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "9") int pageSize) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize, resolveSort(sort));

        return ResponseEntity.ok(productService.getAllProducts(category, null, color, productSize,
                        minPrice, maxPrice, minDiscount, stock, pageable)
                .map(ProductResponse::new));
    }

    private Sort resolveSort(String sort) {
        if (sort == null || sort.isBlank()) {
            return Sort.by(Sort.Direction.DESC, "sellingPrice");
        }
        String[] parts = sort.split(",");
        String field = parts[0].equalsIgnoreCase("price") ? "sellingPrice" : parts[0];
        Sort.Direction direction = (parts.length > 1 && parts[1].equalsIgnoreCase("asc"))
                ? Sort.Direction.ASC : Sort.Direction.DESC;
        return Sort.by(direction, field);
    }

//    private Pageable sanitizePageable(Pageable pageable) {
//        Sort sanitizedSort = Sort.by(pageable.getSort().stream()
//                .map(order -> {
//                    String property = order.getProperty().equalsIgnoreCase("price") ? "sellingPrice" : order.getProperty();
//                    return new Sort.Order(order.getDirection(), property);
//                })
//                .toList());
//
//        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sanitizedSort);
//    }
}