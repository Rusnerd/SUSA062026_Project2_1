package org.example.bookingexperiences.payment;

public class PaymentIntentNotFoundException extends RuntimeException {
    public PaymentIntentNotFoundException(String message) {
        super(message);
    }
}
