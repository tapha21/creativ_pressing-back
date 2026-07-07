package com.creativpressing.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum OrderStatus {
    RECEIVED("Reçu"),
    WASHING("En lavage"),
    IRONING("En repassage"),
    READY("Prêt"),
    DELIVERED("Livré");

    private final String label;

    OrderStatus(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }

    @JsonCreator
    public static OrderStatus fromValue(String value) {
        for (OrderStatus s : values()) {
            if (s.label.equalsIgnoreCase(value) || s.name().equalsIgnoreCase(value))
                return s;
        }
        throw new IllegalArgumentException("Statut de commande invalide : " + value);
    }
}

