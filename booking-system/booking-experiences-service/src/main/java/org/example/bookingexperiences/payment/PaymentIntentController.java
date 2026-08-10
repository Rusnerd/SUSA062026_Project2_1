package org.example.bookingexperiences.payment;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/payment-intents")
public class PaymentIntentController {

    private final PaymentIntentService paymentIntentService;

    public PaymentIntentController(PaymentIntentService paymentIntentService) {
        this.paymentIntentService = paymentIntentService;
    }

    @PostMapping
    public ResponseEntity<PaymentIntent> create(@RequestBody Map<String, String> body) {
        UUID id = UUID.fromString(body.get("id"));
        UUID bookingId = UUID.fromString(body.get("bookingId"));
        BigDecimal amount = new BigDecimal(body.get("amount"));
        PaymentIntent created = paymentIntentService.createPaymentIntent(id, bookingId, amount);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
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
