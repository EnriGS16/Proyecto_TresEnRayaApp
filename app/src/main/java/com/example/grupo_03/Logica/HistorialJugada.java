package com.example.grupo_03.Logica;

import com.example.grupo_03.Modelo.Tablero;

import java.io.Serializable;

/**
 * Representa el registro inmutable de un turno realizado durante la partida.
 * Guarda el autor de la jugada, la posición seleccionada y una captura estática
 * del estado del tablero en ese momento.
 */

public class HistorialJugada implements Serializable {
    private static final long serialVersionUID = 1L;

    private final char jugador;
    private final int fila;
    private final int columna;
    private final Tablero tableroResultante;

    public HistorialJugada(char jugador, int fila, int columna, Tablero tableroResultante) {
        this.jugador = jugador;
        this.fila = fila;
        this.columna = columna;
        this.tableroResultante = tableroResultante.copy();
    }

    public char getJugador() {
        return jugador;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    public Tablero getTableroResultante() {
        return tableroResultante;
    }
}