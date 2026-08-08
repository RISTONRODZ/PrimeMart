package org.riston.ecommerce.service.impl;

import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.riston.ecommerce.domain.PaymentOrderStatus;
import org.riston.ecommerce.domain.PaymentStatus;
import org.riston.ecommerce.exception.PaymentGatewayException;
import org.riston.ecommerce.exception.PaymentOrderNotFoundException;
import org.riston.ecommerce.exception.PaymentValidationException;
import org.riston.ecommerce.model.*;
import org.riston.ecommerce.repository.PaymentOrderRepository;
import org.riston.ecommerce.service.PaymentService;
import org.riston.ecommerce.service.SellerReportService;
import org.riston.ecommerce.service.SellerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.riston.ecommerce.service.TransactionService;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentOrderRepository paymentOrderRepository;
    private final RazorpayClient razorpayClient;
    private final TransactionService transactionService;
    @Value("${app.frontend.success-url}")
    private String successUrl;
    private final SellerService sellerService;
    private final SellerReportService sellerReportService;

    @Override
    @Transactional
    public PaymentOrder createOrder(User user, Set<Order> orders) {
        log.info("Creating payment order for user: {}, order count: {}", user.getId(), orders.size());
        
        long amount = orders.stream()
                .mapToLong(Order::getTotalSellingPrice)
                .sum();
        
        log.info("Calculated total amount: {} rupees ({} paise)", amount, amount * 100);
        
        if (amount <= 0) {
            log.error("Invalid payment amount: {}", amount);
            throw new RuntimeException("Invalid payment amount: " + amount);
        }

        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setAmount(amount);
        paymentOrder.setUser(user);
        paymentOrder.setOrders(orders);

        PaymentOrder savedOrder = paymentOrderRepository.save(paymentOrder);
        log.info("Payment order created successfully with ID: {}, amount: {} rupees", savedOrder.getId(), savedOrder.getAmount());
        
        return savedOrder;
    }

    @Override
    public PaymentOrder getPaymentOrderById(Long orderId) {
        return paymentOrderRepository.findById(orderId)
                .orElseThrow(() ->
                        new PaymentOrderNotFoundException(
                                "Payment order not found with id: " + orderId
                        )
                );
    }

    @Override
    public PaymentOrder getPaymentOrderByPaymentId(String paymentLinkId) {
        PaymentOrder paymentOrder =
                paymentOrderRepository.findByPaymentLinkId(paymentLinkId);

        if (paymentOrder == null) {
            throw new PaymentOrderNotFoundException(
                    "Payment order not found with payment link id: " + paymentLinkId
            );
        }

        return paymentOrder;
    }

    @Transactional
    @Override
    public boolean proceedPaymentOrder(PaymentOrder paymentOrder, String paymentId) {
        log.info("Processing payment order. paymentOrderId={}, paymentId={}, currentStatus={}", 
                paymentOrder.getId(), paymentId, paymentOrder.getStatus());
        
        if (paymentOrder.getStatus() == PaymentOrderStatus.SUCCESS) {
            log.info("Payment order already in SUCCESS status. paymentOrderId={}", paymentOrder.getId());
            return true;
        }
        if (paymentOrder.getStatus() != PaymentOrderStatus.PENDING) {
            log.warn("Payment order not in PENDING status. paymentOrderId={}, status={}", 
                    paymentOrder.getId(), paymentOrder.getStatus());
            return false;
        }

        try {
            log.info("Fetching payment details from Razorpay for paymentId={}", paymentId);
            Payment payment = razorpayClient.payments.fetch(paymentId);
            String paymentStatus = payment.get("status");
            
            log.info("Razorpay payment status: {} for paymentId={}", paymentStatus, paymentId);

            if (!"captured".equals(paymentStatus)) {
                paymentOrder.setStatus(PaymentOrderStatus.FAILED);
                log.warn("Payment not captured. paymentOrderId={}, paymentId={}, status={}",
                        paymentOrder.getId(), paymentId, paymentStatus);
                return false;
            }

            String currency = payment.get("currency");
            log.info("Payment currency: {} for paymentId={}", currency, paymentId);
            
            if (!"INR".equals(currency)) {
                paymentOrder.setStatus(PaymentOrderStatus.FAILED);
                throw new PaymentValidationException("Invalid currency received: " + currency);
            }

            Long paidAmount = ((Number) payment.get("amount")).longValue();
            Long expectedAmountInPaise = paymentOrder.getAmount() * 100;
            
            log.info("Amount verification - Expected: {} paise, Paid: {} paise", expectedAmountInPaise, paidAmount);

            if (paidAmount.longValue() != expectedAmountInPaise.longValue()) {
                paymentOrder.setStatus(PaymentOrderStatus.FAILED);
                throw new PaymentValidationException(
                        String.format("Payment amount mismatch. Expected=%d, Actual=%d",
                                expectedAmountInPaise, paidAmount)
                );
            }
            
            log.info("Amount verified successfully. Processing {} orders for paymentOrderId={}", 
                    paymentOrder.getOrders().size(), paymentOrder.getId());
            
            for (Order order : paymentOrder.getOrders()) {
                order.setPaymentStatus(PaymentStatus.COMPLETED);
                transactionService.createTransaction(order);
                log.info("Order {} marked as COMPLETED", order.getOrderId());
            }
            paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);

            log.info("Payment completed successfully. paymentOrderId={}, paymentId={}",
                    paymentOrder.getId(), paymentId);

            return true;

        } catch (PaymentValidationException e) {
            log.error("Payment validation failed. paymentOrderId={}, paymentId={}", 
                    paymentOrder.getId(), paymentId, e);
            throw e;
        } catch (RazorpayException e) {
            log.error("Razorpay API error during payment verification. paymentOrderId={}, paymentId={}",
                    paymentOrder.getId(), paymentId, e);
            throw new PaymentGatewayException("Unable to verify payment with provider", e);
        } catch (Exception e) {
            log.error("Internal processing error during payment verification. paymentOrderId={}, paymentId={}",
                    paymentOrder.getId(), paymentId, e);
            throw new PaymentGatewayException("Internal processing error during verification", e);
        }
    }

    @Override
    @Transactional
    public JSONObject createRazorpayPaymentLink(User user, Long amount, Long orderId) {
        log.info("Creating Razorpay order for orderId: {}, amount: {} rupees", orderId, amount);
        log.info("Current successUrl configuration: {}", successUrl);
        
        if (amount == null || amount <= 0) {
            log.error("Invalid payment amount: {} for orderId: {}", amount, orderId);
            throw new PaymentValidationException("Invalid payment amount: " + amount);
        }

        final long RAZORPAY_MAX_AMOUNT_RUPEES = 50000L;
        if (amount > RAZORPAY_MAX_AMOUNT_RUPEES) {
            log.warn("Amount {} rupees exceeds Razorpay's maximum limit of {} rupees. Capping to maximum.", 
                      amount, RAZORPAY_MAX_AMOUNT_RUPEES);
            amount = RAZORPAY_MAX_AMOUNT_RUPEES;
        }

        try {
            Long amountInPaise = amount * 100;
            log.info("Converting amount to paise: {} rupees = {} paise", amount, amountInPaise);

            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amountInPaise);
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "order_" + orderId);
            
            log.debug("Order request payload: {}", orderRequest);

            log.info("Calling Razorpay API to create order...");
            com.razorpay.Order order = razorpayClient.orders.create(orderRequest);
            
