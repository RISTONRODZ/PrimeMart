package org.riston.ecommerce.controller;

import com.razorpay.PaymentLink;
import lombok.RequiredArgsConstructor;
import org.riston.ecommerce.model.*;
import org.riston.ecommerce.repository.PaymentOrderRepository;
import org.riston.ecommerce.response.ApiResponseDto;
import org.riston.ecommerce.response.OrderDto;
import org.riston.ecommerce.response.OrderItemDto;
import org.riston.ecommerce.response.PaymentLinkResponseDto;
import org.riston.ecommerce.service.CartService;
import org.riston.ecommerce.service.OrderService;
import org.riston.ecommerce.service.PaymentService;
import org.riston.ecommerce.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final CartService cartService;
    private final UserService userService;
    private final PaymentService paymentService;
    private final PaymentOrderRepository paymentOrderRepository;

    @PostMapping
    public ResponseEntity<ApiResponseDto<PaymentLinkResponseDto>> createOrderHandler(@RequestBody Address shippingAddress, @RequestHeader("Authorization") String jwt) {
        User user = userService.findUserByJwtToken(jwt);
        Cart cart = cartService.findUserCart(user);

        Set<Order> orders = orderService.createOrder(user, shippingAddress, cart);
        PaymentOrder paymentOrder = paymentService.createOrder(user, orders);

        PaymentLink payment = paymentService.createRazorpayPaymentLink(user, paymentOrder.getAmount(), paymentOrder.getId());

        PaymentLinkResponseDto res = new PaymentLinkResponseDto(String.valueOf(payment.get("short_url")), String.valueOf(payment.get("id")));

        paymentOrder.setPaymentLinkId(String.valueOf(payment.get("id")));

        paymentOrderRepository.save(paymentOrder);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDto.success("Order created successfully", res));
    }

    @GetMapping("/history")
    public ResponseEntity<ApiResponseDto<List<OrderDto>>> usersOrderHistoryHandler(@RequestHeader("Authorization") String jwt) {
        User user = userService.findUserByJwtToken(jwt);
        List<Order> orders = orderService.usersOrderHistory(user.getId());

        List<OrderDto> dtos = orders.stream().map(OrderDto::fromEntity).toList();

        return ResponseEntity.ok(ApiResponseDto.success("Order history retrieved", dtos));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponseDto<OrderDto>> getOrderById(@PathVariable String orderId) {
        Order order = orderService.findOrderByOrderId(orderId);
        return ResponseEntity.ok(ApiResponseDto.success("Order found", OrderDto.fromEntity(order)));
    }

    @GetMapping("/item/{orderItemId}")
    public ResponseEntity<ApiResponseDto<OrderItemDto>> getOrderItemById(@PathVariable Long orderItemId) {
        OrderItem orderItem = orderService.getOrderItemById(orderItemId);
        return ResponseEntity.ok(ApiResponseDto.success("Order item found", OrderItemDto.fromEntity(orderItem)));
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<ApiResponseDto<Void>> cancelOrder(@PathVariable String orderId, @RequestHeader("Authorization") String jwt) {
        User user = userService.findUserByJwtToken(jwt);
        orderService.processCancelOrder(orderId, user);

        return ResponseEntity.ok(ApiResponseDto.success("Order canceled successfully", null));
    }
}