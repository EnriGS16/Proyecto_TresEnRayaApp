package com.example.grupo_03.Logica;

/**
 * Enum que define los niveles de dificultad del oponente controlado por la computadora.
 * La dificultad determina estadísticamente la probabilidad de que la IA elija
 * el movimiento óptimo calculado por Minimax versus un movimiento aleatorio.
 */

public enum Dificultad {

    FACIL(0.15),
    MEDIO(0.5),
    DIFICIL(1.0);

    private final double probabilidadMinimax;

    Dificultad(double probabilidadMinimax) {
        this.probabilidadMinimax = probabilidadMinimax;
    }

    public double getProbabilidadMinimax() {
        return probabilidadMinimax;
    }
}
