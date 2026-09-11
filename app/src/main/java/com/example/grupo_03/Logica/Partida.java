package com.example.grupo_03.Logica;

import com.example.grupo_03.Modelo.Tablero;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

/*
Partida implementa Serializable para que Jetpack Compose pueda guardarla
directamente con rememberSaveable (y asi sobreviva a una rotacion de
pantalla o a que Android mate el proceso en segundo plano). Para que esto
funcione, TODOS los objetos que Partida guarda como campos (Tablero,
MiniMax, HistorialJugada, AnalisisJugada, AnalisisRespuesta) tambien deben
ser Serializable; Dificultad ya lo es porque es un enum, y Random y
LinkedList/ArrayList ya son Serializable por si solos.
*/
public class Partida implements Serializable {
    private static final long serialVersionUID = 1L;

    private Tablero tablero;
    private final MiniMax minimax;
    private final char simboloHumano;
    private final char simboloComputadora;
    private char turnoActual;
    private Dificultad dificultad;
    private final Random random;
    private final LinkedList<HistorialJugada> historial;

    private boolean modoComputadoraVsComputadora = false;
    private Dificultad dificultadJugador1;
    private Dificultad dificultadJugador2;
    private MiniMax minimaxJugador1;
    private MiniMax minimaxJugador2;

    private boolean modoHumanoVsHumano = false;

    // Constructor: Humano vs Computadora
    public Partida(char simboloHumano, char simboloComputadora, boolean iniciaHumano){
        this(simboloHumano, simboloComputadora, iniciaHumano, Dificultad.DIFICIL);
    }

    public Partida(char simboloHumano, char simboloComputadora, boolean iniciaHumano, Dificultad dificultad){
        this.tablero = new Tablero();
        this.minimax = new MiniMax();
        this.random = new Random();

        this.simboloHumano = simboloHumano;
        this.simboloComputadora = simboloComputadora;
        this.dificultad = dificultad;
        this.historial = new LinkedList<>();

        if(iniciaHumano){
            this.turnoActual = simboloHumano;
        }else{
            this.turnoActual = simboloComputadora;
        }
    }

    // Constructor: Computadora vs Computadora
    public Partida(char simboloJugador1, char simboloJugador2, char simboloQueInicia, Dificultad dificultadJugador1, Dificultad dificultadJugador2){
        this.tablero = new Tablero();
        this.minimax = new MiniMax();
        this.random = new Random();

        this.simboloHumano = simboloJugador1;
        this.simboloComputadora = simboloJugador2;
        this.dificultad = dificultadJugador1;
        this.historial = new LinkedList<>();

        this.modoComputadoraVsComputadora = true;
        this.dificultadJugador1 = dificultadJugador1;
        this.dificultadJugador2 = dificultadJugador2;
        this.minimaxJugador1 = new MiniMax();
        this.minimaxJugador2 = new MiniMax();

        this.turnoActual = simboloQueInicia;
    }

    // Constructor: Humano vs Humano (ACTUALIZADO para recibir el símbolo inicial)
    public Partida(boolean modoHumanoVsHumano, char simboloQueInicia) {
        this.tablero = new Tablero();
        this.minimax = new MiniMax();
        this.random = new Random();

        this.simboloHumano = 'X';
        this.simboloComputadora = 'O';
        this.historial = new LinkedList<>();

        this.modoHumanoVsHumano = modoHumanoVsHumano;
        this.turnoActual = simboloQueInicia;
    }

    public boolean isModoComputadoraVsComputadora(){
        return modoComputadoraVsComputadora;
    }

    public boolean isModoHumanoVsHumano() {
        return modoHumanoVsHumano;
    }

    public char getSimboloJugador1(){
        return simboloHumano;
    }

    public char getSimboloJugador2(){
        return simboloComputadora;
    }

    public Dificultad getDificultadJugador1(){
        return dificultadJugador1;
    }

    public Dificultad getDificultadJugador2(){
        return dificultadJugador2;
    }

    public Tablero getTablero(){
        return tablero;
    }

    public char getTurnoActual(){
        return turnoActual;
    }

