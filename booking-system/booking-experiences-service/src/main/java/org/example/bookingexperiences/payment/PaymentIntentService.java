package org.example.bookingexperiences.payment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class PaymentIntentService {
    private static final Logger logger = LoggerFactory.getLogger(PaymentIntentService.class);

    private final PaymentIntentRepository paymentIntentRepository;

    public PaymentIntentService(PaymentIntentRepository paymentIntentRepository) {
        this.paymentIntentRepository = paymentIntentRepository;
    }

    @Transactional
    public PaymentIntent createPaymentIntent(UUID id, UUID bookingId, BigDecimal amount) {
        PaymentIntent intent = new PaymentIntent(id, bookingId, amount, PaymentStatus.CREATED);
        PaymentIntent savedIntent = paymentIntentRepository.save(intent);
        logger.info("Created payment intent {} for booking {}", id, bookingId);
        return savedIntent;
    }

    @Transactional
    public PaymentIntent markPaid(UUID id) {
        return changeStatus(id, PaymentStatus.PAID);
    }

    @Transactional
    public PaymentIntent markFailed(UUID id) {
        return changeStatus(id, PaymentStatus.FAILED);
    }

    @Transactional
    public PaymentIntent markCanceled(UUID id) {
        return changeStatus(id, PaymentStatus.CANCELED);
    }

    @Transactional(readOnly = true)
    public PaymentIntent getById(UUID id) {
        PaymentIntent intent = findById(id);
        logger.info("Retrieved payment intent {}", id);
        return intent;
    }

    private PaymentIntent changeStatus(UUID id, PaymentStatus requestedStatus) {
        PaymentIntent intent = findById(id);
        validateTransition(intent.getStatus(), requestedStatus);
        PaymentStatus previousStatus = intent.getStatus();

        if (requestedStatus == PaymentStatus.PAID) {
            intent.markPaid();
        } else if (requestedStatus == PaymentStatus.FAILED) {
            intent.markFailed();
        } else {
            intent.markCanceled();
        }

        PaymentIntent savedIntent = paymentIntentRepository.save(intent);
        logger.info("Changed payment intent {} status from {} to {}", id, previousStatus, requestedStatus);
        return savedIntent;
    }

    private PaymentIntent findById(UUID id) {
        return paymentIntentRepository.findById(id)
                .orElseThrow(() -> new PaymentIntentNotFoundException("Payment intent not found: " + id));
    }

    private void validateTransition(PaymentStatus currentStatus, PaymentStatus requestedStatus) {
        if (currentStatus != PaymentStatus.CREATED) {
            throw new InvalidPaymentStatusTransitionException(currentStatus, requestedStatus);
        }
    }
}
