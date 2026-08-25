package org.example.bookingmain.controller;

import org.example.bookingmain.security.Role;
import org.example.bookingmain.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
public class UserAdminMvcController {

    private final UserService userService;

    public UserAdminMvcController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin/users")
    public String users(Model model) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("roles", Role.values());
        return "admin-users";
    }

    @PostMapping("/admin/users/role")
    public String updateRole(
            @RequestParam UUID userId,
            @RequestParam Role role
    ) {
        userService.updateRole(userId, role);
        return "redirect:/admin/users?updated";
    }
}
