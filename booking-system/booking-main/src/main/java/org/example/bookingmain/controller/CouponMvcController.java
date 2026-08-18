package org.example.bookingmain.controller;

import jakarta.validation.Valid;
import org.example.bookingmain.domain.Coupon;
import org.example.bookingmain.service.CouponService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
public class CouponMvcController {
  private final CouponService couponService;

  public CouponMvcController(CouponService couponService) {
    this.couponService = couponService;
  }

  @GetMapping("/coupons")
  public String list(Model model) {
    List<Coupon> coupons = couponService.findAll();
    model.addAttribute("coupons", coupons);
    return "coupons";
  }

  @PostMapping("/coupons")
  public String upsert(@Valid @ModelAttribute CouponService.CouponUpsertRequest req) {
    couponService.upsert(req);
    return "redirect:/coupons";
  }

  @PostMapping("/coupons/{id}/toggle")
  public String toggle(@PathVariable UUID id) {
    couponService.toggle(id);
    return "redirect:/coupons";
  }
}
