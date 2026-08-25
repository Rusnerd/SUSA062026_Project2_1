package org.example.bookingexperiences.payment;

public class InvalidPaymentStatusTransitionException extends RuntimeException {
    private final PaymentStatus currentStatus;
    private final PaymentStatus requestedStatus;

    public InvalidPaymentStatusTransitionException(PaymentStatus currentStatus, PaymentStatus requestedStatus) {
        super("Cannot change payment status from " + currentStatus + " to " + requestedStatus);
        this.currentStatus = currentStatus;
        this.requestedStatus = requestedStatus;
    }

    public PaymentStatus getCurrentStatus() {
        return currentStatus;
    }

    public PaymentStatus getRequestedStatus() {
        return requestedStatus;
    }
}
