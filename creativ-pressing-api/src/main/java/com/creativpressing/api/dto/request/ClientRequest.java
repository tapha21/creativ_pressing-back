package com.creativpressing.api.dto.request;

import jakarta.validation.constraints.*;
import java.util.UUID;

public record ClientRequest(@NotNull UUID shopId, @NotBlank String name, @NotBlank String phone, String address,
                String city) {
}
