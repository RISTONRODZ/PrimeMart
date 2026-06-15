package org.riston.ecommerce.service;

import org.riston.ecommerce.model.Product;
import org.riston.ecommerce.model.Review;
import org.riston.ecommerce.model.User;
import org.riston.ecommerce.request.CreateReviewRequestDto;

import java.util.List;

public interface ReviewService {
    Review createReview(CreateReviewRequestDto req, User user, Product product);
    List<Review> getReviewByProductId(Long productId);
    Review updateReview(Long reviewId,String reviewText,double rating,Long userId);
    void deleteReview(Long reviewId,Long userId);
    Review getReviewById(Long reviewId);
}
