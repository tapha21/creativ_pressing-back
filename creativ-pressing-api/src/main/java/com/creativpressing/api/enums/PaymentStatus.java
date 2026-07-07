package com.creativpressing.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PaymentStatus {
    PAID("Payé"), 
    PARTIALLY_PAID("Partiellement payé"),
    UNPAID("Non payé");

    private final String label;

    PaymentStatus(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }

    @JsonCreator
    public static PaymentStatus fromValue(String value) {
        for (PaymentStatus s : values()) {
            if (s.label.equalsIgnoreCase(value) || s.name().equalsIgnoreCase(value))
                return s;
        }
        throw new IllegalArgumentException("Statut de paiement invalide : " + value);
    }
}

