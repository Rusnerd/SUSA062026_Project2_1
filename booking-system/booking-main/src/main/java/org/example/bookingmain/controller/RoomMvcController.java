package org.example.bookingmain.controller;
 
import jakarta.validation.Valid;
import org.example.bookingmain.domain.Room;
import org.example.bookingmain.service.RoomService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
 
import java.util.List;
import java.util.UUID;
 
@Controller
public class RoomMvcController {
  private final RoomService roomService;
 
  public RoomMvcController(RoomService roomService) {
    this.roomService = roomService;
  }
 
  @GetMapping("/rooms")
  public String list(@RequestParam(required = false) UUID hotelId, Model model) {
    List<Room> rooms = (hotelId == null) ? roomService.findAll() : roomService.findByHotelId(hotelId);
    model.addAttribute("rooms", rooms);
    model.addAttribute("hotelId", hotelId);
    return "rooms";
  }
 
  @PostMapping("/rooms")
  public String create(@Valid @ModelAttribute RoomService.RoomCreateRequest req) {
    roomService.createRoom(req);
    return "redirect:/rooms";
  }
}
