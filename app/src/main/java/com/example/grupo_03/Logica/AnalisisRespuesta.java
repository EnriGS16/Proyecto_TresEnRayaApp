package com.example.grupo_03.Logica;

import com.example.grupo_03.Modelo.Tablero;

import java.io.Serializable;

/**
 * Representa una respuesta simulada del oponente frente a un movimiento.
 * Guarda el estado del tablero resultante y el cálculo de su utilidad.
 */

public class AnalisisRespuesta implements Serializable {
    private static final long serialVersionUID = 1L;

    private Tablero tablero;
    private int utilidad;

    public AnalisisRespuesta(Tablero tablero, int utilidad) {
        this.tablero = tablero;
        this.utilidad = utilidad;
    }

    public Tablero getTablero() {
        return tablero;
    }

    public int getUtilidad() {
        return utilidad;
    }
}