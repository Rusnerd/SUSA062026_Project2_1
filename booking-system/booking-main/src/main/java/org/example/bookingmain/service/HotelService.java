package org.example.bookingmain.service;
 
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.example.bookingmain.domain.Hotel;
import org.example.bookingmain.repository.HotelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
 
import java.util.List;
 
@Service
public class HotelService {
  private static final Logger log = LoggerFactory.getLogger(HotelService.class);
  private final HotelRepository hotelRepository;
 
  public HotelService(HotelRepository hotelRepository) {
    this.hotelRepository = hotelRepository;
  }
 
  public Hotel createHotel(@Valid HotelCreateRequest req) {
    log.info("Creating hotel name={}, city={}, stars={}", req.name, req.city, req.stars);
    var hotel = new Hotel(req.name, req.city, req.stars);
    return hotelRepository.save(hotel);
  }
 
  public List<Hotel> findAll() {
    return hotelRepository.findAll();
  }
 
  public record HotelCreateRequest(
    @NotBlank String name,
    @NotBlank String city,
    @Min(1) @Max(5) int stars
  ) {}
}
