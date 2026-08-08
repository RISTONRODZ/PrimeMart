package org.riston.ecommerce.service;

import org.json.JSONObject;
import org.riston.ecommerce.model.Order;
import org.riston.ecommerce.model.PaymentOrder;
import org.riston.ecommerce.model.User;

import java.util.Set;

public interface PaymentService {
    PaymentOrder createOrder(User user, Set<Order> orders);

    PaymentOrder getPaymentOrderById(Long orderId);

    PaymentOrder getPaymentOrderByPaymentId(String orderId);

    boolean proceedPaymentOrder(PaymentOrder paymentOrder,
                                String paymentId);

    JSONObject createRazorpayPaymentLink(User user, Long amount, Long orderId);
    void updateSellerReports(PaymentOrder paymentOrder);
}
