package org.riston.ecommerce.controller;

import lombok.RequiredArgsConstructor;
import org.riston.ecommerce.domain.OrderStatus;
import org.riston.ecommerce.model.Order;
import org.riston.ecommerce.model.Seller;
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

    @GetMapping()
    public ResponseEntity<List<Order>> getAllOrdersHandler(@RequestHeader("Authorization") String jwt) {
        Seller seller = sellerService.getSellerProfile(jwt);
        List<Order> orders = orderService.sellersOrder(seller.getId());

        return new ResponseEntity<>(orders, HttpStatus.ACCEPTED);
    }

    @PatchMapping("/{orderId}/status/{orderStatus}")
    public ResponseEntity<Order> updateOrderHandler(@RequestHeader("Authorization") String jwt,
                                                    @PathVariable String orderId,
                                                    @PathVariable OrderStatus orderStatus) {
        Seller seller = sellerService.getSellerProfile(jwt);
        Order order = orderService.findOrderByOrderId(orderId);
        if (!order.getSellerId().equals(seller.getId())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Order updatedOrder = orderService.updateOrderStatus(orderId, orderStatus);
        return new ResponseEntity<>(updatedOrder, HttpStatus.ACCEPTED);
    }
}