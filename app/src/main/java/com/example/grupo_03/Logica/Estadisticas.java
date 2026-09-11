package com.example.grupo_03.Logica;

import android.content.Context;
import android.content.SharedPreferences;

public class Estadisticas {

    private static Estadisticas instancia;
    private SharedPreferences prefs;

    public int totalPartidas = 0;
    public int victoriasGlobalesX = 0;
    public int victoriasGlobalesO = 0;
    public int empatesGlobales = 0;

    // --- NUEVAS VARIABLES PARA IA VS IA ---
    public int cvcVictoriasFacil = 0;
    public int cvcVictoriasMedio = 0;
    public int cvcVictoriasDificil = 0;

    public static class StatsHvC {
        public int jugadas = 0;
        public int victoriasHumano = 0;
        public int victoriasIA = 0;
        public int empates = 0;
    }

    public static class StatsGeneral {
        public int jugadas = 0;
        public int victoriasX = 0;
        public int victoriasO = 0;
        public int empates = 0;
    }

    public StatsHvC hvcFacil = new StatsHvC();
    public StatsHvC hvcMedio = new StatsHvC();
    public StatsHvC hvcDificil = new StatsHvC();

    public StatsGeneral hvh = new StatsGeneral();
    public StatsGeneral cvc = new StatsGeneral();

    private Estadisticas(Context context) {
        prefs = context.getSharedPreferences("EstadisticasTresEnRaya", Context.MODE_PRIVATE);
        cargarDatosLocales();
    }

    public static void inicializar(Context context) {
        if (instancia == null) {
            instancia = new Estadisticas(context.getApplicationContext());
        }
    }

    public static Estadisticas getInstancia() {
        if (instancia == null) {
            throw new IllegalStateException("Estadisticas no inicializadas.");
        }
        return instancia;
    }

    private void cargarDatosLocales() {
        totalPartidas = prefs.getInt("global_jugadas", 0);
        victoriasGlobalesX = prefs.getInt("global_vicX", 0);
        victoriasGlobalesO = prefs.getInt("global_vicO", 0);
        empatesGlobales = prefs.getInt("global_empates", 0);

        cvcVictoriasFacil = prefs.getInt("cvc_vicFacil", 0);
        cvcVictoriasMedio = prefs.getInt("cvc_vicMedio", 0);
        cvcVictoriasDificil = prefs.getInt("cvc_vicDificil", 0);

        hvcFacil.jugadas = prefs.getInt("facil_jugadas", 0);
        hvcFacil.victoriasHumano = prefs.getInt("facil_victoriasH", 0);
        hvcFacil.victoriasIA = prefs.getInt("facil_victoriasIA", 0);
        hvcFacil.empates = prefs.getInt("facil_empates", 0);

        hvcMedio.jugadas = prefs.getInt("medio_jugadas", 0);
        hvcMedio.victoriasHumano = prefs.getInt("medio_victoriasH", 0);
        hvcMedio.victoriasIA = prefs.getInt("medio_victoriasIA", 0);
        hvcMedio.empates = prefs.getInt("medio_empates", 0);

        hvcDificil.jugadas = prefs.getInt("dificil_jugadas", 0);
        hvcDificil.victoriasHumano = prefs.getInt("dificil_victoriasH", 0);
        hvcDificil.victoriasIA = prefs.getInt("dificil_victoriasIA", 0);
        hvcDificil.empates = prefs.getInt("dificil_empates", 0);

        hvh.jugadas = prefs.getInt("hvh_jugadas", 0);
        hvh.victoriasX = prefs.getInt("hvh_victoriasX", 0);
        hvh.victoriasO = prefs.getInt("hvh_victoriasO", 0);
        hvh.empates = prefs.getInt("hvh_empates", 0);

        cvc.jugadas = prefs.getInt("cvc_jugadas", 0);
        cvc.victoriasX = prefs.getInt("cvc_victoriasX", 0);
        cvc.victoriasO = prefs.getInt("cvc_victoriasO", 0);
        cvc.empates = prefs.getInt("cvc_empates", 0);
    }

