package org.riston.ecommerce.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.riston.ecommerce.exception.ProductException;
import org.riston.ecommerce.model.Product;
import org.riston.ecommerce.model.Review;
import org.riston.ecommerce.model.User;
import org.riston.ecommerce.request.CreateReviewRequestDto;
import org.riston.ecommerce.response.ReviewResponseDto;
import org.riston.ecommerce.response.UserSummaryDto;
import org.riston.ecommerce.service.ProductService;
import org.riston.ecommerce.service.ReviewService;
import org.riston.ecommerce.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
@Slf4j
public class ReviewController {

    private final ReviewService reviewService;
    private final UserService userService;
    private final ProductService productService;

    private ReviewResponseDto mapToDto(Review review) {
        User user = review.getUser();
        UserSummaryDto userDto = new UserSummaryDto(user.getId(), user.getEmail(), user.getFullName(), user.getMobile(), user.getRole().toString());

        return new ReviewResponseDto(review.getId(), review.getReviewText(), review.getRating(), null, userDto, review.getCreatedAt());
    }

    @GetMapping("/products/{productId}/reviews")
    public ResponseEntity<List<ReviewResponseDto>> getReviewsByProductId(@PathVariable Long productId) {
        log.info("Fetching reviews for Product ID: {}", productId);
        List<Review> reviews = reviewService.getReviewByProductId(productId);
        log.debug("Number of reviews found: {}", reviews.size());
        List<ReviewResponseDto> response = reviews.stream().map(this::mapToDto).collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/products/{productId}/reviews")
    public ResponseEntity<ReviewResponseDto> writeReview(@RequestBody CreateReviewRequestDto req, @PathVariable Long productId, @RequestHeader("Authorization") String jwt) throws ProductException {

        User user = userService.findUserByJwtToken(jwt);
        Product product = productService.findProductById(productId);

        Review review = reviewService.createReview(req, user, product);
        return ResponseEntity.ok(mapToDto(review));
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDto> updateReview(@PathVariable Long reviewId, @RequestBody CreateReviewRequestDto req, @RequestHeader("Authorization") String jwt) {

        User user = userService.findUserByJwtToken(jwt);

        Review updatedReview = reviewService.updateReview(reviewId, req.reviewText(), req.reviewRating(), user.getId());

        return ResponseEntity.ok(mapToDto(updatedReview));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable Long reviewId, @RequestHeader("Authorization") String jwt) {

        User user = userService.findUserByJwtToken(jwt);
        reviewService.deleteReview(reviewId, user.getId());

        return ResponseEntity.ok("Review deleted successfully");
    }
}