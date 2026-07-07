package com.creativpressing.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PhotoType {
    BEFORE("Avant"), AFTER("Après");

    private final String label;

    PhotoType(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }

    @JsonCreator
    public static PhotoType fromValue(String value) {
        for (PhotoType t : values()) {
            if (t.label.equalsIgnoreCase(value) || t.name().equalsIgnoreCase(value))
                return t;
        }
        throw new IllegalArgumentException("Type de photo invalide : " + value);
    }
}

