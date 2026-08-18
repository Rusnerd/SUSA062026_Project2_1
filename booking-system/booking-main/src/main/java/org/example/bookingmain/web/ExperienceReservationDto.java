package org.example.bookingmain.web;
 
import java.time.LocalDate;
import java.util.UUID;
 
public record ExperienceReservationDto(
  UUID id,
  String name,
  String guestName,
  LocalDate date,
  String status,
  boolean confirmable,
  boolean cancelable
) {}
