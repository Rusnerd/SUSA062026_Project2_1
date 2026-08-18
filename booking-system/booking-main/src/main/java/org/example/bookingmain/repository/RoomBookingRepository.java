package org.example.bookingmain.repository;
import org.example.bookingmain.domain.RoomBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
public interface RoomBookingRepository extends JpaRepository<RoomBooking, UUID> {}
