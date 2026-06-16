package com.creativpressing.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignupRequest(@NotBlank String shopName, @NotBlank String ownerName, @NotBlank String phone,
        @NotBlank String city, @NotBlank String address, @Email @NotBlank String email, @NotBlank String password) {
}
