package com.creativpressing.api.dto.request;

import com.creativpressing.api.enums.EmployeeRole;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.UUID;

public record EmployeeRequest(@NotNull UUID shopId, @NotBlank String name, @NotNull EmployeeRole role,
                @NotBlank String phone, @Email String email, String password, @NotNull LocalDate joinedAt,
                Boolean active) {
}
