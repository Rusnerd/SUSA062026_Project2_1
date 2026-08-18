package org.example.bookingmain.controller;
 
import jakarta.validation.Valid;
import org.example.bookingmain.web.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
 
import java.util.List;
import java.util.UUID;
 
@Controller
public class ExperienceMvcController {
  private final ExperiencesClient experiencesClient;
 
  public ExperienceMvcController(ExperiencesClient experiencesClient) {
    this.experiencesClient = experiencesClient;
  }
 
  @GetMapping("/experiences")
  public String list(Model model) {
    List<ExperienceReservationDto> reservations = experiencesClient.listAll();
    model.addAttribute("reservations", reservations);
    return "experiences";
  }
 
  @PostMapping("/experiences")
  public String create(@Valid @ModelAttribute CreateExperienceReservationRequest req, Model model) {
    experiencesClient.create(req);
    return "redirect:/experiences";
  }
 
  @PostMapping("/experiences/{id}/confirm")
  public String confirm(@PathVariable UUID id) {
    experiencesClient.confirm(id);
    return "redirect:/experiences";
  }
 
  @PostMapping("/experiences/{id}/cancel")
  public String cancel(@PathVariable UUID id) {
    experiencesClient.cancel(id);
    return "redirect:/experiences";
  }
}
