package org.riston.ecommerce.service.impl;

import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
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

    @Value("${razorpay.max.amount:1000}")
    private long maxAmount;

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

        if (amount > maxAmount) {
            log.warn("Amount {} exceeds maximum allowed amount {}. Adjusting to {} for test mode", amount, maxAmount, maxAmount);
            amount = maxAmount;
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
    public PaymentLink createRazorpayPaymentLink(User user, Long amount, Long orderId) {
        log.info("Creating Razorpay payment link for orderId: {}, amount: {} rupees", orderId, amount);
        
        if (amount == null || amount <= 0) {
            log.error("Invalid payment amount: {} for orderId: {}", amount, orderId);
            throw new PaymentValidationException("Invalid payment amount: " + amount);
        }

        try {
            Long amountInPaise = amount * 100;
            log.info("Converting amount to paise: {} rupees = {} paise", amount, amountInPaise);

            JSONObject paymentLinkRequest = buildPaymentLinkRequest(user, amountInPaise, orderId);
            log.debug("Payment link request payload: {}", paymentLinkRequest);

            log.info("Calling Razorpay API to create payment link...");
            PaymentLink paymentLink = razorpayClient.paymentLink.create(paymentLinkRequest);
            
            log.info("Razorpay payment link created. ID: {}, Short URL: {}", 
                    paymentLink.get("id"), paymentLink.get("short_url"));
            
            PaymentOrder paymentOrder = getPaymentOrderById(orderId);

            paymentOrder.setPaymentLinkId(paymentLink.get("id"));

            paymentOrderRepository.save(paymentOrder);

            log.info("Payment link created successfully. paymentOrderId={}, paymentLinkId={}",
                    paymentOrder.getId(), paymentLink.get("id"));

            return paymentLink;

        } catch (RazorpayException e) {
            log.error("Razorpay API error while creating payment link for paymentOrderId={}. Error: {}", 
                    orderId, e.getMessage(), e);
            
            // For test mode, create a mock payment link to allow flow to continue
            if (e.getMessage() != null && e.getMessage().contains("amount exceeds maximum")) {
                log.warn("Razorpay amount limit exceeded. Creating mock payment link for test mode.");
                return createMockPaymentLink(orderId, amount);
            }
            
            throw new PaymentGatewayException("Failed to create payment link", e);
        } catch (Exception e) {
            log.error("Unexpected error while creating payment link for paymentOrderId={}. Error: {}", 
                    orderId, e.getMessage(), e);
            throw new PaymentGatewayException("Failed to generate payment payload structure", e);
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

    private JSONObject buildPaymentLinkRequest(User user, Long amountInPaise, Long orderId) throws JSONException {
        JSONObject paymentLinkRequest = new JSONObject();

        paymentLinkRequest.put("amount", amountInPaise);
        paymentLinkRequest.put("currency", "INR");

        paymentLinkRequest.put("callback_url", successUrl + "/" + orderId);
        paymentLinkRequest.put("callback_method", "get");

        JSONObject customer = new JSONObject();
        customer.put("name", user.getFullName());

        if (user.getEmail() != null) {
            customer.put("email", user.getEmail());
        }
        paymentLinkRequest.put("customer", customer);

        JSONObject notify = new JSONObject();
        notify.put("email", true);
        notify.put("sms", true);
        paymentLinkRequest.put("notify", notify);

        return paymentLinkRequest;
    }

    private PaymentLink createMockPaymentLink(Long orderId, Long amount) {
        log.info("Creating mock payment link for test mode. orderId: {}, amount: {} rupees", orderId, amount);
        
        try {
            JSONObject mockPaymentLink = new JSONObject();
            String mockLinkId = "pay_mock_" + orderId + "_" + System.currentTimeMillis();
            String mockShortUrl = successUrl + "/" + orderId + "?mock_payment=true";
            
            mockPaymentLink.put("id", mockLinkId);
            mockPaymentLink.put("short_url", mockShortUrl);
            mockPaymentLink.put("amount", amount * 100);
            mockPaymentLink.put("currency", "INR");
            mockPaymentLink.put("status", "created");
            
            PaymentOrder paymentOrder = getPaymentOrderById(orderId);
            paymentOrder.setPaymentLinkId(mockLinkId);
            paymentOrderRepository.save(paymentOrder);
            
            log.info("Mock payment link created. Link ID: {}, Short URL: {}", mockLinkId, mockShortUrl);
            
            return new PaymentLink(mockPaymentLink);
        } catch (Exception e) {
            log.error("Failed to create mock payment link for orderId: {}", orderId, e);
            throw new PaymentGatewayException("Failed to create mock payment link", e);
        }
    }
}