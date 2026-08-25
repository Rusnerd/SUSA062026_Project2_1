package org.example.bookingexperiences.payment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentIntentServiceTest {
    @Mock
    private PaymentIntentRepository paymentIntentRepository;

    private PaymentIntentService paymentIntentService;

    @BeforeEach
    void setUp() {
        paymentIntentService = new PaymentIntentService(paymentIntentRepository);
    }

    @Test
    void createsPaymentIntentWithCreatedStatus() {
        UUID id = UUID.randomUUID();
        UUID bookingId = UUID.randomUUID();
        BigDecimal amount = new BigDecimal("49.99");
        PaymentIntent intent = new PaymentIntent(id, bookingId, amount, PaymentStatus.CREATED);

        when(paymentIntentRepository.save(org.mockito.ArgumentMatchers.any(PaymentIntent.class))).thenReturn(intent);

        PaymentIntent result = paymentIntentService.createPaymentIntent(id, bookingId, amount);

        assertEquals(id, result.getId());
        assertEquals(PaymentStatus.CREATED, result.getStatus());
        verify(paymentIntentRepository).save(org.mockito.ArgumentMatchers.any(PaymentIntent.class));
    }

    @Test
    void marksCreatedIntentAsPaid() {
        UUID id = UUID.randomUUID();
        PaymentIntent intent = new PaymentIntent(id, UUID.randomUUID(), new BigDecimal("20.00"), PaymentStatus.CREATED);

        when(paymentIntentRepository.findById(id)).thenReturn(java.util.Optional.of(intent));
        when(paymentIntentRepository.save(intent)).thenReturn(intent);

        PaymentIntent result = paymentIntentService.markPaid(id);

        assertEquals(PaymentStatus.PAID, result.getStatus());
    }

    @Test
    void rejectsTransitionFromPaidStatus() {
        UUID id = UUID.randomUUID();
        PaymentIntent intent = new PaymentIntent(id, UUID.randomUUID(), new BigDecimal("20.00"), PaymentStatus.PAID);

        when(paymentIntentRepository.findById(id)).thenReturn(java.util.Optional.of(intent));

        assertThrows(InvalidPaymentStatusTransitionException.class, () -> paymentIntentService.markFailed(id));
    }

    @Test
    void throwsNotFoundExceptionForMissingIntent() {
        UUID id = UUID.randomUUID();
        when(paymentIntentRepository.findById(id)).thenReturn(java.util.Optional.empty());

        assertThrows(PaymentIntentNotFoundException.class, () -> paymentIntentService.getById(id));
    }
}
