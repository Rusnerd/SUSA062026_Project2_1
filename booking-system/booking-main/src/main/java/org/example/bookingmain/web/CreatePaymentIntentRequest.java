package org.example.bookingmain.web;

import java.math.BigDecimal;
import java.util.UUID;

public record CreatePaymentIntentRequest(UUID id, UUID bookingId, BigDecimal amount) {}
