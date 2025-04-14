package com.example.projetmindfit.Dtos;

public enum Etat {
    ATHLETIQUE(0),
    SEDENTAIRE(1),
    ACTIF(2);

    private final int value;

    Etat(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Etat fromValue(int value) {
        for (Etat etat : values()) {
            if (etat.value == value) {
                return etat;
            }
        }
        throw new IllegalArgumentException("Unknown Etat value: " + value);
    }
}
