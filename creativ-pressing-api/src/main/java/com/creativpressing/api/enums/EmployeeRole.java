package com.creativpressing.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EmployeeRole {
    OWNER("Propriétaire"), EMPLOYEE("Employé");

    private final String label;

    EmployeeRole(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }

    @JsonCreator
    public static EmployeeRole fromValue(String value) {
        for (EmployeeRole r : values()) {
            if (r.label.equalsIgnoreCase(value) || r.name().equalsIgnoreCase(value))
                return r;
        }
        throw new IllegalArgumentException("Rôle invalide : " + value);
    }
}
