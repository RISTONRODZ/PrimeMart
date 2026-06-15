package org.riston.ecommerce.controller;

import com.razorpay.PaymentLink;
import lombok.RequiredArgsConstructor;
import org.riston.ecommerce.model.*;
import org.riston.ecommerce.repository.PaymentOrderRepository;
import org.riston.ecommerce.response.ApiResponse; // Import added
import org.riston.ecommerce.response.PaymentLinkResponse;
import org.riston.ecommerce.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final CartService cartService;
    private final UserService userService;
    private final PaymentService paymentService;
    private final PaymentOrderRepository paymentOrderRepository;

    @PostMapping
    public ResponseEntity<ApiResponse<PaymentLinkResponse>> createOrderHandler(@RequestBody Address shippingAddress, @RequestHeader("Authorization") String jwt) {
        User user = userService.findUserByJwtToken(jwt);
        Cart cart = cartService.findUserCart(user);

        Set<Order> orders = orderService.createOrder(user, shippingAddress, cart);
        PaymentOrder paymentOrder = paymentService.createOrder(user, orders);

        PaymentLink payment = paymentService.createRazorpayPaymentLink(user, paymentOrder.getAmount(), paymentOrder.getId());

        PaymentLinkResponse res = new PaymentLinkResponse();
        res.setPayment_link_url(payment.get("short_url"));
        res.setPayment_link_id(payment.get("id"));

        paymentOrder.setPaymentLinkId(payment.get("id"));
        paymentOrderRepository.save(paymentOrder);

        return ResponseEntity.ok(ApiResponse.success("Order created successfully", res));
    }

    @GetMapping("/history")
    public ResponseEntity<ApiResponse<List<Order>>> usersOrderHistoryHandler(@RequestHeader("Authorization") String jwt) {
        User user = userService.findUserByJwtToken(jwt);
        List<Order> orders = orderService.usersOrderHistory(user.getId());
        return ResponseEntity.ok(ApiResponse.success("Order history retrieved", orders));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<Order>> getOrderById(@PathVariable String orderId) {
        Order order = orderService.findOrderByOrderId(orderId);
        return ResponseEntity.ok(ApiResponse.success("Order found", order));
    }

    @GetMapping("/item/{orderItemId}")
    public ResponseEntity<ApiResponse<OrderItem>> getOrderItemById(@PathVariable Long orderItemId) {
        OrderItem orderItem = orderService.getOrderItemById(orderItemId);
        return ResponseEntity.ok(ApiResponse.success("Order item found", orderItem));
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<ApiResponse<Order>> cancelOrder(@PathVariable String orderId, @RequestHeader("Authorization") String jwt) {
        User user = userService.findUserByJwtToken(jwt);
        orderService.processCancelOrder(orderId, user);
        return ResponseEntity.ok(ApiResponse.success("Order canceled successfully", null));
    }
}