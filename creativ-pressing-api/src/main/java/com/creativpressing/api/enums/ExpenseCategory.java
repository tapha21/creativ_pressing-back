package com.creativpressing.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ExpenseCategory {
    WATER("Eau"), 
    ELECTRICITY("Électricité"),
    SALARIES("Salaires"),
    PRODUCTS("Produits"),
     OTHER("Autre");

    private final String label;

    ExpenseCategory(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }

    @JsonCreator
    public static ExpenseCategory fromValue(String value) {
        for (ExpenseCategory c : values()) {
            if (c.label.equalsIgnoreCase(value) || c.name().equalsIgnoreCase(value))
                return c;
        }
        throw new IllegalArgumentException("Catégorie invalide : " + value);
    }
}

