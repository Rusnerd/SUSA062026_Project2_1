package org.example.bookingmain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "org.example.bookingmain.web")
public class BookingMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(BookingMainApplication.class, args);
    }
}
