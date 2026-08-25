package org.example.bookingexperiences.payment;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

public record CreatePaymentIntentRequest(
        @NotNull UUID id,
        @NotNull UUID bookingId,
        @NotNull @DecimalMin(value = "0.01") BigDecimal amount
) {
}