    private void guardarDatosLocales() {
        SharedPreferences.Editor editor = prefs.edit();

        editor.putInt("global_jugadas", totalPartidas);
        editor.putInt("global_vicX", victoriasGlobalesX);
        editor.putInt("global_vicO", victoriasGlobalesO);
        editor.putInt("global_empates", empatesGlobales);

        editor.putInt("cvc_vicFacil", cvcVictoriasFacil);
        editor.putInt("cvc_vicMedio", cvcVictoriasMedio);
        editor.putInt("cvc_vicDificil", cvcVictoriasDificil);

        editor.putInt("facil_jugadas", hvcFacil.jugadas);
        editor.putInt("facil_victoriasH", hvcFacil.victoriasHumano);
        editor.putInt("facil_victoriasIA", hvcFacil.victoriasIA);
        editor.putInt("facil_empates", hvcFacil.empates);

        editor.putInt("medio_jugadas", hvcMedio.jugadas);
        editor.putInt("medio_victoriasH", hvcMedio.victoriasHumano);
        editor.putInt("medio_victoriasIA", hvcMedio.victoriasIA);
        editor.putInt("medio_empates", hvcMedio.empates);

        editor.putInt("dificil_jugadas", hvcDificil.jugadas);
        editor.putInt("dificil_victoriasH", hvcDificil.victoriasHumano);
        editor.putInt("dificil_victoriasIA", hvcDificil.victoriasIA);
        editor.putInt("dificil_empates", hvcDificil.empates);

        editor.putInt("hvh_jugadas", hvh.jugadas);
        editor.putInt("hvh_victoriasX", hvh.victoriasX);
        editor.putInt("hvh_victoriasO", hvh.victoriasO);
        editor.putInt("hvh_empates", hvh.empates);

        editor.putInt("cvc_jugadas", cvc.jugadas);
        editor.putInt("cvc_victoriasX", cvc.victoriasX);
        editor.putInt("cvc_victoriasO", cvc.victoriasO);
        editor.putInt("cvc_empates", cvc.empates);

        editor.apply();
    }

    private void registrarVictoriaGlobal(char ganador, boolean esEmpate) {
        totalPartidas++;
        if (esEmpate) {
            empatesGlobales++;
        } else if (ganador == 'X') {
            victoriasGlobalesX++;
        } else if (ganador == 'O') {
            victoriasGlobalesO++;
        }
    }

    public void registrarHvC(char ganador, char simboloHumano, Dificultad dificultad, boolean esEmpate) {
        registrarVictoriaGlobal(ganador, esEmpate);
        StatsHvC stats;
        switch (dificultad) {
            case FACIL: stats = hvcFacil; break;
            case MEDIO: stats = hvcMedio; break;
            default: stats = hvcDificil; break;
        }

        stats.jugadas++;
        if (esEmpate) {
            stats.empates++;
        } else if (ganador == simboloHumano) {
            stats.victoriasHumano++;
        } else {
            stats.victoriasIA++;
        }
        guardarDatosLocales();
    }

    public void registrarHvH(char ganador, boolean esEmpate) {
        registrarVictoriaGlobal(ganador, esEmpate);
        hvh.jugadas++;
        if (esEmpate) hvh.empates++;
        else if (ganador == 'X') hvh.victoriasX++;
        else if (ganador == 'O') hvh.victoriasO++;
        guardarDatosLocales();
    }

    // --- AHORA RECIBE LAS DIFICULTADES DE AMBAS IA --
    public void registrarCvC(char ganador, boolean esEmpate, Dificultad difX, Dificultad difO) {
        registrarVictoriaGlobal(ganador, esEmpate);
        cvc.jugadas++;

        if (esEmpate) {
            cvc.empates++;
        } else {
            if (ganador == 'X') {
                cvc.victoriasX++;
                incrementarVictoriaDificultadCvC(difX);
            } else if (ganador == 'O') {
                cvc.victoriasO++;
                incrementarVictoriaDificultadCvC(difO);
            }
        }
        guardarDatosLocales();
    }

    private void incrementarVictoriaDificultadCvC(Dificultad dif) {
        switch (dif) {
            case FACIL: cvcVictoriasFacil++; break;
            case MEDIO: cvcVictoriasMedio++; break;
            case DIFICIL: cvcVictoriasDificil++; break;
        }
    }

    // Borra permanentemente todas las estadísticas guardadas (tanto en
    // memoria como en SharedPreferences) y las deja en cero. Útil para
    // pruebas, o si el usuario quiere empezar a contar desde cero.
    public void reiniciar() {
        totalPartidas = 0;
        victoriasGlobalesX = 0;
        victoriasGlobalesO = 0;
        empatesGlobales = 0;

        cvcVictoriasFacil = 0;
        cvcVictoriasMedio = 0;
        cvcVictoriasDificil = 0;

        hvcFacil = new StatsHvC();
        hvcMedio = new StatsHvC();
        hvcDificil = new StatsHvC();

        hvh = new StatsGeneral();
        cvc = new StatsGeneral();

        guardarDatosLocales();
    }
}