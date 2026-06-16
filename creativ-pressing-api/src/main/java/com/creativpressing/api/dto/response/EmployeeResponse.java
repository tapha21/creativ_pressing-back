package com.creativpressing.api.dto.response;

import com.creativpressing.api.enums.EmployeeRole;
import java.time.LocalDate;
import java.util.UUID;

public record EmployeeResponse(UUID id, String name, EmployeeRole role, String phone, String email, LocalDate joinedAt,
        Boolean active) {
}
