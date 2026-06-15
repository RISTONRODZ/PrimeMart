package org.riston.ecommerce.exception;

import org.riston.ecommerce.response.ApiResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponseDto<String>> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponseDto.error(ex.getMessage()));
    }

    @ExceptionHandler({SellerNotFoundException.class, UserNotFoundException.class, ItemNotFoundException.class, OrderNotFoundException.class, OrderItemNotFoundException.class, PaymentOrderNotFoundException.class, WishlistNotFoundException.class, ReviewNotFoundException.class, ResourceNotFoundException.class, ProductException.class, CouponNotFoundException.class})
    public ResponseEntity<ApiResponseDto<String>> handleNotFoundExceptions(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponseDto.error(ex.getMessage()));
    }

    @ExceptionHandler({InvalidOtpException.class, IllegalArgumentException.class, CouponException.class, InvalidCouponException.class})
    public ResponseEntity<ApiResponseDto<String>> handleBadRequest(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponseDto.error(ex.getMessage()));
    }

    @ExceptionHandler(CouponAlreadyUsedException.class)
    public ResponseEntity<ApiResponseDto<String>> handleConflict(CouponAlreadyUsedException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponseDto.error(ex.getMessage()));
    }

    @ExceptionHandler(PaymentGatewayException.class)
    public ResponseEntity<ApiResponseDto<String>> handlePaymentGatewayException(PaymentGatewayException ex) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(ApiResponseDto.error(ex.getMessage()));
    }

    @ExceptionHandler(ResourceAccessDeniedException.class)
    public ResponseEntity<ApiResponseDto<String>> handleAccessDenied(ResourceAccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponseDto.error(ex.getMessage()));
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDto<String>> handleAllExceptions(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponseDto.error("An unexpected error occurred: " + ex.getMessage()));
    }
}