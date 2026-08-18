package org.example.bookingmain.controller;
 
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.example.bookingmain.service.AppDomainException;
import org.springframework.http.HttpStatus;
 
@ControllerAdvice
public class GlobalWebExceptionHandler {
  @ExceptionHandler({IllegalArgumentException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String handleIllegalArgument(IllegalArgumentException ex, Model model) {
    model.addAttribute("error", ex.getMessage());
    return "error";
  }
 
  @ExceptionHandler(AppDomainException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String handleDomain(AppDomainException ex, Model model) {
    model.addAttribute("error", ex.getMessage());
    return "error";
  }
}
