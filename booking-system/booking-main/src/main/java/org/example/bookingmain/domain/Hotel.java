package org.example.bookingmain.domain;
 
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.UUID;
 
@Entity
@Table(name = "hotels")
public class Hotel {
  @Id
  @GeneratedValue
  private UUID id;
 
  @NotBlank
  @Column(nullable = false)
  private String name;
 
  @NotBlank
  @Column(nullable = false)
  private String city;
 
  @Min(1)
  @Max(5)
  @Column(nullable = false)
  private int stars;
 
  protected Hotel() {}
 
  public Hotel(String name, String city, int stars) {
    this.name = name;
    this.city = city;
    this.stars = stars;
  }
 
  public UUID getId() { return id; }
  public String getName() { return name; }
  public String getCity() { return city; }
  public int getStars() { return stars; }
}
