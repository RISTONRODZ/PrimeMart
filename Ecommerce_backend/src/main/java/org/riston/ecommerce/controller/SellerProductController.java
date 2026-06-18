package org.riston.ecommerce.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.riston.ecommerce.annotation.ApiNotFoundResponse;
import org.riston.ecommerce.model.Product;
import org.riston.ecommerce.model.Seller;
import org.riston.ecommerce.request.CreateProductRequestDto;
import org.riston.ecommerce.response.ProductResponse;
import org.riston.ecommerce.service.ProductService;
import org.riston.ecommerce.service.SellerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/seller/products")
public class SellerProductController {
    private final ProductService productService;
    private final SellerService sellerService;

    @GetMapping()
    public ResponseEntity<List<Product>> getProductBySellerId(@RequestHeader("Authorization") String jwt) {
        Seller seller = sellerService.getSellerProfile(jwt);
        List<Product> products = productService.getProductBySellerId(seller.getId());
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody CreateProductRequestDto request,
                                                         @RequestHeader("Authorization") String jwt) {
        Seller seller = sellerService.getSellerProfile(jwt);
        Product product = productService.createProduct(request, seller);

        ProductResponse response = new ProductResponse(
                product.getId(),
                product.getTitle(),
                product.getDescription(),
                product.getSellingPrice(),
                product.getMrpPrice(),
                product.getDiscountPercent(),
                product.getColor(),
                product.getImages(),
                product.getCategory().getCategoryId(),
                product.getSeller().getSellerName()
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long productId,
                                           @RequestHeader("Authorization") String jwt) {
        Seller seller = sellerService.getSellerProfile(jwt);
        Product product = productService.findProductById(productId);

        Map<String, String> response = new HashMap<>();

        if (!product.getSeller().getId().equals(seller.getId())) {
            response.put("error", "Access Denied");
            response.put("message", "You do not have permission to delete this product because you are not the owner.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        productService.deleteProduct(productId);

        response.put("status", "success");
        response.put("message", "Product with ID " + productId + " was successfully deleted.");
        return ResponseEntity.ok(response);
    }
    @PutMapping("/{productId}")
    @Operation(
        summary = "Update product",
        description = "Updates a product created by the authenticated seller"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product updated successfully"),
    })
    @ApiNotFoundResponse
    public ResponseEntity<Product> updateProduct(
        @Parameter(description = "Product ID to update", required = true)
        @PathVariable Long productId,
        @RequestBody Product productDetails,
        @RequestHeader("Authorization") String jwt
    ) {
        Seller seller = sellerService.getSellerProfile(jwt);
        Product product = productService.findProductById(productId);

        if (!product.getSeller().getId().equals(seller.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(productService.updateProduct(productId, productDetails));
    }
}
