package org.example.bookingmain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BookingMainApplication {
    public static void main(String[] args) {
        System.out.println("Starting BookingMainApplication...");
        SpringApplication.run(BookingMainApplication.class, args);
    }
}
