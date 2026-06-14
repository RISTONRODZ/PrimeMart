package org.riston.ecommerce.exception;

public class PaymentValidationException extends RuntimeException {
    public PaymentValidationException(String message) {
        super(message);
    }
}
