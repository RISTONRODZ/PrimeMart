package org.riston.ecommerce.service;

import org.riston.ecommerce.domain.OrderStatus;
import org.riston.ecommerce.exception.OrderNotFoundException;
import org.riston.ecommerce.model.*;

import java.util.List;
import java.util.Set;

public interface OrderService {
    Set<Order> createOrder(User user, Address shippingAddress, Cart cart);
    List<Order> usersOrderHistory(Long userId);
    List<Order> sellersOrder(Long userId);
    Order updateOrderStatus(String orderId, OrderStatus orderStatus);
    Order cancelOrder(String orderId,User user);
    OrderItem getOrderItemById(Long Id) throws OrderNotFoundException;
    Order findOrderByOrderId(String orderId);
}
