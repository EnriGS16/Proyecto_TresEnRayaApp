package com.example.grupo_03.Logica;

import com.example.grupo_03.Modelo.Tablero;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Almacena el análisis y la proyección de una jugada evaluada por la IA.
 * Incluye la utilidad mínima garantizada de la jugada y un registro de las
 * posibles respuestas que el oponente podría realizar.
 */

public class AnalisisJugada implements Serializable {
    private static final long serialVersionUID = 1L;

    private Tablero tablero;
    private int utilidadMinima;
    private boolean elegida;

    private ArrayList<AnalisisRespuesta> respuestas;

    public AnalisisJugada(Tablero tablero, int utilidadMinima, boolean elegida) {
        this.tablero = tablero;
        this.utilidadMinima = utilidadMinima;
        this.elegida = elegida;
        this.respuestas = new ArrayList<>();
    }

    public Tablero getTablero() {
        return tablero;
    }

    public int getUtilidadMinima() {
        return utilidadMinima;
    }

    public boolean isElegida() {
        return elegida;
    }

    public void setElegida(boolean elegida) {
        this.elegida = elegida;
    }

    public void addRespuesta(AnalisisRespuesta respuesta){
        respuestas.add(respuesta);
    }

    public ArrayList<AnalisisRespuesta> getRespuestas(){
        return respuestas;
    }
}