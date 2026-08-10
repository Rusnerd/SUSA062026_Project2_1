package org.example.bookingexperiences.payment;
import jakarta.persistence.Column;import jakarta.persistence.Entity;import jakarta.persistence.EnumType;import jakarta.persistence.Enumerated;import jakarta.persistence.Id;import jakarta.persistence.Table;
import java.math.BigDecimal;import java.time.Instant;import java.util.UUID;
@Entity@Table(name = "payment_intents")public class PaymentIntent {
    @Id    private UUID id;
    @Column(nullable = false)    private UUID bookingId;
    @Column(nullable = false)    private BigDecimal amount;
    @Enumerated(EnumType.STRING)    @Column(nullable = false)    private PaymentStatus status;
    @Column(nullable = false, updatable = false)    private Instant createdAt;
    @Column    private Instant updatedAt;
    protected PaymentIntent() {    }
    public PaymentIntent(UUID id, UUID bookingId, BigDecimal amount, PaymentStatus status) {        this.id = id;        this.bookingId = bookingId;        this.amount = amount;        this.status = status;        this.createdAt = Instant.now();        this.updatedAt = Instant.now();    }
    public UUID getId() {        return id;    }
    public UUID getBookingId() {        return bookingId;    }
    public BigDecimal getAmount() {        return amount;    }
    public PaymentStatus getStatus() {        return status;    }
    public void markPaid() {        this.status = PaymentStatus.PAID;        this.updatedAt = Instant.now();    }
    public void markFailed() {        this.status = PaymentStatus.FAILED;        this.updatedAt = Instant.now();    }
    public void markCanceled() {        this.status = PaymentStatus.CANCELED;        this.updatedAt = Instant.now();    }}
