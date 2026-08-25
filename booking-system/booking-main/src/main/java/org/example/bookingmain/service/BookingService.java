package org.example.bookingmain.service;
 
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.example.bookingmain.domain.Room;
import org.example.bookingmain.domain.RoomBooking;
import org.example.bookingmain.repository.RoomBookingRepository;
import org.example.bookingmain.web.CreatePaymentIntentRequest;
import org.example.bookingmain.web.ExperiencesClient;
import org.example.bookingmain.repository.RoomRepository;
import org.example.bookingmain.service.RoomBookingViewMapper.BookingView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
 
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
 
@Service
public class BookingService {
  private static final Logger log = LoggerFactory.getLogger(BookingService.class);
  private final RoomBookingRepository bookingRepository;
  private final RoomRepository roomRepository;
  private final CouponService couponService;
  private final ExperiencesClient experiencesClient;
 
  public BookingService(RoomBookingRepository bookingRepository, RoomRepository roomRepository, CouponService couponService, ExperiencesClient experiencesClient) {
    this.bookingRepository = bookingRepository;
    this.roomRepository = roomRepository;
    this.couponService = couponService;
    this.experiencesClient = experiencesClient;
  }
 
  public RoomBooking createBooking(@Valid BookingCreateRequest req) {
    Room room = roomRepository.findById(req.roomId()).orElseThrow(() -> new IllegalArgumentException("Room not found: " + req.roomId()));
    if (!req.endDate().isAfter(req.startDate())) {
      throw new IllegalArgumentException("endDate must be after startDate");
    }
    long nights = ChronoUnit.DAYS.between(req.startDate(), req.endDate());
    if (nights <= 0) throw new IllegalArgumentException("Invalid booking dates");
    BigDecimal base = room.getPrice().multiply(BigDecimal.valueOf(nights));
    BigDecimal total = base;
    String appliedCouponCode = null;
 
    if (req.couponCode() != null && !req.couponCode().isBlank()) {
      var activeCouponOpt = couponService.findActiveByCode(req.couponCode().trim());
      if (activeCouponOpt.isPresent()) {
        var coupon = activeCouponOpt.get();
        BigDecimal discount = base.multiply(BigDecimal.valueOf(coupon.getDiscountPercent()).divide(BigDecimal.valueOf(100)));// percent
        total = base.subtract(discount);
        appliedCouponCode = coupon.getCode();
        log.info("Applied coupon code={} base={} total={}", appliedCouponCode, base, total);
      } else {
        log.info("Coupon not applied (missing or inactive). code={}", req.couponCode());
      }
    }
 
    log.info("Creating booking roomId={}, guestName={}", req.roomId(), req.guestName());
    var booking = new RoomBooking(req.roomId(), req.guestName(), req.startDate(), req.endDate(), total, appliedCouponCode);
    var savedBooking = bookingRepository.save(booking);
    experiencesClient.createPaymentIntent(new CreatePaymentIntentRequest(savedBooking.getId(), savedBooking.getId(), savedBooking.getTotalPrice()));
    experiencesClient.getPaymentIntent(savedBooking.getId());
    log.info("Created payment intent for booking id={}", savedBooking.getId());
    return savedBooking;
  }
 
  public void cancel(UUID bookingId) {
    var b = bookingRepository.findById(bookingId).orElseThrow(() -> new IllegalArgumentException("Booking not found: " + bookingId));
    if (!b.cancelable()) throw new IllegalArgumentException("Booking already canceled");
    b.cancel();
    bookingRepository.save(b);
    experiencesClient.cancelPayment(bookingId);
    log.info("Canceled payment for booking id={}", bookingId);
    log.info("Canceled booking id={", bookingId);
  }
 
  public List<BookingView> findAllViews() {
    var bookings = bookingRepository.findAll();
    List<BookingView> views = new ArrayList<>();
    for (var b : bookings) {
      var room = roomRepository.findById(b.getRoomId()).orElse(null);
      if (room == null) continue;
      views.add(RoomBookingViewMapper.map(b, room));
    }
    return views;
  }
 
  public record BookingCreateRequest(
    @NotNull UUID roomId,
    @NotBlank String guestName,
    @NotNull LocalDate startDate,
    @NotNull LocalDate endDate,
    String couponCode
  ) {}
}
