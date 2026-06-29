package org.riston.ecommerce.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.riston.ecommerce.annotation.ApiNotFoundResponse;
import org.riston.ecommerce.exception.ProductException;
import org.riston.ecommerce.model.Product;
import org.riston.ecommerce.model.Review;
import org.riston.ecommerce.model.User;
import org.riston.ecommerce.request.CreateReviewRequestDto;
import org.riston.ecommerce.response.ApiResponseDto;
import org.riston.ecommerce.response.ReviewResponseDto;
import org.riston.ecommerce.response.UserSummaryDto;
import org.riston.ecommerce.service.ProductService;
import org.riston.ecommerce.service.ReviewService;
import org.riston.ecommerce.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Review Management", description = "Endpoints for viewing, creating, updating, and deleting product reviews.")
public class ReviewController {

    private final ReviewService reviewService;
    private final UserService userService;
    private final ProductService productService;

    private ReviewResponseDto mapToDto(Review review) {
        User user = review.getUser();
        UserSummaryDto userDto = new UserSummaryDto(user.getId(), user.getEmail(), user.getFullName(), user.getMobile(), user.getRole().toString());
        return new ReviewResponseDto(review.getId(), review.getReviewText(), review.getRating(), review.getProductImages(), userDto, review.getCreatedAt());
    }

    @GetMapping("/products/{productId}/reviews")
    @Operation(summary = "Get reviews for a product", description = "Retrieves all reviews associated with a specific product ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(schema = @Schema(implementation = ReviewResponseDto.class))),
               })
    @ApiNotFoundResponse
    public ResponseEntity<List<ReviewResponseDto>> getReviewsByProductId(@Parameter(description = "The unique identifier of the product", required = true) @PathVariable Long productId) {
        List<Review> reviews = reviewService.getReviewByProductId(productId);
        List<ReviewResponseDto> response = reviews.stream().map(this::mapToDto).collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/products/{productId}/reviews")
    @Operation(summary = "Write a review", description = "Allows an authenticated user to post a review and rating for a product.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Review created successfully", content = @Content(schema = @Schema(implementation = ReviewResponseDto.class))),
    })
    @ApiNotFoundResponse
    public ResponseEntity<ReviewResponseDto> writeReview(@Valid @RequestBody CreateReviewRequestDto req, @Parameter(description = "The unique identifier of the product to write a review", required = true) @PathVariable Long productId, @Parameter(description = "JWT token", required = true) @RequestHeader("Authorization") String jwt) throws ProductException {
        User user = userService.findUserByJwtToken(jwt);
        Product product = productService.findProductById(productId);
        Review review = reviewService.createReview(req, user, product);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToDto(review));
    }

    @PutMapping("/{reviewId}")
    @Operation(summary = "Update a review by id", description = "Allows an authenticated user to edit their existing review text and rating.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review updated successfully",
                    content = @Content(schema = @Schema(implementation = ReviewResponseDto.class))),
    })
    @ApiNotFoundResponse
    public ResponseEntity<ReviewResponseDto> updateReview(@Parameter(description = "The unique identifier of the review to update", required = true) @PathVariable Long reviewId, @RequestBody CreateReviewRequestDto req, @Parameter(description = "JWT token", required = true) @RequestHeader("Authorization") String jwt) {
        User user = userService.findUserByJwtToken(jwt);
        Review updatedReview = reviewService.updateReview(reviewId, req.reviewText(), req.reviewRating(), user.getId());
        return ResponseEntity.ok(mapToDto(updatedReview));
    }

    @DeleteMapping("/{reviewId}")
    @Operation(summary = "Delete a review by id", description = "Allows an authenticated user to delete their own review.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review deleted successfully", content = @Content(schema = @Schema(implementation = ApiResponseDto.class))),

    })
    public ResponseEntity<String> deleteReview(@Parameter(description = "The unique identifier of the review to delete", required = true) @PathVariable Long reviewId, @Parameter(description = "JWT token", required = true) @RequestHeader("Authorization") String jwt) {
        User user = userService.findUserByJwtToken(jwt);
        reviewService.deleteReview(reviewId, user.getId());
        return ResponseEntity.ok("Review deleted successfully");
    }

    @GetMapping
    @Operation(summary = "Get all reviews", description = "Retrieves a list of all reviews in the system.")
    @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = ReviewResponseDto.class)))
    public ResponseEntity<List<ReviewResponseDto>> getAllReviews() {
        return ResponseEntity.ok(reviewService.getAllReviews());
    }
}