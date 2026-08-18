package org.example.bookingmain.controller;
 
import org.example.bookingmain.domain.Coupon;
import org.example.bookingmain.domain.Hotel;
import org.example.bookingmain.domain.Room;
import org.example.bookingmain.repository.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
 
import java.math.BigDecimal;
import java.util.UUID;
 
@Controller
public class AdminMvcController {
  private final HotelRepository hotelRepository;
  private final RoomRepository roomRepository;
  private final CouponRepository couponRepository;
 
  public AdminMvcController(HotelRepository hotelRepository, RoomRepository roomRepository, CouponRepository couponRepository) {
    this.hotelRepository = hotelRepository;
    this.roomRepository = roomRepository;
    this.couponRepository = couponRepository;
  }
 
  @GetMapping("/admin/seed")
  public String seedPage() { return "admin-seed"; }
 
  @PostMapping("/admin/seed")
  public String seed(Model model) {
    var h1 = hotelRepository.save(new Hotel("Grand Harbor", "Athens", 5));
    var h2 = hotelRepository.save(new Hotel("Sakura Inn", "Kyoto", 4));
    roomRepository.save(new Room(h1.getId(), "101", 2, new BigDecimal("120.00"), "USD"));
    roomRepository.save(new Room(h1.getId(), "201", 3, new BigDecimal("160.00"), "USD"));
    roomRepository.save(new Room(h2.getId(), "12A", 2, new BigDecimal("95.00"), "USD"));
    couponRepository.save(new Coupon("WELCOME10", 10, true));
    model.addAttribute("message", "Seeded demo data.");
    return "admin-seed";
  }
}
