package org.example.bookingmain.controller;
 
import jakarta.validation.Valid;
import org.example.bookingmain.domain.Hotel;
import org.example.bookingmain.service.HotelService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
 
import java.util.List;
 
@Controller
public class HotelMvcController {
  private final HotelService hotelService;
 
  public HotelMvcController(HotelService hotelService) {
    this.hotelService = hotelService;
  }
 
  @GetMapping("/hotels")
  public String list(Model model) {
    List<Hotel> hotels = hotelService.findAll();
    model.addAttribute("hotels", hotels);
    return "hotels";
  }
 
  @PostMapping("/hotels")
  public String create(@ModelAttribute("request") @Valid HotelService.HotelCreateRequest req, Model model) {
    hotelService.createHotel(req);
    return "redirect:/hotels";
  }
}
