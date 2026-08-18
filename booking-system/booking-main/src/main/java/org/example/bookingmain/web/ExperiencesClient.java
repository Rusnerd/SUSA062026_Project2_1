package org.example.bookingmain.web;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "bookingExperiencesClient", url = "${experiences.service.url}")
public interface ExperiencesClient {

    @GetMapping("/experiences")
    List<ExperienceReservationDto> listAll();

    @PostMapping("/experiences")
    ExperienceReservationDto create(@RequestBody CreateExperienceReservationRequest request);

    @PostMapping("/experiences/{id}/confirm")
    ExperienceReservationDto confirm(@PathVariable("id") UUID id);

    @PostMapping("/experiences/{id}/cancel")
    ExperienceReservationDto cancel(@PathVariable("id") UUID id);
}
