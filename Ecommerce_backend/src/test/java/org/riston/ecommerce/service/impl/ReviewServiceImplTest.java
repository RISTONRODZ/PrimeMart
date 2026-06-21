package org.riston.ecommerce.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.riston.ecommerce.exception.ResourceAccessDeniedException;
import org.riston.ecommerce.exception.ReviewNotFoundException;
import org.riston.ecommerce.model.*;
import org.riston.ecommerce.repository.ReviewRepository;
import org.riston.ecommerce.request.CreateReviewRequestDto;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    private User user;
    private Product product;
    private Review review;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        product = new Product();
        product.setId(10L);

        review = new Review();
        review.setId(100L);
        review.setUser(user);
    }

    @Test
    void createReview_ShouldSave_WhenValid() {
        CreateReviewRequestDto dto = new CreateReviewRequestDto("Great product", 5.0, null);
        when(reviewRepository.existsByUserIdAndProductId(1L, 10L)).thenReturn(false);
        when(reviewRepository.save(any(Review.class))).thenAnswer(i -> i.getArguments()[0]);

        Review saved = reviewService.createReview(dto, user, product);

        assertNotNull(saved);
        assertEquals("Great product", saved.getReviewText());
        verify(reviewRepository).save(any(Review.class));
    }

    @Test
    void createReview_ShouldThrowException_WhenAlreadyExists() {
        CreateReviewRequestDto dto = new CreateReviewRequestDto("Good", 4.0, null);
        when(reviewRepository.existsByUserIdAndProductId(1L, 10L)).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> reviewService.createReview(dto, user, product));
    }

    @Test
    void updateReview_ShouldUpdate_WhenOwnerMatches() {
        when(reviewRepository.findById(100L)).thenReturn(Optional.of(review));
        when(reviewRepository.save(any(Review.class))).thenAnswer(i -> i.getArguments()[0]);

        Review updated = reviewService.updateReview(100L, "New Text", 4.5, 1L);

        assertEquals("New Text", updated.getReviewText());
        assertEquals(4.5, updated.getRating());
    }

    @Test
    void updateReview_ShouldThrowException_WhenUserNotOwner() {
        when(reviewRepository.findById(100L)).thenReturn(Optional.of(review));

        assertThrows(ResourceAccessDeniedException.class, () ->
                reviewService.updateReview(100L, "New", 5.0, 99L));
    }

    @Test
    void deleteReview_ShouldCallRepository_WhenOwnerMatches() {
        when(reviewRepository.findById(100L)).thenReturn(Optional.of(review));

        reviewService.deleteReview(100L, 1L);

        verify(reviewRepository, times(1)).delete(review);
    }

    @Test
    void getReviewById_ShouldThrowException_WhenNotFound() {
        when(reviewRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ReviewNotFoundException.class, () -> reviewService.getReviewById(1L));
    }
}