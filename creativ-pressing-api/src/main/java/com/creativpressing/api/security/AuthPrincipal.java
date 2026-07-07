package com.creativpressing.api.security;

import com.creativpressing.api.enums.EmployeeRole;

import java.util.UUID;

public record AuthPrincipal(UUID employeeId, UUID shopId, EmployeeRole role, String email, String name) {
}
