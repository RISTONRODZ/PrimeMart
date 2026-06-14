package org.riston.ecommerce.service.impl;

import lombok.RequiredArgsConstructor;
import org.riston.ecommerce.exception.ResourceAccessDeniedException;
import org.riston.ecommerce.exception.ReviewNotFoundException;
import org.riston.ecommerce.model.Product;
import org.riston.ecommerce.model.Review;
import org.riston.ecommerce.model.User;
import org.riston.ecommerce.repository.ReviewRepository;
import org.riston.ecommerce.request.CreateReviewRequest;
import org.riston.ecommerce.service.ReviewService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    @Override
    public Review createReview(CreateReviewRequest req, User user, Product product) {
        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setReviewText(req.getReviewText());
        review.setRating(req.getReviewRating());
        review.setProductImages(req.getProductImages());

        product.getReviews().add(review);
        return reviewRepository.save(review);
    }

    @Override
    public List<Review> getReviewByProductId(Long productId) {
        return reviewRepository.findByProductId(productId);
    }

    @Override
    public Review updateReview(Long reviewId, String reviewText, double rating, Long userId) {
        Review review = getReviewById(reviewId);

        if (review.getUser().getId().equals(userId)) {
            review.setReviewText(reviewText);
            review.setRating(rating);
            return reviewRepository.save(review);
        }

        throw new ResourceAccessDeniedException("you can't update this review");
    }

    @Override
    public void deleteReview(Long reviewId, Long userId) {
        Review review = getReviewById(reviewId);
        if (!review.getUser().getId().equals(userId)) {
            throw new ResourceAccessDeniedException("You don't have permission to delete this review");
        }

        reviewRepository.delete(review);
    }

    @Override
    public Review getReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId).orElseThrow(()-> new ReviewNotFoundException("review not found"));
    }
}
