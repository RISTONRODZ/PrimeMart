package org.riston.ecommerce.controller;

import com.razorpay.PaymentLink;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.riston.ecommerce.annotation.ApiNotFoundResponse;
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
import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Slf4j
@Tag(
        name = "Order Management",
        description = "Endpoints for creating, retrieving, and managing orders"
)
public class OrderController {

    private final OrderService orderService;
    private final CartService cartService;
    private final UserService userService;
    private final PaymentService paymentService;
    private final PaymentOrderRepository paymentOrderRepository;

    @PostMapping
    @Operation(
            summary = "Create order",
            description = "Creates a new order from the user's cart with shipping address and initiates Razorpay payment"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order created successfully with payment link"),
            @ApiResponse(responseCode = "400", description = "Invalid request or cart is empty"),
    })
    public ResponseEntity<ApiResponseDto<PaymentLinkResponseDto>> createOrderHandler(
            @RequestBody Address shippingAddress,
            @RequestHeader("Authorization") String jwt
    ) {
        log.info("Creating order for user with JWT token");
        User user = userService.findUserByJwtToken(jwt);
        log.info("User found: {}", user.getId());
        
        Cart cart = cartService.findUserCart(user);
        log.info("User cart found with {} items", cart.getCartItems().size());

        Set<Order> orders = orderService.createOrder(user, shippingAddress, cart);
        log.info("Created {} orders", orders.size());
        
        PaymentOrder paymentOrder = paymentService.createOrder(user, orders);
        log.info("Payment order created with ID: {}", paymentOrder.getId());

        PaymentLink payment = paymentService.createRazorpayPaymentLink(user, paymentOrder.getAmount(), paymentOrder.getId());
        log.info("Razorpay payment link created successfully");

        PaymentLinkResponseDto res = new PaymentLinkResponseDto(
                Objects.toString(payment.get("short_url"), null),
                Objects.toString(payment.get("id"), null)
        );

        paymentOrder.setPaymentLinkId(Objects.toString(payment.get("id"), null));

        paymentOrderRepository.save(paymentOrder);
        log.info("Payment order updated with payment link ID: {}", paymentOrder.getPaymentLinkId());

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDto.success("Order created successfully", res));
    }

    @GetMapping("/history")
    @Operation(
            summary = "Get order history",
            description = "Retrieves the complete order history for the authenticated user"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order history retrieved successfully"),
    })
    public ResponseEntity<ApiResponseDto<List<OrderDto>>> usersOrderHistoryHandler(
            @RequestHeader("Authorization") String jwt
    ) {
        User user = userService.findUserByJwtToken(jwt);
        List<Order> orders = orderService.usersOrderHistory(user.getId());

        List<OrderDto> dtos = orders.stream().map(OrderDto::fromEntity).toList();

        return ResponseEntity.ok(ApiResponseDto.success("Order history retrieved", dtos));
    }

    @GetMapping("/{orderId}")
    @Operation(
            summary = "Get order by ID",
            description = "Retrieves detailed information for a specific order"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order found and retrieved"),
    })
    @ApiNotFoundResponse
    public ResponseEntity<ApiResponseDto<OrderDto>> getOrderById(
            @Parameter(description = "Unique order identifier", required = true)
            @PathVariable String orderId
    ) {
        Order order = orderService.findOrderByOrderId(orderId);
        return ResponseEntity.ok(ApiResponseDto.success("Order found", OrderDto.fromEntity(order)));
    }

    @GetMapping("/item/{orderItemId}")
    @Operation(
            summary = "Get order item by ID",
            description = "Retrieves details of a specific item within an order"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order item found"),
    })
    @ApiNotFoundResponse
    public ResponseEntity<ApiResponseDto<OrderItemDto>> getOrderItemById(
            @Parameter(description = "Unique order item identifier", required = true)
            @PathVariable Long orderItemId
    ) {
        OrderItem orderItem = orderService.getOrderItemById(orderItemId);
        return ResponseEntity.ok(ApiResponseDto.success("Order item found", OrderItemDto.fromEntity(orderItem)));
    }

    @PutMapping("/{orderId}/cancel")
    @Operation(
            summary = "Cancel order",
            description = "Cancels an existing order if it's in a cancellable state"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order cancelled successfully"),

    })
    @ApiNotFoundResponse
    public ResponseEntity<ApiResponseDto<Void>> cancelOrder(
            @Parameter(description = "Unique order identifier", required = true)
            @PathVariable String orderId,
            @RequestHeader("Authorization") String jwt
    ) {
        User user = userService.findUserByJwtToken(jwt);
        orderService.processCancelOrder(orderId, user);

        return ResponseEntity.ok(ApiResponseDto.success("Order canceled successfully", null));
    }
}