package com.example.grupo_03.Modelo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Representa la matriz 3x3 del juego Tres en Raya.
 * Maneja la lógica base del tablero: actualización de celdas, validación de estados de victoria
 * y generación de tableros resultantes para el cálculo heurístico del algoritmo Minimax.
 */

public class Tablero implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final int tamanio = 3;
    public static final char vacio = ' ';
    private final char[][] casillas;

    public Tablero(){
        this.casillas= new char[tamanio][tamanio];
        for(int fila = 0; fila<tamanio; fila++){
            for (int columna = 0; columna<tamanio;columna++){
                casillas[fila][columna] = vacio;
            }
        }
    }

    /** Verifica si las coordenadas ingresadas se encuentran dentro de los límites del tablero. */
    public boolean posicionValida(int fila, int columna){
        return fila>=0 && fila<tamanio && columna>=0 && columna<tamanio;
    }

    /** Obtiene el valor de una casilla específica sin exponer directamente la matriz de estado. */
    public char obtenerCasilla(int fila, int columna){
        if(!posicionValida(fila,columna)){
            throw new IllegalArgumentException("Posicion fuera del tablero");
        }
        return casillas[fila][columna];
    }

    /** Comprueba si una casilla específica se encuentra disponible. */
    public boolean isEmpty(int fila, int columna){
        return obtenerCasilla(fila,columna)==vacio;
    }

    /** Intenta colocar un símbolo en las coordenadas dadas, validando que sea un movimiento legal. */
    public boolean colocarSimbolo(int fila, int columna, char simbolo){
        if(!posicionValida(fila,columna)){
            return false;
        }
        if(!isEmpty(fila,columna)){
            return false;
        }
        if(simbolo!='X'&&simbolo!='O'){
            return false;
        }
        casillas[fila][columna]=simbolo;
        return true;
    }

    public void mostrarTablero(){
        for (int fila=0; fila<tamanio; fila++){
            for(int columna = 0; columna<tamanio; columna++){
                System.out.print(casillas[fila][columna]);

                // Formato visual para separar columnas, evitando imprimir el separador en la última iteración
                if(columna<tamanio-1){
                    System.out.print("|");
                }
            }
            System.out.println(); // Salto de línea por cada fila
            if(fila<tamanio-1){
                System.out.println("-----");
            }
        }
    }

    public boolean isFull(){
        for (int fila=0;fila<tamanio; fila++){
            for(int columna = 0; columna<tamanio;columna++){
                if(casillas[fila][columna]==vacio ){
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isWinner(char simbolo){
        if(simbolo!='X'&& simbolo!='O'){
            return false;
        }

        // Verificación de filas
        for(int fila = 0; fila<tamanio; fila++){
            boolean filaCompleta = true;
            for(int columna=0; columna<tamanio;columna++){
                if(casillas[fila][columna]!=simbolo){
                    filaCompleta = false;
                    break;
                }
            }
            if(filaCompleta){return true;}
        }

        // Verificación de columnas
        for(int columna=0; columna<tamanio;columna++){
            boolean columnaCompleta = true;
            for(int fila= 0; fila<tamanio;fila++){
                if(casillas[fila][columna]!= simbolo){
                    columnaCompleta = false;
                    break;
                }
            }
            if(columnaCompleta){return true;}
        }

        // Verificación de diagonales
        boolean diagonalPrincipal = true;
        boolean diagonalSecundaria = true;
        for (int posicion=0; posicion<tamanio;posicion++){
            if(casillas[posicion][posicion]!=simbolo){
                diagonalPrincipal = false;
            }
            if(casillas[posicion][tamanio-1-posicion]!=simbolo){
                // Verifica la correspondencia en la diagonal inversa
                diagonalSecundaria = false;
            }
        }
        return diagonalPrincipal || diagonalSecundaria;
    }

    /** Crea una copia profunda del tablero actual para simular estados sin alterar el original. */
    public Tablero copy(){
        Tablero copia = new Tablero();
        for(int fila= 0; fila<tamanio; fila++){
            for ( int columna = 0; columna<tamanio; columna ++){
                // Copia valor por valor para evitar referenciar al mismo objeto en memoria
                copia.casillas[fila][columna] = this.casillas[fila][columna];
            }
        }
        return copia;
    }

    /**
     * Cuenta la cantidad de líneas (filas, columnas, diagonales) que aún se encuentran
     * disponibles para que un jugador gane, es decir, que no están interceptadas por el oponente.
     */
    public int countAvailableLines(char simbolo) {

        if (simbolo != 'X' && simbolo != 'O') {
            return 0;
        }
        char oponente;
        if(simbolo=='X'){
            oponente = 'O';
        } else{
            oponente = 'X';
        }
        int lineasDisponibles = 0;

        for ( int fila = 0; fila<tamanio; fila++){
            boolean disponible = true;
            for(int columna = 0; columna<tamanio; columna++){
                if(casillas[fila][columna]==oponente){
                    disponible = false;
                    break;
                }
            }
            if(disponible){
                lineasDisponibles++;
            }
        }

        for(int columna = 0; columna<tamanio; columna++){
            boolean disponible = true;
            for(int fila = 0; fila<tamanio;fila++){
                if(casillas[fila][columna]== oponente){
                    disponible = false;
                    break;
                }
            }
            if(disponible){
                lineasDisponibles++;
            }
        }

        boolean diagonalPrincipalDisponible = true;
        boolean diagonalSecundariaDisponible = true;
        for(int posicion = 0; posicion<tamanio; posicion++){
            if(casillas[posicion][posicion]==oponente){
                diagonalPrincipalDisponible= false;
            }
            if(casillas[posicion][tamanio-1-posicion]==oponente){
                diagonalSecundariaDisponible = false;
            }
        }

        if(diagonalPrincipalDisponible){
            lineasDisponibles++;
        }
        if(diagonalSecundariaDisponible){
            lineasDisponibles++;
        }
        return lineasDisponibles;
    }

    public int calculateUtility(char simbolo){
        if(simbolo!='X' && simbolo !='O'){
            return 0;
        }
        char oponente;
        if(simbolo=='X'){
            oponente = 'O';
        }else{
            oponente= 'X';
        }
        int lineasJugador= countAvailableLines(simbolo);
        int lineasOponente = countAvailableLines(oponente);
        return lineasJugador - lineasOponente;
    }

    /**
     * Genera un conjunto de todos los estados posibles derivados del tablero actual, asumiendo
     * el turno del jugador indicado. Estos estados servirán como nodos hijos en el árbol.
     */
    public ArrayList<Tablero> generateStates(char simbolo){
        ArrayList<Tablero> estados = new ArrayList<>();
        if(simbolo !='X'&&simbolo!='O'){
            return estados;
        }
        for (int fila = 0; fila < tamanio; fila++) {
            for (int columna = 0; columna < tamanio; columna++) {
                if (isEmpty(fila, columna)) {
                    Tablero nuevoEstado = this.copy();
                    nuevoEstado.colocarSimbolo(fila, columna, simbolo);
                    estados.add(nuevoEstado);
                }
            }
        }
        return estados;
    }
}