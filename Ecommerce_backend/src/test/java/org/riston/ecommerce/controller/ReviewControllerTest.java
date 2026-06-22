package org.riston.ecommerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.riston.ecommerce.exception.GlobalExceptionHandler;
import org.riston.ecommerce.exception.ProductException;
import org.riston.ecommerce.model.Product;
import org.riston.ecommerce.model.Review;
import org.riston.ecommerce.model.User;
import org.riston.ecommerce.request.CreateReviewRequestDto;
import org.riston.ecommerce.service.ProductService;
import org.riston.ecommerce.service.ReviewService;
import org.riston.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReviewController.class)
@Import(GlobalExceptionHandler.class)
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private ReviewService reviewService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private ProductService productService;

    private User user;
    private Product product;
    private Review review;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("user@gmail.com");
        user.setFullName("xyz");
        user.setMobile("9999999999");

        product = new Product();
        product.setId(100L);

        review = new Review();
        review.setId(10L);
        review.setReviewText("Excellent Product");
        review.setRating(5);
        review.setUser(user);
        review.setCreatedAt(LocalDateTime.now());
    }

    @Nested
    @DisplayName("Get Reviews By Product Tests")
    class GetReviewsTests {

        @Test
        void getReviewsByProductId_Success() throws Exception {
            when(reviewService.getReviewByProductId(100L))
                    .thenReturn(List.of(review));

            mockMvc.perform(get("/api/v1/reviews/products/100/reviews"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].reviewText")
                            .value("Excellent Product"))
                    .andExpect(jsonPath("$[0].rating")
                            .value(5));

            verify(reviewService).getReviewByProductId(100L);
        }

        @Test
        void getReviewsByProductId_EmptyList() throws Exception {
            when(reviewService.getReviewByProductId(100L))
                    .thenReturn(List.of());

            mockMvc.perform(get("/api/v1/reviews/products/100/reviews"))
                    .andExpect(status().isOk())
                    .andExpect(content().json("[]"));
        }
    }

    @Nested
    @DisplayName("Create Review Tests")
    class CreateReviewTests {

        @Test
        void writeReview_Success() throws Exception {
            CreateReviewRequestDto dto =
                    new CreateReviewRequestDto("Excellent Product", 5.0, List.of("img1"));

            when(userService.findUserByJwtToken(anyString()))
                    .thenReturn(user);

            when(productService.findProductById(100L))
                    .thenReturn(product);

            when(reviewService.createReview(any(), any(), any()))
                    .thenReturn(review);

            mockMvc.perform(post("/api/v1/reviews/products/100/reviews")
                            .header("Authorization", "Bearer token")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.reviewText")
                            .value("Excellent Product"))
                    .andExpect(jsonPath("$.rating")
                            .value(5));

            verify(reviewService)
                    .createReview(any(), eq(user), eq(product));
        }

        @Test
        void writeReview_ProductNotFound() throws Exception {
            CreateReviewRequestDto dto =
                    new CreateReviewRequestDto("Good", 4.0, List.of("img1"));

            when(userService.findUserByJwtToken(anyString()))
                    .thenReturn(user);

            when(productService.findProductById(100L))
                    .thenThrow(new ProductException("Product not found"));

            mockMvc.perform(post("/api/v1/reviews/products/100/reviews")
                            .header("Authorization", "Bearer token")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("Update Review Tests")
    class UpdateReviewTests {

        @Test
        void updateReview_Success() throws Exception {
            CreateReviewRequestDto dto =
                    new CreateReviewRequestDto("Updated Review", 4.0, List.of("img1"));

            review.setReviewText("Updated Review");
            review.setRating(4);

            when(userService.findUserByJwtToken(anyString()))
                    .thenReturn(user);

            when(reviewService.updateReview(any(), any(), anyDouble(), any()))
                    .thenReturn(review);

            mockMvc.perform(put("/api/v1/reviews/10")
                            .header("Authorization", "Bearer token")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.reviewText").value("Updated Review"))
                    .andExpect(jsonPath("$.rating").value(4));

            verify(reviewService).updateReview(eq(10L), eq("Updated Review"), anyDouble(), eq(1L));
        }
    }

    @Nested
    @DisplayName("Delete Review Tests")
    class DeleteReviewTests {

        @Test
        void deleteReview_Success() throws Exception {
            when(userService.findUserByJwtToken(anyString()))
                    .thenReturn(user);

            doNothing().when(reviewService)
                    .deleteReview(10L, 1L);

            mockMvc.perform(delete("/api/v1/reviews/10")
                            .header("Authorization", "Bearer token"))
                    .andExpect(status().isOk())
                    .andExpect(content()
                            .string("Review deleted successfully"));

            verify(reviewService).deleteReview(10L, 1L);
        }
    }

    @Nested
    @DisplayName("Get All Reviews Tests")
    class GetAllReviewsTests {

        @Test
        void getAllReviews_Success() throws Exception {
            when(reviewService.getAllReviews())
                    .thenReturn(List.of());

            mockMvc.perform(get("/api/v1/reviews"))
                    .andExpect(status().isOk());

            verify(reviewService).getAllReviews();
        }
    }
}