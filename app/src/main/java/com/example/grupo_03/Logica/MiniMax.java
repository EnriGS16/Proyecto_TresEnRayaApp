package com.example.grupo_03.Logica;

import com.example.grupo_03.Estructuras.Tree;
import com.example.grupo_03.Modelo.Tablero;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

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

        // NOTA: Ya no marcamos la jugada como elegida aquí.
        // Eso ahora lo hace la clase Partida usando el método marcarJugadaRealmenteElegida()

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

    // --- NUEVO MÉTODO --
    // Nos permite marcar como "Elegida" la jugada que la IA realmente colocó en el tablero,
    // sea por decisión óptima (Minimax) o por aleatoriedad (Dificultad fácil/media)
    public void marcarJugadaRealmenteElegida(int fila, int columna, char simbolo) {

        // Primero nos aseguramos de que ninguna jugada esté marcada
        for (AnalisisJugada analisis : analisisUltimaJugada) {
            analisis.setElegida(false);
        }

        // Luego buscamos cuál de los tableros analizados coincide con la jugada que hizo la IA
        for (AnalisisJugada analisis : analisisUltimaJugada) {
            if (analisis.getTablero().obtenerCasilla(fila, columna) == simbolo) {
                analisis.setElegida(true);
                break;
            }
        }
    }
}