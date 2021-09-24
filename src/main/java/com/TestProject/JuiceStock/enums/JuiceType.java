package com.TestProject.JuiceStock.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JuiceType {

    MORANGO("Morango"),
    ABACAXI("Abacaxi"),
    LARANJA("Laranja"),
    MAÇA("Maça"),
    LIMAO("Limao"),
    BETERRABA("Beterraba"),
    MELANCIA("Melancia");

    private final String description;
}
