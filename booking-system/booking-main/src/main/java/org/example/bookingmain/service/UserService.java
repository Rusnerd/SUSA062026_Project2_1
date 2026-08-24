package org.example.bookingmain.service;

import jakarta.validation.Valid;
import org.example.bookingmain.domain.User;
import org.example.bookingmain.repository.UserRepository;
import org.example.bookingmain.security.Role;
import org.example.bookingmain.web.ProfileUpdateRequest;
import org.example.bookingmain.web.RegisterRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;

@Service
@Validated
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(@Valid RegisterRequest request) {
        String email = request.email().trim().toLowerCase();

        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new AppDomainException("An account with this email already exists.");
        }

        User user = new User(
                email,
                passwordEncoder.encode(request.password()),
                request.firstName().trim(),
                request.lastName().trim(),
                Role.USER
        );

        User savedUser = userRepository.save(user);
        log.info("Registered user with id {}", savedUser.getId());
        return savedUser;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new AppDomainException("User account was not found."));
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User updateProfile(
            String email,
            @Valid ProfileUpdateRequest request
    ) {
        User user = findByEmail(email);

        user.setFirstName(request.firstName().trim());
        user.setLastName(request.lastName().trim());

        if (request.newPassword() != null && !request.newPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.newPassword()));
        }

        User savedUser = userRepository.save(user);
        log.info("Updated profile for user {}", savedUser.getId());
        return savedUser;
    }

    public User updateRole(UUID userId, Role role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppDomainException("The selected user was not found."));

        user.setRole(role);
        User savedUser = userRepository.save(user);
        log.info("Changed role for user {} to {}", userId, role);
        return savedUser;
    }
}
