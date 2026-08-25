package org.example.bookingexperiences.payment;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.UUID;

@Service
public class PaymentIntentService {

    private final PaymentIntentRepository paymentIntentRepository;

    public PaymentIntentService(PaymentIntentRepository paymentIntentRepository) {
        this.paymentIntentRepository = paymentIntentRepository;
    }

    @Transactional
    public PaymentIntent createPaymentIntent(UUID id, UUID bookingId, java.math.BigDecimal amount) {
        PaymentIntent intent = new PaymentIntent(id, bookingId, amount, PaymentStatus.CREATED);
        return paymentIntentRepository.save(intent);
    }

    @Transactional
    public PaymentIntent markPaid(UUID id) {
        PaymentIntent intent = paymentIntentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("PaymentIntent not found: " + id));
        intent.markPaid();
        return paymentIntentRepository.save(intent);
    }

    @Transactional
    public PaymentIntent markFailed(UUID id) {
        PaymentIntent intent = paymentIntentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("PaymentIntent not found: " + id));
        intent.markFailed();
        return paymentIntentRepository.save(intent);
    }

    @Transactional
    public PaymentIntent markCanceled(UUID id) {
        PaymentIntent intent = paymentIntentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("PaymentIntent not found: " + id));
        intent.markCanceled();
        return paymentIntentRepository.save(intent);
    }
    @Transactional(readOnly = true)
    public PaymentIntent getById(UUID id) {
        return paymentIntentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("PaymentIntent not found: " + id));
    }

}
