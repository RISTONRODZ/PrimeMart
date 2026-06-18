package org.riston.ecommerce.controller;

import lombok.RequiredArgsConstructor;
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
public class SellerOrderController {

    private final OrderService orderService;
    private final SellerService sellerService;

    @GetMapping
    public ResponseEntity<ApiResponseDto<List<OrderResponse>>> getAllOrdersHandler(@RequestHeader("Authorization") String jwt) {
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
    public ResponseEntity<ApiResponseDto<Order>> updateOrderHandler(@RequestHeader("Authorization") String jwt,
                                                                    @PathVariable String orderId,
                                                                    @PathVariable OrderStatus orderStatus) {
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