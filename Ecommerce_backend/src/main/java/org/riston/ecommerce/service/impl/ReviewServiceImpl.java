package org.riston.ecommerce.service.impl;

import lombok.RequiredArgsConstructor;
import org.riston.ecommerce.exception.ResourceAccessDeniedException;
import org.riston.ecommerce.exception.ReviewNotFoundException;
import org.riston.ecommerce.model.Product;
import org.riston.ecommerce.model.Review;
import org.riston.ecommerce.model.User;
import org.riston.ecommerce.repository.ReviewRepository;
import org.riston.ecommerce.request.CreateReviewRequestDto;
import org.riston.ecommerce.service.ReviewService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    @Override
    @Transactional
    public Review createReview(CreateReviewRequestDto req, User user, Product product) {
        if (reviewRepository.existsByUserIdAndProductId(user.getId(), product.getId())) {
            throw new IllegalStateException("You have already submitted a review for this product.");
        }

        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setReviewText(req.reviewText());
        review.setRating(req.reviewRating());
        review.setProductImages(req.productImages());

        return reviewRepository.save(review);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Review> getReviewByProductId(Long productId) {
        return reviewRepository.findByProductId(productId);
    }

    @Override
    @Transactional
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
    @Transactional
    public void deleteReview(Long reviewId, Long userId) {
        Review review = getReviewById(reviewId);
        if (!review.getUser().getId().equals(userId)) {
            throw new ResourceAccessDeniedException("You don't have permission to delete this review");
        }

        reviewRepository.delete(review);
    }

    @Override
    @Transactional(readOnly = true)
    public Review getReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId).orElseThrow(() -> new ReviewNotFoundException("review not found"));
    }
}