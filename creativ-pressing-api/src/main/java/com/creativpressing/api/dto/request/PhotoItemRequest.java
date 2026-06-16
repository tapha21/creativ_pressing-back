package com.creativpressing.api.dto.request;

import com.creativpressing.api.enums.PhotoType;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.UUID;

public record PhotoItemRequest(@NotNull UUID orderId, @NotNull PhotoType type, @NotBlank String url,
        @NotNull LocalDate date) {
}
