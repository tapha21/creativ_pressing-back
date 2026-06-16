package com.creativpressing.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SubscriptionPlan {
    BASIC("Basic"), STANDARD("Standard"), PREMIUM("Premium");

    private final String label;

    SubscriptionPlan(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }

    @JsonCreator
    public static SubscriptionPlan fromValue(String value) {
        for (SubscriptionPlan plan : values()) {
            if (plan.name().equalsIgnoreCase(value) || plan.label.equalsIgnoreCase(value)) {
                return plan;
            }
        }
        throw new IllegalArgumentException("Offre invalide : " + value);
    }
}
