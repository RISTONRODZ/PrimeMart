package org.riston.ecommerce.service.impl;

import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.riston.ecommerce.domain.PaymentOrderStatus;
import org.riston.ecommerce.domain.PaymentStatus;
import org.riston.ecommerce.exception.PaymentGatewayException;
import org.riston.ecommerce.exception.PaymentOrderNotFoundException;
import org.riston.ecommerce.exception.PaymentValidationException;
import org.riston.ecommerce.model.Order;
import org.riston.ecommerce.model.PaymentOrder;
import org.riston.ecommerce.model.Seller;
import org.riston.ecommerce.model.SellerReport;
import org.riston.ecommerce.model.User;
import org.riston.ecommerce.repository.PaymentOrderRepository;
import org.riston.ecommerce.service.SellerReportService;
import org.riston.ecommerce.service.SellerService;
import org.riston.ecommerce.service.TransactionService;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import java.util.Set;

import com.razorpay.PaymentClient;
import com.razorpay.PaymentLinkClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceImplTest {

    @Mock
    private PaymentOrderRepository paymentOrderRepository;
    @Mock
    private RazorpayClient razorpayClient;
    @Mock
    private TransactionService transactionService;
    @Mock
    private SellerService sellerService;
    @Mock
    private SellerReportService sellerReportService;
    @Mock
    private PaymentClient payments;
    @Mock
    private PaymentLinkClient paymentLink;
    @InjectMocks
    private PaymentServiceImpl paymentService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(paymentService, "successUrl", "http://localhost:3000/payment-success");
        ReflectionTestUtils.setField(paymentService, "maxAmount", 10000L);
        razorpayClient.payments = this.payments;
        razorpayClient.paymentLink = this.paymentLink;
    }

    @Test
    @DisplayName("Should successfully create a payment order from a set of user orders")
    void createOrder_Success() {
        User user = new User();
        user.setId(1L);

        Order order1 = new Order();
        order1.setTotalSellingPrice(400);
        Order order2 = new Order();
        order2.setTotalSellingPrice(600);

        Set<Order> orders = Set.of(order1, order2);

        when(paymentOrderRepository.save(any(PaymentOrder.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PaymentOrder result = paymentService.createOrder(user, orders);

        assertNotNull(result);
        assertEquals(1000L, result.getAmount());
        assertEquals(user, result.getUser());
        assertEquals(orders, result.getOrders());
        verify(paymentOrderRepository, times(1)).save(any(PaymentOrder.class));
    }

    @Test
    @DisplayName("Should throw RuntimeException when total orders value is zero or negative")
    void createOrder_InvalidAmount_ThrowsException() {
        User user = new User();
        Order order = new Order();
        order.setTotalSellingPrice(0);

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                paymentService.createOrder(user, Set.of(order))
        );

        assertTrue(exception.getMessage().contains("Invalid payment amount"));
        verify(paymentOrderRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should find payment order by ID successfully")
    void getPaymentOrderById_Success() {
        Long orderId = 1L;
        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setId(orderId);

        when(paymentOrderRepository.findById(orderId)).thenReturn(Optional.of(paymentOrder));

        PaymentOrder result = paymentService.getPaymentOrderById(orderId);

        assertNotNull(result);
        assertEquals(orderId, result.getId());
    }

    @Test
    @DisplayName("Should throw PaymentOrderNotFoundException when payment order ID does not exist")
    void getPaymentOrderById_NotFound_ThrowsException() {
        Long orderId = 99L;
        when(paymentOrderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(PaymentOrderNotFoundException.class, () ->
                paymentService.getPaymentOrderById(orderId)
        );
    }

    @Test
    @DisplayName("Should find payment order by payment link ID successfully")
    void getPaymentOrderByPaymentId_Success() {
        String paymentLinkId = "plink_123";
        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setPaymentLinkId(paymentLinkId);

        when(paymentOrderRepository.findByPaymentLinkId(paymentLinkId)).thenReturn(paymentOrder);

        PaymentOrder result = paymentService.getPaymentOrderByPaymentId(paymentLinkId);

        assertNotNull(result);
        assertEquals(paymentLinkId, result.getPaymentLinkId());
    }

    @Test
    @DisplayName("Should throw PaymentOrderNotFoundException when payment link ID does not exist")
    void getPaymentOrderByPaymentId_NotFound_ThrowsException() {
        String paymentLinkId = "plink_invalid";
        when(paymentOrderRepository.findByPaymentLinkId(paymentLinkId)).thenReturn(null);

        assertThrows(PaymentOrderNotFoundException.class, () ->
                paymentService.getPaymentOrderByPaymentId(paymentLinkId)
        );
    }

    @Test
    @DisplayName("Should bypass processing and return true if payment order status is already SUCCESS")
    void proceedPaymentOrder_AlreadySuccess_ReturnsTrue() {
        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);

        boolean result = paymentService.proceedPaymentOrder(paymentOrder, "pay_123");

        assertTrue(result);
        verifyNoInteractions(razorpayClient);
    }

    @Test
    @DisplayName("Should return false if payment order status is not PENDING")
    void proceedPaymentOrder_NotPending_ReturnsFalse() {
        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setStatus(PaymentOrderStatus.FAILED);

        boolean result = paymentService.proceedPaymentOrder(paymentOrder, "pay_123");

        assertFalse(result);
        verifyNoInteractions(razorpayClient);
    }

    @Test
    @DisplayName("Should verify payment captured successfully and update associated orders and transactions")
    void proceedPaymentOrder_Captured_Success() throws RazorpayException {
        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setStatus(PaymentOrderStatus.PENDING);
        paymentOrder.setAmount(500L);

        Order order = new Order();
        order.setPaymentStatus(PaymentStatus.PENDING);
        paymentOrder.setOrders(Set.of(order));

        Payment mockPayment = mock(Payment.class);
        when(mockPayment.get("status")).thenReturn("captured");
        when(mockPayment.get("currency")).thenReturn("INR");
        when(mockPayment.get("amount")).thenReturn(50000L);

        when(payments.fetch("pay_123")).thenReturn(mockPayment);

        boolean result = paymentService.proceedPaymentOrder(paymentOrder, "pay_123");

        assertTrue(result);
        assertEquals(PaymentOrderStatus.SUCCESS, paymentOrder.getStatus());
        assertEquals(PaymentStatus.COMPLETED, order.getPaymentStatus());
        verify(transactionService, times(1)).createTransaction(order);
    }

    @Test
    @DisplayName("Should mark status as FAILED and return false when payment status is not captured")
    void proceedPaymentOrder_NotCaptured_ReturnsFalse() throws RazorpayException {
        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setStatus(PaymentOrderStatus.PENDING);

        Payment mockPayment = mock(Payment.class);
        when(mockPayment.get("status")).thenReturn("authorized");

        when(payments.fetch("pay_123")).thenReturn(mockPayment);

        boolean result = paymentService.proceedPaymentOrder(paymentOrder, "pay_123");

        assertFalse(result);
        assertEquals(PaymentOrderStatus.FAILED, paymentOrder.getStatus());
    }

    @Test
    @DisplayName("Should throw PaymentValidationException if payment currency is not INR")
    void proceedPaymentOrder_InvalidCurrency_ThrowsException() throws RazorpayException {
        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setStatus(PaymentOrderStatus.PENDING);

        Payment mockPayment = mock(Payment.class);
        when(mockPayment.get("status")).thenReturn("captured");
        when(mockPayment.get("currency")).thenReturn("USD");

        when(payments.fetch("pay_123")).thenReturn(mockPayment);

        assertThrows(PaymentValidationException.class, () ->
                paymentService.proceedPaymentOrder(paymentOrder, "pay_123")
        );
        assertEquals(PaymentOrderStatus.FAILED, paymentOrder.getStatus());
    }

    @Test
    @DisplayName("Should throw PaymentValidationException if payment amount does not match expected amount")
    void proceedPaymentOrder_AmountMismatch_ThrowsException() throws RazorpayException {
        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setStatus(PaymentOrderStatus.PENDING);
        paymentOrder.setAmount(1000L);

        Payment mockPayment = mock(Payment.class);
        when(mockPayment.get("status")).thenReturn("captured");
        when(mockPayment.get("currency")).thenReturn("INR");
        when(mockPayment.get("amount")).thenReturn(50000L);

        when(payments.fetch("pay_123")).thenReturn(mockPayment);

        assertThrows(PaymentValidationException.class, () ->
                paymentService.proceedPaymentOrder(paymentOrder, "pay_123")
        );
        assertEquals(PaymentOrderStatus.FAILED, paymentOrder.getStatus());
    }

    @Test
    @DisplayName("Should throw PaymentGatewayException if Razorpay client encounters an API exception")
    void proceedPaymentOrder_RazorpayException_ThrowsGatewayException() throws RazorpayException {
        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setStatus(PaymentOrderStatus.PENDING);

        when(payments.fetch("pay_123")).thenThrow(new RazorpayException("API Failure"));

        assertThrows(PaymentGatewayException.class, () ->
                paymentService.proceedPaymentOrder(paymentOrder, "pay_123")
        );
    }

    @Test
    @DisplayName("Should successfully construct a payload request and create an external payment link")
    void createRazorpayPaymentLink_Success() throws RazorpayException {
        User user = new User();
        user.setFullName("John Doe");
        user.setEmail("johndoe@gmail.com");

        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setId(10L);

        PaymentLink mockLink = mock(PaymentLink.class);
        when(mockLink.get("id")).thenReturn("plink_98765");

        when(paymentLink.create(any(JSONObject.class))).thenReturn(mockLink);
        when(paymentOrderRepository.findById(10L)).thenReturn(Optional.of(paymentOrder));

        PaymentLink result = paymentService.createRazorpayPaymentLink(user, 1500L, 10L);

        assertNotNull(result);
        assertEquals("plink_98765", result.get("id"));
        assertEquals("plink_98765", paymentOrder.getPaymentLinkId());
        verify(paymentOrderRepository, times(1)).save(paymentOrder);
    }

    @Test
    @DisplayName("Should throw PaymentValidationException when initiating link creation with negative amount")
    void createRazorpayPaymentLink_InvalidAmount_ThrowsValidationException() {
        User user = new User();
        assertThrows(PaymentValidationException.class, () ->
                paymentService.createRazorpayPaymentLink(user, -50L, 10L)
        );
    }

    @Test
    @DisplayName("Should throw PaymentGatewayException when Razorpay link creation fails")
    void createRazorpayPaymentLink_RazorpayException_ThrowsGatewayException() throws RazorpayException {
        User user = new User();
        user.setFullName("John Doe");

        when(paymentLink.create(any(JSONObject.class))).thenThrow(new RazorpayException("Link creation failed"));

        assertThrows(PaymentGatewayException.class, () ->
                paymentService.createRazorpayPaymentLink(user, 1500L, 10L)
        );
    }

    @Test
    @DisplayName("Should successfully update seller earnings and order metrics across separate stores")
    void updateSellerReports_Success() {
        PaymentOrder paymentOrder = new PaymentOrder();

        Order order = new Order();
        order.setSellerId(5L);
        order.setTotalSellingPrice(1200);
        paymentOrder.setOrders(Set.of(order));

        Seller seller = new Seller();
        seller.setId(5L);
        SellerReport report = new SellerReport();
        report.setTotalOrders(10L);
        report.setTotalEarnings(5000L);

        when(sellerService.getSellerById(5L)).thenReturn(seller);
        when(sellerReportService.getSellerReport(seller)).thenReturn(report);

        paymentService.updateSellerReports(paymentOrder);

        assertEquals(11L, report.getTotalOrders());
        assertEquals(6200L, report.getTotalEarnings());
        verify(sellerReportService, times(1)).updateSellerReport(report);
    }
}