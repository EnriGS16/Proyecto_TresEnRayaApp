package com.example.grupo_03.Modelo;

import java.io.Serializable;
import java.util.ArrayList;

public class Tablero implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final int tamanio = 3;
    public static final char vacio = ' ';
    private final char[][] casillas;
    public Tablero(){
        this.casillas= new char[tamanio][tamanio];
        for(int fila = 0; fila<tamanio; fila++){
            for (int columna = 0; columna<tamanio;columna++){
                casillas[fila][columna] = vacio; //''|''|''

            }
        }
    }

    /*Haremos un metodo que valide si se ingresara
    correctamente la posicion del tablero, mas que nada verificar si es valida o no
    */
    public boolean posicionValida(int fila, int columna){
        return fila>=0 && fila<tamanio && columna>=0 && columna<tamanio;
    }
    /* Crearemos un metodo para consultar una casilla
    sin tratar de dejar tan expuesta la matriz, como el atributo es private
    otras clases no podran acceder directamente a ella:D
    */
    public char obtenerCasilla(int fila, int columna){
        if(!posicionValida(fila,columna)){
            throw new IllegalArgumentException("Posicion fuera del tablero");
        }
        return casillas[fila][columna];

    }
    //validamos si la casilla esta vacia con un metodo para poder
    // evitar escribir de nuevo columnas[1][2]==vacio,
    //queremos que nuestro trabajo sea lo más limpio posible y ser organizados.
    public boolean isEmpty(int fila, int columna){
        return obtenerCasilla(fila,columna)==vacio;
    }

    //Crearemos el metodo para poner X , O segun quiera el usuario
    //Chequee que usamos ya metodos anteriores, asi evitamos escribir tanto codigo
    //y podemos ahorrarnos daño visual con tanta cosa xd.
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
                /* Visualmente no queremos tener algo como X|O|X|
                porque el ultimo | no sería correcto, por eso validamos si
                columna es menor al tamaño -1.
                */
                if(columna<tamanio-1){
                    System.out.print("|");
                }
            }
            System.out.println();//saltamos a otra fila
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
        //Verificamos las filas
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
        //Verificamos las columnas
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
        //Chequeamos las diagnolaes
        boolean diagonalPrincipal = true;
        boolean diagonalSecundaria = true;
        for (int posicion=0; posicion<tamanio;posicion++){
            if(casillas[posicion][posicion]!=simbolo){
                diagonalPrincipal = false;
            }
            if(casillas[posicion][tamanio-1-posicion]!=simbolo){ //0|0|tamanio-1-0 primera vuelta
                //0|2-1-1|0 segunda vuelta, por eso escogemos la columna de esa forma.
                diagonalSecundaria = false;
            }

        }
        return diagonalPrincipal || diagonalSecundaria;
    }
    //generaremos varios estados posibles a partir de cada tablero actual
    public Tablero copy(){
        Tablero copia = new Tablero();
        for(int fila= 0; fila<tamanio; fila++){
            for ( int columna = 0; columna<tamanio; columna ++){
                /*Copiaremos el contenido de la casilla, no hacemos solo this porque
                estariamos referenciando al mismo objeto.*/
                copia.casillas[fila][columna] = this.casillas[fila][columna];


            }
        }
        return copia;
    }
    /* Una fila,columna o diagonal se encontrará disponible cuando para X no contenga ningun O
    Puede haber X,X,vacío, entonces esta disponible para q X gane
    o tambien X,vacio,vacio, puede volver a poner X
    vacio,vacio,vacio es algo que tambien serviría para que X inicie su jugada.
    Pero X,O, vacío, no sirve para que ganemos, porque O bloquea dicha posibilidad.
    Lo mismo se usa para O.

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
        //Contaremos las filas disponibles
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
        //Contaremos columnas disponibless
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
        //Diagonales
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
    /* Habiendo implementado el metodo calculateUtility para comenzar
    a desarrollar de mejor forma nuestro proyecto, ahora si vamos a generar los
    posibles estados del tablero, dado a que el tablero actual
    y un jugador con el turno, debemos producir todos los tableros que podrían
    resultar de colcuar su símbolo en cada casilla libre, esos
    tableros serían hijos de un nodo en el arbol.
    */
    //GenerateStates generara los estados del tablero.
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
