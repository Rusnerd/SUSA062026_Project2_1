package org.example.bookingmain.service;
 
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.example.bookingmain.domain.Room;
import org.example.bookingmain.repository.RoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
 
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
 
@Service
public class RoomService {
  private static final Logger log = LoggerFactory.getLogger(RoomService.class);
  private final RoomRepository roomRepository;
 
  public RoomService(RoomRepository roomRepository) {
    this.roomRepository = roomRepository;
  }
 
  public Room createRoom(@Valid RoomCreateRequest req) {
    log.info("Creating room for hotelId={}, roomNumber={}, capacity={}, price={}", req.hotelId, req.roomNumber, req.capacity, req.price);
    return roomRepository.save(new Room(req.hotelId, req.roomNumber, req.capacity, req.price, req.currency));
  }
 
  public List<Room> findAll() {
    return roomRepository.findAll();
  }
 
  public List<Room> findByHotelId(UUID hotelId) {
    return roomRepository.findByHotelId(hotelId);
  }
 
  public Room getOrThrow(UUID id) {
    return roomRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Room not found: " + id));
  }
 
  public record RoomCreateRequest(
    @NotNull UUID hotelId,
    @NotBlank String roomNumber,
    @Min(1) int capacity,
    @DecimalMin("0.00") BigDecimal price,
    @NotBlank String currency
  ) {}
}
