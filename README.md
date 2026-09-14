# Tres en Raya — Grupo 03

Aplicación de Android para jugar Tres en Raya (Tic-Tac-Toe), desarrollada como
proyecto académico. Incluye una inteligencia artificial basada en el
algoritmo **Minimax**, tres modalidades de juego, seguimiento de
estadísticas, y una interfaz adaptada tanto a orientación vertical como
horizontal, con soporte de modo oscuro y claro.

## Características

- **3 modalidades de juego:**
  - **Humano vs Computadora**, con 3 niveles de dificultad (Fácil, Medio,
    Difícil) y recomendación de jugada en tiempo real.
  - **Humano vs Humano**, partida local por turnos en el mismo dispositivo.
  - **Computadora vs Computadora**, modo espectador donde dos IAs juegan
    entre sí (cada una con su propia dificultad configurable), con control
    de velocidad de la simulación.
- **Historial y análisis de cada partida**: revisa las jugadas realizadas y
  el análisis que hizo Minimax al decidir su movimiento.
- **Estadísticas globales**, separadas por modalidad y dificultad, con
  persistencia entre sesiones y opción de restablecerlas.
- **Modo oscuro / claro**, con un switch en el menú lateral que recuerda tu
  preferencia aunque cierres la app.
- **Diseño adaptativo**: todas las pantallas se ajustan correctamente tanto
  en orientación vertical como horizontal.
- Pantallas de **Reglas e instrucciones** y **Desarrolladores** del
  proyecto.

## Tecnologías utilizadas

- **Kotlin** + **Jetpack Compose** (interfaz declarativa)
- **Material 3** (componentes de diseño)
- **Java** (lógica del juego: tablero, partida, Minimax, análisis)
- **SharedPreferences** (persistencia de estadísticas y preferencia de tema)
- Estructuras de datos propias (árbol N-ario) para el algoritmo Minimax

## Estructura del proyecto

```
app/src/main/java/com/example/grupo_03/
├── Estructuras/     # Árbol genérico (Tree, NodeTree) usado por Minimax
├── Logica/          # Partida, MiniMax, Estadisticas, Dificultad, etc.
├── Modelo/          # Tablero
├── ui/theme/        # Tema de Jetpack Compose
└── MainActivity.kt  # Toda la interfaz (pantallas y componentes)
```

## Cómo ejecutar el proyecto

1. Clona este repositorio:
   ```
   git clone https://github.com/EnriGS16/Proyecto_TresEnRayaApp.git
   ```
2. Ábrelo con **Android Studio**.
3. Espera a que Gradle sincronice las dependencias automáticamente.
4. Ejecuta la app (▶) en un emulador o en un dispositivo físico con
   depuración USB habilitada.

## Descargar la aplicación

Escanea el siguiente código QR para descargar directamente la aplicación
en un dispositivo Android:

![QR para descargar Tres en Raya](assets/QR_TresEnRaya.png)

### Descarga directa

[Descargar APK](https://github.com/EnriGS16/Proyecto_TresEnRayaApp/releases/download/v1.0/3-Raya.apk)

## Integrantes — Grupo 03

- Steven Guzman
- Carlos Garcia
- David Jalon


