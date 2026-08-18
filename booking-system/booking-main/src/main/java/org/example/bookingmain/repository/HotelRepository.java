package org.example.bookingmain.repository;
import org.example.bookingmain.domain.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
public interface HotelRepository extends JpaRepository<Hotel, UUID> {}
