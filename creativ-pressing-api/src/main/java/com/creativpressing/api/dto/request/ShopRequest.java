package com.creativpressing.api.dto.request;

import jakarta.validation.constraints.*;

public record ShopRequest(@NotBlank String name, @NotBlank String ownerName, @NotBlank String phone,
        @NotBlank String city, @NotBlank String address, @Email @NotBlank String email, String password) {
}
