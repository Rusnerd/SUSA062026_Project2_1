package org.example.bookingmain.web;
 
import jakarta.validation.constraints.*;
import java.time.LocalDate;
 
public record CreateExperienceReservationRequest(
  @NotBlank String name,
  @NotBlank String guestName,
  @NotNull LocalDate date
) {}
