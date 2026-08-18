package org.example.bookingmain.controller;
 
import jakarta.validation.Valid;
import org.example.bookingmain.service.BookingService;
import org.example.bookingmain.service.RoomService;
import org.example.bookingmain.service.RoomBookingViewMapper.BookingView;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
 
import java.util.List;
import java.util.UUID;
 
@Controller
public class BookingMvcController {
  private final BookingService bookingService;
  private final RoomService roomService;
 
  public BookingMvcController(BookingService bookingService, RoomService roomService) {
    this.bookingService = bookingService;
    this.roomService = roomService;
  }
 
  @GetMapping("/bookings")
  public String list(Model model) {
    List<BookingView> bookings = bookingService.findAllViews();
    model.addAttribute("bookings", bookings);
    return "bookings";
  }
 
  @GetMapping("/bookings/new")
  public String newBooking(@RequestParam UUID roomId, Model model) {
    roomService.getOrThrow(roomId);
    model.addAttribute("roomId", roomId);
    return "booking-new";
  }
 
  @PostMapping("/bookings")
  public String create(@Valid @ModelAttribute BookingService.BookingCreateRequest req) {
    bookingService.createBooking(req);
    return "redirect:/bookings";
  }
 
  @PostMapping("/bookings/{id}/cancel")
  public String cancel(@PathVariable UUID id) {
    bookingService.cancel(id);
    return "redirect:/bookings";
  }
}