    public Dificultad getDificultad(){
        return dificultad;
    }

    public void setDificultad(Dificultad dificultad){
        this.dificultad = dificultad;
    }

    public boolean jugarHumano(int fila, int columna){
        if(turnoActual != simboloHumano){
            return false;
        }
        if(tablero.isFull()||tablero.isWinner(simboloHumano)||tablero.isWinner(simboloComputadora)){
            return false;
        }
        if(!tablero.colocarSimbolo(fila, columna, simboloHumano)){
            return false;
        }

        historial.add(new HistorialJugada(simboloHumano, fila, columna, tablero));

        if(!tablero.isFull()&& !tablero.isWinner(simboloHumano)){
            turnoActual = simboloComputadora;
        }
        return true;
    }

    // Método de juego: Humano vs Humano (ACTUALIZADO para generar el análisis)
    public boolean jugarTurnoHumanoVsHumano(int fila, int columna) {
        if (!modoHumanoVsHumano) {
            return false;
        }
        if (partidaTerminada()) {
            return false;
        }

        char simboloEnTurno = turnoActual;
        char simboloRival = (simboloEnTurno == 'X') ? 'O' : 'X';

        // 1. Generamos el análisis simulado de las opciones antes de mover
        minimax.obtenerMejorMovimiento(tablero, simboloEnTurno, simboloRival);

        // 2. Colocamos el símbolo en el tablero
        if (!tablero.colocarSimbolo(fila, columna, simboloEnTurno)) {
            return false;
        }

        // 3. Marcamos la jugada que el humano REALMENTE hizo
        minimax.marcarJugadaRealmenteElegida(fila, columna, simboloEnTurno);

        historial.add(new HistorialJugada(simboloEnTurno, fila, columna, tablero));

        if (!tablero.isFull() && !tablero.isWinner(simboloEnTurno)) {
            turnoActual = simboloRival;
        }
        return true;
    }

    public boolean jugarComputadora(){
        if(turnoActual!= simboloComputadora){
            return false;
        }
        if(tablero.isFull()||tablero.isWinner(simboloHumano)||tablero.isWinner(simboloComputadora)){
            return false;
        }

        int[] movimiento = decidirMovimientoComputadora();

        if(movimiento ==null){
            return false;
        }

        tablero.colocarSimbolo(movimiento[0],movimiento[1],simboloComputadora);

        minimax.marcarJugadaRealmenteElegida(movimiento[0], movimiento[1], simboloComputadora);

        historial.add(new HistorialJugada(simboloComputadora, movimiento[0], movimiento[1], tablero));

        if(!tablero.isFull()&& !tablero.isWinner(simboloComputadora)){
            turnoActual = simboloHumano;
        }
        return true;
    }

    private int[] decidirMovimientoComputadora(){
        int[] movimientoMinimax = minimax.obtenerMejorMovimiento(tablero, simboloComputadora, simboloHumano);
        if(movimientoMinimax == null){
            return null;
        }
        if(random.nextDouble() < dificultad.getProbabilidadMinimax()){
            return movimientoMinimax;
        }
        int[] movimientoAleatorio = obtenerMovimientoAleatorio();
        if(movimientoAleatorio == null){
            return movimientoMinimax;
        }
        return movimientoAleatorio;
    }

    private int[] obtenerMovimientoAleatorio(){
        ArrayList<int[]> casillasLibres = new ArrayList<>();
        for (int fila = 0; fila < Tablero.tamanio; fila++) {
            for (int columna = 0; columna < Tablero.tamanio; columna++) {
                if (tablero.obtenerCasilla(fila, columna) == Tablero.vacio) {
                    casillasLibres.add(new int[]{fila, columna});
                }
            }
        }
        if (casillasLibres.isEmpty()) {
            return null;
        }
        int indice = random.nextInt(casillasLibres.size());
        return casillasLibres.get(indice);
    }

