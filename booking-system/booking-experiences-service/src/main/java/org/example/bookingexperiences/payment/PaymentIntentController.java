package org.example.bookingexperiences.payment;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/payment-intents")
public class PaymentIntentController {
    private final PaymentIntentService paymentIntentService;

    public PaymentIntentController(PaymentIntentService paymentIntentService) {
        this.paymentIntentService = paymentIntentService;
    }

    @PostMapping
    public ResponseEntity<PaymentIntent> create(@Valid @RequestBody CreatePaymentIntentRequest request) {
        PaymentIntent created = paymentIntentService.createPaymentIntent(request.id(), request.bookingId(), request.amount());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/{id}/paid")
    public PaymentIntent paid(@PathVariable UUID id) {
        return paymentIntentService.markPaid(id);
    }

    @PostMapping("/{id}/failed")
    public PaymentIntent failed(@PathVariable UUID id) {
        return paymentIntentService.markFailed(id);
    }

    @PostMapping("/{id}/canceled")
    public PaymentIntent canceled(@PathVariable UUID id) {
        return paymentIntentService.markCanceled(id);
    }
}
