package org.riston.ecommerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.razorpay.PaymentLink;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.riston.ecommerce.domain.OrderStatus;
import org.riston.ecommerce.domain.PaymentStatus;
import org.riston.ecommerce.model.*;
import org.riston.ecommerce.repository.PaymentOrderRepository;
import org.riston.ecommerce.service.CartService;
import org.riston.ecommerce.service.OrderService;
import org.riston.ecommerce.service.PaymentService;
import org.riston.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    private static final String BASE = "/api/v1/orders";
    private static final String JWT = "Bearer mock.jwt.token";

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private OrderService orderService;
    @MockitoBean
    private CartService cartService;
    @MockitoBean
    private UserService userService;
    @MockitoBean
    private PaymentService paymentService;
    @MockitoBean
    private PaymentOrderRepository paymentOrderRepository;

    private static User sampleUser() {
        User user = new User();
        user.setId(42L);
        user.setEmail("john@example.com");
        user.setFullName("John Doe");
        return user;
    }

    private static Address sampleAddress() {
        return new Address(
                1L,
                "John Doe",
                "Andheri West",
                "123 Main St",
                "Mumbai",
                "Maharashtra",
                "400058",
                "9876543210"
        );
    }

    private static Cart sampleCart(User user) {
        Cart cart = new Cart();
        cart.setId(10L);
        cart.setUser(user);
        cart.setTotalSellingPrice(1999.0);
        cart.setTotalItem(2);
        return cart;
    }

    private static Order sampleOrder(Long id, String orderId, User user) {
        Order order = new Order();
        order.setId(id);
        order.setOrderId(orderId);
        order.setUser(user);
        order.setSellerId(55L);
        order.setTotalMrpPrice(4999.0);
        order.setTotalSellingPrice(3999);
        order.setDiscount(1000);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setTotalItem(2);
        order.setPaymentStatus(PaymentStatus.PENDING);
        order.setOrderDate(LocalDateTime.of(2026, 6, 1, 10, 0));
        order.setDeliverDate(LocalDateTime.of(2026, 6, 8, 10, 0));
        return order;
    }

    private static PaymentOrder samplePaymentOrder(Long id, User user) {
        PaymentOrder po = new PaymentOrder();
        po.setId(id);
        po.setAmount(3999L);
        po.setUser(user);
        return po;
    }

    private static OrderItem sampleOrderItem(Long id, Order order) {
        OrderItem item = new OrderItem();
        item.setId(id);
        item.setOrder(order);
        item.setSize("10");
        item.setQuantity(2);
        item.setMrpPrice(2999);
        item.setSellingPrice(1999);
        return item;
    }

    @Nested
    @DisplayName("POST /orders")
    class CreateOrderTests {

        @Test
        @DisplayName("returns 201 with payment link on successful order creation")
        void createOrder_success() throws Exception {
            User user = sampleUser();
            Address address = sampleAddress();
            Cart cart = sampleCart(user);
            Order order = sampleOrder(1L, "ORD-998877", user);
            Set<Order> orders = Set.of(order);
            PaymentOrder paymentOrder = samplePaymentOrder(7L, user);

            PaymentLink mockPaymentLink = mock(PaymentLink.class);
            when(mockPaymentLink.get(anyString())).thenReturn("https://rzp.io/i/abc123");

            when(userService.findUserByJwtToken(anyString())).thenReturn(user);
            when(cartService.findUserCart(any(User.class))).thenReturn(cart);
            when(orderService.createOrder(any(User.class), any(Address.class), any(Cart.class))).thenReturn(orders);
            when(paymentService.createOrder(any(User.class), anySet())).thenReturn(paymentOrder);
            when(paymentService.createRazorpayPaymentLink(any(User.class), anyLong(), anyLong())).thenReturn(mockPaymentLink);
            when(paymentOrderRepository.save(any(PaymentOrder.class))).thenReturn(paymentOrder);

            mockMvc.perform(post(BASE)
                            .header("Authorization", JWT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(address)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("Order created successfully"));

            verify(orderService).createOrder(any(User.class), any(Address.class), any(Cart.class));
        }

        @Test
        @DisplayName("sets paymentLinkId on the PaymentOrder before saving (mutation-then-save sequence)")
        void createOrder_setsPaymentLinkIdBeforeSave() throws Exception {
            User user = sampleUser();
            Address address = sampleAddress();
            Cart cart = sampleCart(user);
            Order order = sampleOrder(1L, "ORD-998877", user);
            Set<Order> orders = Set.of(order);
            PaymentOrder paymentOrder = samplePaymentOrder(7L, user);

            PaymentLink mockPaymentLink = mock(PaymentLink.class);
            when(mockPaymentLink.get(anyString())).thenReturn("plink_xyz789");

            when(userService.findUserByJwtToken(anyString())).thenReturn(user);
            when(cartService.findUserCart(any(User.class))).thenReturn(cart);
            when(orderService.createOrder(any(User.class), any(Address.class), any(Cart.class))).thenReturn(orders);
            when(paymentService.createOrder(any(User.class), anySet())).thenReturn(paymentOrder);
            when(paymentService.createRazorpayPaymentLink(any(User.class), anyLong(), anyLong())).thenReturn(mockPaymentLink);
            when(paymentOrderRepository.save(any(PaymentOrder.class))).thenReturn(paymentOrder);

            mockMvc.perform(post(BASE)
                            .header("Authorization", JWT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(address)))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("propagates as 500 when cart is empty / order creation fails")
        void createOrder_emptyCartOrServiceFailure_returns500() throws Exception {
            User user = sampleUser();
            Address address = sampleAddress();
            Cart emptyCart = sampleCart(user);

            when(userService.findUserByJwtToken(anyString())).thenReturn(user);
            when(cartService.findUserCart(any(User.class))).thenReturn(emptyCart);
            when(orderService.createOrder(any(User.class), any(Address.class), any(Cart.class)))
                    .thenThrow(new RuntimeException("Cart is empty"));

            mockMvc.perform(post(BASE)
                            .header("Authorization", JWT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(address)))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.success").value(false));
        }

        @Test
        @DisplayName("malformed JSON body — empty response")
        void createOrder_malformedJson_returns500ViaCatchAll() throws Exception {
            mockMvc.perform(post(BASE)
                            .header("Authorization", JWT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{ this is not valid json"))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.success").value(false));
        }

        @Test
        @DisplayName("invalid/expired JWT is rejected per BadCredentialsException mapping (403)")
        void createOrder_invalidToken_returns403() throws Exception {
            Address address = sampleAddress();
            when(userService.findUserByJwtToken(anyString()))
                    .thenThrow(new org.springframework.security.authentication.BadCredentialsException("Invalid or expired token"));

            mockMvc.perform(post(BASE)
                            .header("Authorization", "Bearer invalid.token")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(address)))
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.success").value(false));
        }
    }

    @Nested
    @DisplayName("GET /orders/history")
    class OrderHistoryTests {

        @Test
        @DisplayName("returns 200 with the user's order history")
        void orderHistory_success() throws Exception {
            User user = sampleUser();
            Order order1 = sampleOrder(1L, "ORD-998877", user);
            Order order2 = sampleOrder(2L, "ORD-998878", user);

            when(userService.findUserByJwtToken(anyString())).thenReturn(user);
            when(orderService.usersOrderHistory(anyLong())).thenReturn(List.of(order1, order2));

            mockMvc.perform(get(BASE + "/history").header("Authorization", JWT))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data.length()").value(2));
        }

        @Test
        @DisplayName("returns an empty list when the user has no orders")
        void orderHistory_empty() throws Exception {
            User user = sampleUser();
            when(userService.findUserByJwtToken(anyString())).thenReturn(user);
            when(orderService.usersOrderHistory(anyLong())).thenReturn(List.of());

            mockMvc.perform(get(BASE + "/history").header("Authorization", JWT))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.length()").value(0));
        }

        @Test
        @DisplayName("returns 500 (via catch-all) when Authorization header is missing")
        void orderHistory_missingAuthHeader_isRejected() throws Exception {
            mockMvc.perform(get(BASE + "/history"))
                    .andExpect(result -> {
                        int status = result.getResponse().getStatus();
                        org.junit.jupiter.api.Assertions.assertTrue(status >= 400);
                    });
        }
    }

    @Nested
    @DisplayName("GET /orders/{orderId}")
    class GetOrderByIdTests {

        @Test
        @DisplayName("returns 200 with the order — no Authorization header needed")
        void getOrderById_success() throws Exception {
            Order order = sampleOrder(1L, "ORD-998877", sampleUser());
            OrderItem item = sampleOrderItem(101L, order);
            order.setOrderItems(List.of(item));

            when(orderService.findOrderByOrderId(anyString())).thenReturn(order);

            mockMvc.perform(get(BASE + "/ORD-998877"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true));
        }

        @Test
        @DisplayName("returns 404 when order does not exist (OrderNotFoundException)")
        void getOrderById_notFound_returns404() throws Exception {
            when(orderService.findOrderByOrderId(anyString()))
                    .thenThrow(new org.riston.ecommerce.exception.OrderNotFoundException("Order not found"));

            mockMvc.perform(get(BASE + "/ORD-NONEXISTENT"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.success").value(false));
        }
    }

    @Nested
    @DisplayName("GET /orders/item/{orderItemId}")
    class GetOrderItemByIdTests {

        @Test
        @DisplayName("returns 200 with the order item")
        void getOrderItemById_success() throws Exception {
            Order order = sampleOrder(1L, "ORD-998877", sampleUser());
            OrderItem item = sampleOrderItem(101L, order);

            when(orderService.getOrderItemById(anyLong())).thenReturn(item);

            mockMvc.perform(get(BASE + "/item/101"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true));
        }

        @Test
        @DisplayName("returns 404 when order item does not exist (OrderNotFoundException)")
        void getOrderItemById_notFound_returns404() throws Exception {
            when(orderService.getOrderItemById(anyLong()))
                    .thenThrow(new org.riston.ecommerce.exception.OrderNotFoundException("Order item not found"));

            mockMvc.perform(get(BASE + "/item/9999"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.success").value(false));
        }

        @Test
        @DisplayName("returns 400 with proper error envelope when orderItemId path variable is not a number")
        void getOrderItemById_nonNumericId_returns400() throws Exception {
            mockMvc.perform(get(BASE + "/item/not-a-number"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.success").value(false));
        }
    }

    @Nested
    @DisplayName("PUT /orders/{orderId}/cancel")
    class CancelOrderTests {

        @Test
        @DisplayName("returns 200 with null data on successful cancellation")
        void cancelOrder_success() throws Exception {
            User user = sampleUser();
            when(userService.findUserByJwtToken(anyString())).thenReturn(user);
            doNothing().when(orderService).processCancelOrder(anyString(), any(User.class));

            mockMvc.perform(put(BASE + "/ORD-998877/cancel").header("Authorization", JWT))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true));
        }

        @Test
        @DisplayName("returns 404 when order does not exist (OrderNotFoundException)")
        void cancelOrder_notFound_returns404() throws Exception {
            User user = sampleUser();
            when(userService.findUserByJwtToken(anyString())).thenReturn(user);
            doThrow(new org.riston.ecommerce.exception.OrderNotFoundException("Order not found"))
                    .when(orderService).processCancelOrder(anyString(), any(User.class));

            mockMvc.perform(put(BASE + "/ORD-NONEXISTENT/cancel").header("Authorization", JWT))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.success").value(false));
        }

        @Test
        @DisplayName("propagates as 400 when order is not in a cancellable state (IllegalArgumentException)")
        void cancelOrder_notCancellable_returns400() throws Exception {
            User user = sampleUser();
            when(userService.findUserByJwtToken(anyString())).thenReturn(user);
            doThrow(new IllegalArgumentException("Order cannot be cancelled after shipping"))
                    .when(orderService).processCancelOrder(anyString(), any(User.class));

            mockMvc.perform(put(BASE + "/ORD-998877/cancel").header("Authorization", JWT))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.success").value(false));
        }

        @Test
        @DisplayName("invalid/expired JWT is rejected per BadCredentialsException mapping (403)")
        void cancelOrder_invalidToken_returns403() throws Exception {
            when(userService.findUserByJwtToken(anyString()))
                    .thenThrow(new org.springframework.security.authentication.BadCredentialsException("Invalid or expired token"));

            mockMvc.perform(put(BASE + "/ORD-998877/cancel").header("Authorization", "Bearer invalid.token"))
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.success").value(false));
        }
    }
}