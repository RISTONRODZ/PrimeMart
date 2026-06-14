package org.riston.ecommerce.service.impl;

import lombok.RequiredArgsConstructor;
import org.riston.ecommerce.domain.OrderStatus;
import org.riston.ecommerce.domain.PaymentStatus;
import org.riston.ecommerce.exception.OrderItemNotFoundException;
import org.riston.ecommerce.exception.OrderNotFoundException;
import org.riston.ecommerce.model.*;
import org.riston.ecommerce.repository.AddressRepository;
import org.riston.ecommerce.repository.OrderItemRepository;
import org.riston.ecommerce.repository.OrderRepository;
import org.riston.ecommerce.repository.UserRepository;
import org.riston.ecommerce.service.OrderService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;

    @Override
    public Set<Order> createOrder(User user, Address shippingAddress, Cart cart) {
        if (!user.getAddresses().contains(shippingAddress)) {
            user.getAddresses().add(shippingAddress);
            userRepository.save(user);
        }
        Address savedAddress = addressRepository.save(shippingAddress);
        Map<Long, List<CartItem>> itemsBySeller = cart.getCartItems().stream()
                .collect(Collectors.groupingBy(item -> item.getProduct().getSeller().getId()));

        Set<Order> orders = new HashSet<>();

        for (Map.Entry<Long, List<CartItem>> entry : itemsBySeller.entrySet()) {
            Long sellerId = entry.getKey();
            List<CartItem> items = entry.getValue();

            int totalSellingPrice = items.stream().mapToInt(CartItem::getSellingPrice).sum();
            int totalMrpPrice = items.stream().mapToInt(CartItem::getMrpPrice).sum();
            int totalItems = items.stream().mapToInt(CartItem::getQuantity).sum();

            Order createdOrder = new Order();
            createdOrder.setOrderId("ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
            createdOrder.setUser(user);
            createdOrder.setSellerId(sellerId);
            createdOrder.setTotalMrpPrice(totalMrpPrice);
            createdOrder.setTotalSellingPrice(totalSellingPrice);
            createdOrder.setTotalItem(totalItems);
            createdOrder.setShippingAddress(savedAddress);
            createdOrder.setOrderStatus(OrderStatus.PENDING);
            createdOrder.getPaymentDetails().setStatus(PaymentStatus.PENDING);

            for (CartItem item : items) {
                OrderItem orderItem = new OrderItem();
                orderItem.setMrpPrice(item.getMrpPrice());
                orderItem.setSellingPrice(item.getSellingPrice());
                orderItem.setProduct(item.getProduct());
                orderItem.setQuantity(item.getQuantity());
                orderItem.setSize(item.getSize());
                orderItem.setUserId(user.getId());

                createdOrder.addOrderItem(orderItem);
            }

            Order savedOrder = orderRepository.save(createdOrder);
            orders.add(savedOrder);
        }

        return orders;
    }

    @Override
    public List<Order> usersOrderHistory(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public List<Order> sellersOrder(Long sellerId) {
        return orderRepository.findBySellerId(sellerId);
    }

    @Override
    public Order updateOrderStatus(String orderId, OrderStatus orderStatus) {
        Order order = findOrderByOrderId(orderId);
        order.setOrderStatus(orderStatus);
        return orderRepository.save(order);
    }

    @Override
    public Order cancelOrder(String orderId, User user) {
        Order order = findOrderByOrderId(orderId);
        if (!user.getId().equals(order.getUser().getId())) {
            throw new AccessDeniedException("You do not have permission to cancel this order.");
        }
        order.setOrderStatus(OrderStatus.CANCELED);
        return orderRepository.save(order);
    }

    @Override
    public OrderItem getOrderItemById(Long id) {
        return orderItemRepository.findById(id).orElseThrow(() ->
                new OrderItemNotFoundException("Order item does not exist")
        );
    }

    @Override
    public Order findOrderByOrderId(String orderId) {
        return orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + orderId));
    }
}