//            log.info("Razorpay order created. ID: {}", order.get("id"));
            
            PaymentOrder paymentOrder = getPaymentOrderById(orderId);
            paymentOrder.setPaymentLinkId(order.get("id"));
            paymentOrder.setAmount(amount);
            paymentOrderRepository.save(paymentOrder);

            log.info("Order created successfully. paymentOrderId={}, razorpayOrderId={}",
                    paymentOrder.getId(), order.get("id"));

            // Return order details for frontend checkout
            JSONObject response = new JSONObject();
            response.put("order_id", (String) order.get("id"));
            response.put("amount", amountInPaise);
            response.put("currency", "INR");
            response.put("name", "Ecommerce Store");
            response.put("description", "Payment for order #" + orderId);
            
            JSONObject customer = new JSONObject();
            customer.put("name", user.getFullName());
            if (user.getEmail() != null) {
                customer.put("email", user.getEmail());
            }
            response.put("customer", customer);
            
            String callbackUrl = successUrl.endsWith("/") ? 
                successUrl + "payment-success/" + paymentOrder.getId() : 
                successUrl + "/payment-success/" + paymentOrder.getId();
            log.info("Callback URL constructed: {}", callbackUrl);
            response.put("callback_url", callbackUrl);
            
            return response;

        } catch (RazorpayException e) {
            log.error("Razorpay API error while creating order for paymentOrderId={}. Error: {}", 
                    orderId, e.getMessage(), e);
            throw new PaymentGatewayException("Failed to create order", e);
        } catch (Exception e) {
            log.error("Unexpected error while creating order for paymentOrderId={}. Error: {}", 
                    orderId, e.getMessage(), e);
            throw new PaymentGatewayException("Failed to generate order payload structure", e);
        }
    }

    @Transactional
    @Override
    public void updateSellerReports(PaymentOrder paymentOrder) {
        for (Order order : paymentOrder.getOrders()) {
            Seller seller = sellerService.getSellerById(order.getSellerId());
            SellerReport report = sellerReportService.getSellerReport(seller);

            report.setTotalOrders(report.getTotalOrders() + 1);
            report.setTotalEarnings(report.getTotalEarnings() + order.getTotalSellingPrice());

            sellerReportService.updateSellerReport(report);
            log.info("Report updated for seller: {}", seller.getId());
        }
    }
}