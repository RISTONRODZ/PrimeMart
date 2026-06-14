package org.riston.ecommerce.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.riston.ecommerce.model.*;
import org.riston.ecommerce.response.ApiResponse;
import org.riston.ecommerce.service.PaymentService;
import org.riston.ecommerce.service.SellerReportService;
import org.riston.ecommerce.service.SellerService;
import org.riston.ecommerce.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {
    private final PaymentService paymentService;
    private final UserService userService;
    private final SellerService sellerService;
    private final SellerReportService sellerReportService;
    @GetMapping("/{paymentId}")
    public ResponseEntity<ApiResponse> paymentSuccessHandler(@PathVariable String paymentId,
                                                             @RequestParam String paymentLinkId, @RequestHeader("Authorization") String jwt){
        @SuppressWarnings("unused")
        User user = userService.findUserByJwtToken(jwt);
        PaymentOrder paymentOrder = paymentService.getPaymentOrderByPaymentId(paymentLinkId);
        boolean paymentSuccess = paymentService.proceedPaymentOrder(
                paymentOrder,
                paymentId
        );
        if(paymentSuccess){
            for(Order order : paymentOrder.getOrders()){
//                transactionService.createTransaction(order);
                Seller seller = sellerService.getSellerById(order.getSellerId());
                SellerReport report = sellerReportService.getSellerReport(seller);
                report.setTotalOrders(report.getTotalOrders()+1);
                report.setTotalEarnings(report.getTotalEarnings()+order.getTotalSellingPrice());
                SellerReport updatedReport = sellerReportService.updateSellerReport(report);
                log.info("Report updated for seller: {}, new total earnings: {}", seller.getId(), updatedReport.getTotalEarnings());
            }
        }
        ApiResponse res = new ApiResponse();
        res.setMessage("Payment successful");
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }
}
