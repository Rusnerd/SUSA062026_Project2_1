package org.example.bookingmain.controller;

import jakarta.validation.Valid;
import org.example.bookingmain.domain.User;
import org.example.bookingmain.service.UserService;
import org.example.bookingmain.web.ProfileUpdateRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ProfileMvcController {

    private final UserService userService;

    public ProfileMvcController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String profile(Authentication authentication, Model model) {
        User user = userService.findByEmail(authentication.getName());
        model.addAttribute("user", user);
        model.addAttribute("profileRequest", new ProfileUpdateRequest(
                user.getFirstName(),
                user.getLastName(),
                ""
        ));
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(
            Authentication authentication,
            @Valid @ModelAttribute("profileRequest") ProfileUpdateRequest request,
            BindingResult bindingResult,
            Model model
    ) {
        User user = userService.findByEmail(authentication.getName());

        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            return "profile";
        }

        userService.updateProfile(authentication.getName(), request);
        return "redirect:/profile?updated";
    }
}
