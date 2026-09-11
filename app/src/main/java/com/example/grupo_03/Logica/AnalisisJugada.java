package com.example.grupo_03.Logica;

import com.example.grupo_03.Modelo.Tablero;

import java.io.Serializable;
import java.util.ArrayList;
public class AnalisisJugada implements Serializable {
    private static final long serialVersionUID = 1L;

    private Tablero tablero;
    private int utilidadMinima;
    private boolean elegida;

    /*Guardaremos todas las posibles respuestas
    que podria realizar el humano
     */
    private ArrayList<AnalisisRespuesta> respuestas;

    public AnalisisJugada(Tablero tablero, int utilidadMinima, boolean elegida) {

        this.tablero = tablero;
        this.utilidadMinima = utilidadMinima;
        this.elegida = elegida;
        //inicializamos la lista para que no sea null
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
    // Agg una posible respuesta que encontro el minimax.
    public void addRespuesta(AnalisisRespuesta respuesta){
        respuestas.add(respuesta);
    }

    //Obtener todas las R. que se analizaron en esta jugada
    public ArrayList<AnalisisRespuesta> getRespuestas(){
        return respuestas;
    }
}
