package org.riston.ecommerce.exception;

public class PaymentOrderNotFoundException extends RuntimeException {
    public PaymentOrderNotFoundException(String message) {
        super(message);
    }
}
