package org.example.bookingmain.domain;
 
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.UUID;
 
@Entity
@Table(name = "rooms")
public class Room {
  @Id
  @GeneratedValue
  private UUID id;
 
  @NotNull
  @Column(nullable = false)
  private UUID hotelId;
 
  @NotBlank
  @Column(nullable = false)
  private String roomNumber;
 
  @Min(1)
  @Column(nullable = false)
  private int capacity;
 
  @NotNull
  @DecimalMin("0.00")
  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal price;
 
  @NotBlank
  @Column(nullable = false)
  private String currency = "USD";
 
  protected Room() {}
 
  public Room(UUID hotelId, String roomNumber, int capacity, BigDecimal price, String currency) {
    this.hotelId = hotelId;
    this.roomNumber = roomNumber;
    this.capacity = capacity;
    this.price = price;
    this.currency = currency;
  }
 
  public UUID getId() { return id; }
  public UUID getHotelId() { return hotelId; }
  public String getRoomNumber() { return roomNumber; }
  public int getCapacity() { return capacity; }
  public BigDecimal getPrice() { return price; }
  public String getCurrency() { return currency; }
}
