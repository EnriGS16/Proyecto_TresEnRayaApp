package com.example.grupo_03.Logica;

/**
 * Representa el nivel de dificultad con el que juega la computadora.
 *
 * Cada nivel define la probabilidad de que, en su turno, la computadora
 * use el movimiento óptimo calculado por Minimax en lugar de un
 * movimiento aleatorio entre las casillas libres del tablero.
 *
 * FACIL   -> casi siempre juega al azar, rara vez usa Minimax.
 * MEDIO   -> mitad de las veces juega óptimo, mitad al azar.
 * DIFICIL -> siempre usa el movimiento óptimo de Minimax (comportamiento actual)
 */

public enum Dificultad {

    FACIL(0.15),
    MEDIO(0.5),
    DIFICIL(1.0);

    // Probabilidad (entre 0.0 y 1.0) de que la computadora
    // use el movimiento óptimo de Minimax en su turno
    private final double probabilidadMinimax;

    Dificultad(double probabilidadMinimax) {
        this.probabilidadMinimax = probabilidadMinimax;
    }

    public double getProbabilidadMinimax() {
        return probabilidadMinimax;
    }
}
