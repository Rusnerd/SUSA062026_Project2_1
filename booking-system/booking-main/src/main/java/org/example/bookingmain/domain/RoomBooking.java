package org.example.bookingmain.domain;
 
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
 
@Entity
@Table(name = "room_bookings")
public class RoomBooking {
  public enum Status {
    CREATED,
    CONFIRMED,
    CANCELED
  }
 
  @Id
  @GeneratedValue
  private UUID id;
 
  @NotNull
  @Column(nullable = false)
  private UUID roomId;
 
  @NotBlank
  @Column(nullable = false)
  private String guestName;
 
  @NotNull
  @Column(nullable = false)
  private LocalDate startDate;
 
  @NotNull
  @Column(nullable = false)
  private LocalDate endDate;
 
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Status status = Status.CREATED;
 
  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal totalPrice;
 
  @Column
  private String appliedCouponCode;
 
  protected RoomBooking() {}
 
  public RoomBooking(UUID roomId, String guestName, LocalDate startDate, LocalDate endDate, BigDecimal totalPrice, String appliedCouponCode) {
    this.roomId = roomId;
    this.guestName = guestName;
    this.startDate = startDate;
    this.endDate = endDate;
    this.totalPrice = totalPrice;
    this.appliedCouponCode = appliedCouponCode;
  }
 
  public UUID getId() { return id; }
  public UUID getRoomId() { return roomId; }
  public String getGuestName() { return guestName; }
  public LocalDate getStartDate() { return startDate; }
  public LocalDate getEndDate() { return endDate; }
  public Status getStatus() { return status; }
  public BigDecimal getTotalPrice() { return totalPrice; }
  public String getAppliedCouponCode() { return appliedCouponCode; }
 
  public boolean cancelable() { return status != Status.CANCELED; }
  public void confirm() { if (status == Status.CREATED) status = Status.CONFIRMED; }
  public void cancel() { this.status = Status.CANCELED; }
}
