package com.example.grupo_03.Logica;

import com.example.grupo_03.Estructuras.Tree;
import com.example.grupo_03.Modelo.Tablero;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * Implementación del algoritmo Minimax para la toma de decisiones de la IA.
 * Construye un árbol de estados posibles, evalúa las utilidades de cada rama
 * y determina el movimiento que maximice las probabilidades de victoria o
 * minimice las de derrota.
 */

public class MiniMax implements Serializable {
    private static final long serialVersionUID = 1L;

    private ArrayList<AnalisisJugada> analisisUltimaJugada = new ArrayList<>();

    public int[] obtenerMejorMovimiento(Tablero tableroActual, char simboloComputadora, char simboloOponente) {

        analisisUltimaJugada.clear();

        if (tableroActual.isFull() || tableroActual.isWinner(simboloOponente) || tableroActual.isWinner(simboloComputadora)) {
            return null;
        }

        Tree<Tablero> arbolEstados = new Tree<>(tableroActual);
        ArrayList<Tablero> estados = tableroActual.generateStates(simboloComputadora);

        for (Tablero estado : estados) {
            Tree<Tablero> hijo = new Tree<>(estado);
            arbolEstados.getRoot().addChild(hijo);
        }

        for (Tree<Tablero> hijo : arbolEstados.getRoot().getChildren()) {
            Tablero tableroHijo = hijo.getRoot().getContent();

            if (tableroHijo.isWinner(simboloComputadora)) {
                continue;
            }
            if (tableroHijo.isFull()) {
                continue;
            }

            ArrayList<Tablero> respuestasOponente = tableroHijo.generateStates(simboloOponente);

            for (Tablero respuesta : respuestasOponente) {
                Tree<Tablero> nieto = new Tree<>(respuesta);
                hijo.getRoot().addChild(nieto);
            }
        }

        int utilidadMaxima = Integer.MIN_VALUE;
        ArrayList<Tree<Tablero>> mejoresJugadas = new ArrayList<>();

        for (Tree<Tablero> hijo : arbolEstados.getRoot().getChildren()) {

            Tablero tableroHijo = hijo.getRoot().getContent();
            int utilidadMinima;
            ArrayList<AnalisisRespuesta> respuestasAnalizadas = new ArrayList<>();

            if (hijo.isLeaf()) {
                utilidadMinima = tableroHijo.calculateUtility(simboloComputadora);
            } else {

                utilidadMinima = Integer.MAX_VALUE;
                boolean hayDerrotaInmediata = false;

                for (Tree<Tablero> respuesta : hijo.getRoot().getChildren()) {

                    Tablero tableroRespuesta = respuesta.getRoot().getContent();
                    int utilidad = tableroRespuesta.calculateUtility(simboloComputadora);

                    AnalisisRespuesta analisisRespuesta = new AnalisisRespuesta(tableroRespuesta, utilidad);
                    respuestasAnalizadas.add(analisisRespuesta);

                    if (tableroRespuesta.isWinner(simboloOponente)) {
                        hayDerrotaInmediata = true;
                    } else {
                        if (utilidad < utilidadMinima) {
                            utilidadMinima = utilidad;
                        }
                    }
                }

                if (hayDerrotaInmediata) {
                    utilidadMinima = Integer.MIN_VALUE;
                }
            }

            AnalisisJugada analisis = new AnalisisJugada(tableroHijo, utilidadMinima, false);

            for (AnalisisRespuesta respuestaAnalizada : respuestasAnalizadas) {
                analisis.addRespuesta(respuestaAnalizada);
            }

            analisisUltimaJugada.add(analisis);

            if (utilidadMinima > utilidadMaxima) {
                utilidadMaxima = utilidadMinima;
                mejoresJugadas.clear();
                mejoresJugadas.add(hijo);
            } else if (utilidadMinima == utilidadMaxima) {
                mejoresJugadas.add(hijo);
            }
        }

        Random random = new Random();
        int posicionAleatoria = random.nextInt(mejoresJugadas.size());
        Tree<Tablero> jugadaElegida = mejoresJugadas.get(posicionAleatoria);
        Tablero estadoElegido = jugadaElegida.getRoot().getContent();

        return obtenerMovimiento(tableroActual, estadoElegido);
    }

    private int[] obtenerMovimiento(Tablero original, Tablero estado) {
        for (int fila = 0; fila < Tablero.tamanio; fila++) {
            for (int columna = 0; columna < Tablero.tamanio; columna++) {
                if (original.obtenerCasilla(fila, columna) != estado.obtenerCasilla(fila, columna)) {
                    return new int[]{fila, columna};
                }
            }
        }
        return null;
    }

    public ArrayList<AnalisisJugada> getAnalisisUltimaJugada() {
        return analisisUltimaJugada;
    }

    /**
     * Identifica y marca en el análisis la jugada que la IA ejecutó de manera definitiva,
     * útil para distinguir entre decisiones basadas en el cálculo óptimo y movimientos aleatorios
     * introducidos por la dificultad.
     */
    public void marcarJugadaRealmenteElegida(int fila, int columna, char simbolo) {

        for (AnalisisJugada analisis : analisisUltimaJugada) {
            analisis.setElegida(false);
        }

        for (AnalisisJugada analisis : analisisUltimaJugada) {
            if (analisis.getTablero().obtenerCasilla(fila, columna) == simbolo) {
                analisis.setElegida(true);
                break;
            }
        }
    }
}