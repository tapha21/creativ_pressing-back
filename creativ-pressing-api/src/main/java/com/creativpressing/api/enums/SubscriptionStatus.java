package com.creativpressing.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SubscriptionStatus {
    TRIAL("Essai"), ACTIVE("Actif"), EXPIRED("Expire"), SUSPENDED("Suspendu");

    private final String label;

    SubscriptionStatus(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }

    @JsonCreator
    public static SubscriptionStatus fromValue(String value) {
        for (SubscriptionStatus status : values()) {
            if (status.name().equalsIgnoreCase(value) || status.label.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Etat d'abonnement invalide : " + value);
    }
}
