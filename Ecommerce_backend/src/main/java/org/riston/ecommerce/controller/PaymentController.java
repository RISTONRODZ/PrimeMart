package org.riston.ecommerce.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.riston.ecommerce.model.*;
import org.riston.ecommerce.response.ApiResponseDto;
import org.riston.ecommerce.service.PaymentService;
import org.riston.ecommerce.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {
    private final PaymentService paymentService;
    private final UserService userService;
    @GetMapping("/{paymentId}")
    public ResponseEntity<ApiResponseDto<PaymentOrder>> paymentSuccessHandler(
            @PathVariable String paymentId,
            @RequestParam String paymentLinkId,
            @RequestHeader("Authorization") String jwt) {
        userService.findUserByJwtToken(jwt);
        PaymentOrder paymentOrder = paymentService.getPaymentOrderByPaymentId(paymentLinkId);
        boolean paymentSuccess = paymentService.proceedPaymentOrder(paymentOrder, paymentId);

        if (paymentSuccess) {
            paymentService.updateSellerReports(paymentOrder);
            return ResponseEntity.ok(ApiResponseDto.success("Payment successful", paymentOrder));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponseDto.error("Payment verification failed"));
    }
}
