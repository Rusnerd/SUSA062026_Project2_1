package org.example.bookingmain.domain;
 
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.UUID;
 
@Entity
@Table(name = "coupons")
public class Coupon {
  @Id
  @GeneratedValue
  private UUID id;
 
  @NotBlank
  @Column(nullable = false, unique = true)
  private String code;
 
  @Min(1)
  @Max(90)
  @Column(nullable = false)
  private int discountPercent;
 
  @Column(nullable = false)
  private boolean active;
 
  protected Coupon() {}
 
  public Coupon(String code, int discountPercent, boolean active) {
    this.code = code;
    this.discountPercent = discountPercent;
    this.active = active;
  }
 
  public UUID getId() { return id; }
  public String getCode() { return code; }
  public int getDiscountPercent() { return discountPercent; }
  public boolean isActive() { return active; }
 
  public void setActive(boolean active) { this.active = active; }
  public void updateDiscount(int discountPercent) { this.discountPercent = discountPercent; }
}
