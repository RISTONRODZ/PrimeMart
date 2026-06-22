package org.riston.ecommerce.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.riston.ecommerce.domain.OrderStatus;
import org.riston.ecommerce.model.*;
import org.riston.ecommerce.repository.*;
import org.riston.ecommerce.service.SellerReportService;
import org.riston.ecommerce.service.SellerService;
import org.springframework.security.access.AccessDeniedException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock private OrderRepository orderRepository;
    @Mock private AddressRepository addressRepository;
    @Mock private SellerService sellerService;
    @Mock private SellerReportService sellerReportService;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private OrderServiceImpl orderService;

    private User user;
    private Cart cart;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setAddresses(new HashSet<>());
        Seller seller = new Seller();
        seller.setId(10L);

        Product product = new Product();
        product.setSeller(seller);
        CartItem item = new CartItem();
        item.setProduct(product);
        item.setSellingPrice(100);
        item.setMrpPrice(120);
        item.setQuantity(2);
        cart = new Cart();
        cart.setCartItems(new HashSet<>(Collections.singletonList(item)));
    }

    @Test
    @DisplayName("Should create orders successfully by grouping by seller")
    void createOrder_Success() {
        Address address = new Address();
        when(addressRepository.save(any(Address.class))).thenReturn(address);
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);
        Set<Order> orders = orderService.createOrder(user, address, cart);
        assertFalse(orders.isEmpty());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    @DisplayName("Should throw AccessDeniedException when canceling someone else's order")
    void cancelOrder_Unauthorized() {
        Order order = new Order();
        User owner = new User();
        owner.setId(99L);
        order.setUser(owner);

        when(orderRepository.findByOrderId("ORD-1")).thenReturn(Optional.of(order));

        assertThrows(AccessDeniedException.class, () ->
                orderService.cancelOrder("ORD-1", user)
        );
    }

    @Test
    @DisplayName("Should process cancellation and update seller report")
    void processCancelOrder_Success() {
        Order order = new Order();
        order.setUser(user);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setTotalSellingPrice(100);

        Seller seller = new Seller();
        SellerReport report = new SellerReport();
        report.setCanceledOrders(0L);
        report.setTotalRefunds(0L);

        when(orderRepository.findByOrderId("ORD-1")).thenReturn(Optional.of(order));
        when(sellerService.getSellerById(any())).thenReturn(seller);
        when(sellerReportService.getSellerReport(seller)).thenReturn(report);

        orderService.processCancelOrder("ORD-1", user);

        assertEquals(OrderStatus.CANCELED, order.getOrderStatus());
        assertEquals(1, report.getCanceledOrders());
        assertEquals(100, report.getTotalRefunds());
        verify(sellerReportService).updateSellerReport(report);
    }
}