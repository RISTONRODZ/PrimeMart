package org.riston.ecommerce.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.riston.ecommerce.annotation.ApiNotFoundResponse;
import org.riston.ecommerce.domain.OrderStatus;
import org.riston.ecommerce.model.Order;
import org.riston.ecommerce.model.Seller;
import org.riston.ecommerce.response.ApiResponseDto;
import org.riston.ecommerce.response.OrderResponse;
import org.riston.ecommerce.service.OrderService;
import org.riston.ecommerce.service.SellerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/seller/orders")
@RequiredArgsConstructor
@Tag(
        name = "Seller Orders",
        description = "Endpoints for sellers to manage and track their orders"
)
public class SellerOrderController {

    private final OrderService orderService;
    private final SellerService sellerService;

    @GetMapping
    @Operation(
            summary = "Get seller's orders",
            description = "Retrieves all orders for products sold by the authenticated seller"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully"),

    })
    public ResponseEntity<ApiResponseDto<List<OrderResponse>>> getAllOrdersHandler(
            @RequestHeader("Authorization") String jwt
    ) {
        Seller seller = sellerService.getSellerProfile(jwt);
        List<Order> orders = orderService.sellersOrder(seller.getId());

        List<OrderResponse> responseData = orders.stream()
                .map(order -> new OrderResponse(
                        order.getOrderId(),
                        order.getOrderStatus().toString(),
                        order.getTotalItem(),
                        order.getTotalSellingPrice(),
                        order.getOrderDate().toString(),
                        order.getShippingAddress().getCity()
                ))
                .toList();

        return ResponseEntity.ok(ApiResponseDto.success("Orders retrieved successfully", responseData));
    }

    @PatchMapping("/{orderId}/status/{orderStatus}")
    @Operation(
            summary = "Update order status",
            description = "Updates the status of an order for the authenticated seller"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order status updated successfully"),

    })
    @ApiNotFoundResponse
    public ResponseEntity<ApiResponseDto<Order>> updateOrderHandler(
            @RequestHeader("Authorization") String jwt,
            @Parameter(description = "Order ID to update", required = true)
            @PathVariable String orderId,
            @Parameter(description = "New order status", required = true)
            @PathVariable OrderStatus orderStatus
    ) {
        Seller seller = sellerService.getSellerProfile(jwt);
        Order order = orderService.findOrderByOrderId(orderId);

        if (!order.getSellerId().equals(seller.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponseDto.error("You do not have permission to update this order"));
        }

        Order updatedOrder = orderService.updateOrderStatus(orderId, orderStatus);
        return ResponseEntity.ok(ApiResponseDto.success("Order status updated", updatedOrder));
    }
}