    public boolean jugarTurnoAutomatico(){
        if(!modoComputadoraVsComputadora){
            return false;
        }
        if(partidaTerminada()){
            return false;
        }

        char simboloEnTurno = turnoActual;
        char simboloRival;
        Dificultad dificultadEnTurno;
        MiniMax minimaxEnTurno;

        if(simboloEnTurno == simboloHumano){
            simboloRival = simboloComputadora;
            dificultadEnTurno = dificultadJugador1;
            minimaxEnTurno = minimaxJugador1;
        } else {
            simboloRival = simboloHumano;
            dificultadEnTurno = dificultadJugador2;
            minimaxEnTurno = minimaxJugador2;
        }

        int[] movimiento = decidirMovimientoAutomatico(simboloEnTurno, simboloRival, dificultadEnTurno, minimaxEnTurno);

        if(movimiento == null){
            return false;
        }

        tablero.colocarSimbolo(movimiento[0], movimiento[1], simboloEnTurno);
        minimaxEnTurno.marcarJugadaRealmenteElegida(movimiento[0], movimiento[1], simboloEnTurno);

        historial.add(new HistorialJugada(simboloEnTurno, movimiento[0], movimiento[1], tablero));

        if(!tablero.isFull() && !tablero.isWinner(simboloEnTurno)){
            turnoActual = simboloRival;
        }

        return true;
    }

    private int[] decidirMovimientoAutomatico(char simboloEnTurno, char simboloRival, Dificultad dificultadEnTurno, MiniMax minimaxEnTurno){
        int[] movimientoOptimo = minimaxEnTurno.obtenerMejorMovimiento(tablero, simboloEnTurno, simboloRival);
        if(movimientoOptimo == null){
            return null;
        }
        if(random.nextDouble() < dificultadEnTurno.getProbabilidadMinimax()){
            return movimientoOptimo;
        }
        int[] movimientoAleatorio = obtenerMovimientoAleatorio();
        if(movimientoAleatorio == null){
            return movimientoOptimo;
        }
        return movimientoAleatorio;
    }

    public boolean partidaTerminada(){
        return tablero.isFull()||tablero.isWinner(simboloHumano)||tablero.isWinner(simboloComputadora);
    }

    public char obtenerGanador(){
        if(tablero.isWinner(simboloHumano)){
            return simboloHumano;
        }
        if(tablero.isWinner(simboloComputadora)){
            return simboloComputadora;
        }
        return Tablero.vacio;
    }

    public boolean esEmpate(){
        return tablero.isFull()&&!tablero.isWinner(simboloHumano)&&!tablero.isWinner(simboloComputadora);
    }

    public int[] recomendarJugadaHumano(){
        if(turnoActual!=simboloHumano||partidaTerminada()){
            return null;
        }
        MiniMax minimaxRecomendacion = new MiniMax();
        return minimaxRecomendacion.obtenerMejorMovimiento(tablero,simboloHumano,simboloComputadora);
    }

    public int[] recomendarJugadaHumanoVsHumano() {
        if (partidaTerminada()) {
            return null;
        }
        MiniMax minimaxRecomendacion = new MiniMax();
        char oponente = (turnoActual == 'X') ? 'O' : 'X';
        return minimaxRecomendacion.obtenerMejorMovimiento(tablero, turnoActual, oponente);
    }

    public ArrayList<AnalisisJugada> getAnalisisUltimaJugada() {
        if (modoComputadoraVsComputadora) {
            // Usamos el historial (quien jugó de verdad la última ficha) en vez
            // de turnoActual: turnoActual NO cambia cuando esa última jugada
            // termina la partida (victoria o empate), asi que basarnos en el
            // turno fallaba justo en ese caso y mostraba el analisis del jugador equivocado.
            if (historial.isEmpty()) {
                return new ArrayList<>();
            }
            char ultimoJugador = historial.getLast().getJugador();
            if (ultimoJugador == simboloHumano) {
                return minimaxJugador1.getAnalisisUltimaJugada();
            } else {
                return minimaxJugador2.getAnalisisUltimaJugada();
            }
        }
        return minimax.getAnalisisUltimaJugada();
    }

    public char getSimboloHumano(){
        return simboloHumano;
    }

    public LinkedList<HistorialJugada> getHistorial() {
        return historial;
    }
}