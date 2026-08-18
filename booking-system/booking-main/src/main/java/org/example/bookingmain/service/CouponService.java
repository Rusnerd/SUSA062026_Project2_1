package org.example.bookingmain.service;
 
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.example.bookingmain.domain.Coupon;
import org.example.bookingmain.repository.CouponRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
 
import java.util.List;
 
@Service
public class CouponService {
  private static final Logger log = LoggerFactory.getLogger(CouponService.class);
  private final CouponRepository couponRepository;
 
  public CouponService(CouponRepository couponRepository) {
    this.couponRepository = couponRepository;
  }
 
  public Coupon upsert(@Valid CouponUpsertRequest req) {
    var existingOpt = couponRepository.findByCode(req.code);
    if (existingOpt.isPresent()) {
      var existing = existingOpt.get();
      existing.updateDiscount(req.discountPercent());
      existing.setActive(req.active());
      log.info("Updated coupon code={}", req.code());
      return couponRepository.save(existing);
    }
    log.info("Created coupon code={}", req.code());
    return couponRepository.save(new Coupon(req.code(), req.discountPercent(), req.active()));
  }
 
  public void toggle(java.util.UUID id) {
    var c = couponRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Coupon not found: " + id));
    c.setActive(!c.isActive());
    couponRepository.save(c);
    log.info("Toggled coupon id={}, active={}", id, c.isActive());
  }
 
  public List<Coupon> findAll() { return couponRepository.findAll(); }
 
  public java.util.Optional<Coupon> findActiveByCode(String code) {
    return couponRepository.findByCode(code).filter(Coupon::isActive);
  }
 
  public record CouponUpsertRequest(
    @NotBlank String code,
    @Min(1) @Max(90) int discountPercent,
    boolean active
  ) {
    public String code() { return code; }
    public int discountPercent() { return discountPercent; }
    public boolean active() { return active; }
  }
}
