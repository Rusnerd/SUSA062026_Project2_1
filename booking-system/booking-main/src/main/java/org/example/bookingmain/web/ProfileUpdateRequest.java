package org.example.bookingmain.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProfileUpdateRequest(
        @NotBlank
        @Size(max = 60)
        String firstName,

        @NotBlank
        @Size(max = 60)
        String lastName,

        @Size(min = 8, max = 100)
        String newPassword
) {
}
