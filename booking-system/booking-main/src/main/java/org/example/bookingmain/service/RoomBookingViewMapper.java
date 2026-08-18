package org.example.bookingmain.service;
 
import org.example.bookingmain.domain.Room;
import org.example.bookingmain.domain.RoomBooking;
import java.util.UUID;
 
public class RoomBookingViewMapper {
  public record BookingView(
    UUID id,
    String guestName,
    String roomNumber,
    java.time.LocalDate startDate,
    java.time.LocalDate endDate,
    RoomBooking.Status status,
    java.math.BigDecimal totalPrice,
    boolean cancelable
  ) {}
 
  public static BookingView map(RoomBooking b, Room room) {
    return new BookingView(
      b.getId(),
      b.getGuestName(),
      room.getRoomNumber(),
      b.getStartDate(),
      b.getEndDate(),
      b.getStatus(),
      b.getTotalPrice(),
      b.cancelable()
    );
  }
}
