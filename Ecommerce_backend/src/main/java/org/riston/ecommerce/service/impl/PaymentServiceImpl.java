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

    @Override
    @Transactional
    public PaymentOrder createOrder(User user, Set<Order> orders) {
        long amount = orders.stream()
                .mapToLong(Order::getTotalSellingPrice)
                .sum();
        if (amount <= 0) {
            throw new RuntimeException("Invalid payment amount: " + amount);
        }

        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setAmount(amount);
        paymentOrder.setUser(user);
        paymentOrder.setOrders(orders);

        return paymentOrderRepository.save(paymentOrder);
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
        if (paymentOrder.getStatus() == PaymentOrderStatus.SUCCESS) {
            return true;
        }
        if (paymentOrder.getStatus() != PaymentOrderStatus.PENDING) {
            return false;
        }

        try {
            Payment payment = razorpayClient.payments.fetch(paymentId);
            String paymentStatus = payment.get("status");

            if (!"captured".equals(paymentStatus)) {
                paymentOrder.setStatus(PaymentOrderStatus.FAILED);
                log.warn("Payment not captured. paymentOrderId={}, paymentId={}, status={}",
                        paymentOrder.getId(), paymentId, paymentStatus);
                return false;
            }

            String currency = payment.get("currency");
            if (!"INR".equals(currency)) {
                paymentOrder.setStatus(PaymentOrderStatus.FAILED);
                throw new PaymentValidationException("Invalid currency received: " + currency);
            }

            Long paidAmount = ((Number) payment.get("amount")).longValue();
            Long expectedAmountInPaise = paymentOrder.getAmount() * 100;

            if (paidAmount.longValue() != expectedAmountInPaise.longValue()) {
                paymentOrder.setStatus(PaymentOrderStatus.FAILED);
                throw new PaymentValidationException(
                        String.format("Payment amount mismatch. Expected=%d, Actual=%d",
                                expectedAmountInPaise, paidAmount)
                );
            }
            for (Order order : paymentOrder.getOrders()) {
                order.setPaymentStatus(PaymentStatus.COMPLETED);
                transactionService.createTransaction(order);
            }
            paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);

            log.info("Payment completed successfully. paymentOrderId={}, paymentId={}",
                    paymentOrder.getId(), paymentId);

            return true;

        } catch (PaymentValidationException e) {
            throw e;
        } catch (RazorpayException e) {
            log.error("Payment verification failed. paymentOrderId={}, paymentId={}",
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
        if (amount == null || amount <= 0) {
            throw new PaymentValidationException("Invalid payment amount: " + amount);
        }

        try {
            Long amountInPaise = amount * 100;

            JSONObject paymentLinkRequest = buildPaymentLinkRequest(user, amountInPaise, orderId);

            PaymentLink paymentLink = razorpayClient.paymentLink.create(paymentLinkRequest);
            PaymentOrder paymentOrder = getPaymentOrderById(orderId);

            paymentOrder.setPaymentLinkId(paymentLink.get("id"));

            paymentOrderRepository.save(paymentOrder);

            log.info("Payment link created successfully. paymentOrderId={}, paymentLinkId={}",
                    paymentOrder.getId(), paymentLink.get("id"));

            return paymentLink;

        } catch (RazorpayException e) {
            log.error("Failed to create payment link for paymentOrderId={}", orderId, e);
            throw new PaymentGatewayException("Failed to create payment link", e);
        } catch (Exception e) {
            log.error("Failed to build request payloads for paymentOrderId={}", orderId, e);
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
}