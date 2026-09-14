package com.example.grupo_03

import android.os.Bundle
import android.content.Context
import android.content.SharedPreferences
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.window.Dialog
import com.example.grupo_03.Logica.Partida
import com.example.grupo_03.Logica.HistorialJugada
import com.example.grupo_03.Logica.Dificultad
import com.example.grupo_03.Logica.Estadisticas
import com.example.grupo_03.Modelo.Tablero
import com.example.grupo_03.ui.theme.Grupo_03Theme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.offset
import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.window.DialogProperties


// ============================================================================
// SECCIÓN 1: COLORES Y CONSTANTES GLOBALES
// Paleta de colores y valores fijos que usa toda la app.
//
// modoOscuro es el interruptor global de tema: true = oscuro (por defecto),
// false = claro. Cada color de aquí abajo es una propiedad calculada
// (get() =) en vez de un valor fijo, así que se recalcula sola cada vez que
// modoOscuro cambia, y como es un mutableStateOf, Compose recompone
// automáticamente toda la app cuando el switch del menú lateral lo cambia.
// ============================================================================

var modoOscuro by mutableStateOf(true)

// Guarda y recupera el tema (claro/oscuro) elegido por el usuario usando
// SharedPreferences, para que se mantenga igual aunque cierre y vuelva a
// abrir la app (se inicializa una vez desde MainActivity.onCreate).
object Preferencias {
    private const val ARCHIVO = "PreferenciasTresEnRaya"
    private const val CLAVE_MODO_OSCURO = "modo_oscuro"

    private lateinit var prefs: SharedPreferences

    fun inicializar(context: Context) {
        prefs = context.getSharedPreferences(ARCHIVO, Context.MODE_PRIVATE)
        // Oscuro por defecto la primera vez que se abre la app
        modoOscuro = prefs.getBoolean(CLAVE_MODO_OSCURO, true)
    }

    fun guardarModoOscuro(valor: Boolean) {
        prefs.edit().putBoolean(CLAVE_MODO_OSCURO, valor).apply()
    }
}

private val FondoAplicacionColor: Color get() = if (modoOscuro) Color(0xFF202124) else Color(0xFFF7F7F9)

private val FondoSeccion: Color get() = if (modoOscuro) Color(0xFF292A2D) else Color(0xFFFFFFFF)

private val AzulX: Color get() = if (modoOscuro) Color(0xFF60A5FA) else Color(0xFF2563EB)

private val RojoO: Color get() = if (modoOscuro) Color(0xFFF87171) else Color(0xFFDC2626)

private val VerdePrincipal: Color get() = if (modoOscuro) Color(0xFF4CAF6A) else Color(0xFF2E9E52)

private val CasillaLibre: Color get() = if (modoOscuro) Color(0xFF202124) else Color(0xFFF7F7F9)

private val CasillaDeshabilitada: Color get() = if (modoOscuro) Color(0xFF202124) else Color(0xFFF7F7F9)

private val BordeCasilla: Color get() = if (modoOscuro) Color(0xFF6B6B6B) else Color(0xFFB8B8BD)

private val TextoPrincipal: Color get() = if (modoOscuro) Color(0xFFF9FAFB) else Color(0xFF17181A)

private val TextoSecundario: Color get() = if (modoOscuro) Color(0xFFB8B8B8) else Color(0xFF5F6368)

// Superficie secundaria (tarjetas/opciones no seleccionadas dentro de otra tarjeta)
private val SuperficieSecundaria: Color get() = if (modoOscuro) Color(0xFF242528) else Color(0xFFEFEFF2)

// Superficie más oscura/clara aún, para elementos anidados dentro de diálogos
private val SuperficieOscura: Color get() = if (modoOscuro) Color(0xFF1D1E20) else Color(0xFFE4E4E8)

// Borde sutil usado en tarjetas y opciones no seleccionadas
private val BordeSutil: Color get() = if (modoOscuro) Color(0xFF3A3B3F) else Color(0xFFD1D1D6)

// Resalta la casilla recomendada o ganadora dentro del tablero
private val CasillaResaltada: Color get() = if (modoOscuro) Color(0xFF315E3B) else Color(0xFFBFE8CC)


// ============================================================================
// SECCIÓN 2: ENUMS GLOBALES
// Modalidad de juego y velocidad de simulación, usados en varias pantallas.
// ============================================================================

enum class ModoJuego {
    HUMANO_VS_COMPUTADORA,
    HUMANO_VS_HUMANO,
    COMPUTADORA_VS_COMPUTADORA
}

enum class VelocidadSimulacion(val delayMs: Long, val etiqueta: String) {
    LENTA(1500L, "Lenta"),
    NORMAL(700L, "Normal"),
    RAPIDA(250L, "Rápida")
}


// ============================================================================
// SECCIÓN 3: PUNTO DE ENTRADA DE LA APP
// MainActivity, el composable raíz que decide qué pantalla mostrar, y el fondo visual base que envuelve a todas las pantallas.
// ============================================================================

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Estadisticas.inicializar(this)
        Preferencias.inicializar(this)

        enableEdgeToEdge()
        setContent {
            Grupo_03Theme {
                AplicacionTresEnRaya()
            }
        }
    }
}

@Composable
fun AplicacionTresEnRaya() {

    // rememberSaveable: asi la partida en curso (y con
    // que modo/dificultad se inicio) sobrevive tanto a una rotacion de
    // pantalla como a que Android mate el proceso en segundo plano.
    // Para que esto funcione, Partida (y todo lo que guarda adentro:
    // Tablero, MiniMax, HistorialJugada, AnalisisJugada, AnalisisRespuesta) implementa Serializable.
    var partida by rememberSaveable { mutableStateOf<Partida?>(null) }
    var modoSeleccionado by rememberSaveable { mutableStateOf(ModoJuego.HUMANO_VS_COMPUTADORA) }

    // Estado para controlar la pantalla de estadísticas
    var mostrarEstadisticas by rememberSaveable { mutableStateOf(false) }

    // Si el usuario presiona "atrás" (gesto o botón) estando en
    // "Estadisticas Globales", regresamos a la pantalla anterior en vez
    // de salir de la app
    BackHandler(enabled = mostrarEstadisticas) {
        mostrarEstadisticas = false
    }

    var dificultadActual by rememberSaveable { mutableStateOf(Dificultad.DIFICIL) }
    var dificultadXActual by rememberSaveable { mutableStateOf(Dificultad.DIFICIL) }
    var dificultadOActual by rememberSaveable { mutableStateOf(Dificultad.DIFICIL) }

    if (mostrarEstadisticas) {

        PantallaEstadisticas(onVolver = { mostrarEstadisticas = false })

    } else if (partida == null) {

        PantallaInicio(
            modoSeleccionado = modoSeleccionado,
            onModoCambiado = { modoSeleccionado = it },
            onComenzar = { simboloHumano, iniciaHumano, dificultad ->
                val simboloComputadora = if (simboloHumano == 'X') 'O' else 'X'
                val nuevaPartida = Partida(simboloHumano, simboloComputadora, iniciaHumano, dificultad)
                if (!iniciaHumano) { nuevaPartida.jugarComputadora() }
                dificultadActual = dificultad
                partida = nuevaPartida
            },
            onComenzarCpuVsCpu = { iniciaSimbolo, dificultadX, dificultadO ->
                val nuevaPartida = Partida('X', 'O', iniciaSimbolo, dificultadX, dificultadO)
                dificultadXActual = dificultadX
                dificultadOActual = dificultadO
                partida = nuevaPartida
            },
            onComenzarHvH = { iniciaSimbolo ->
                val nuevaPartida = Partida(true, iniciaSimbolo)
                partida = nuevaPartida
            },
            onVerEstadisticas = {
                mostrarEstadisticas = true
            }
        )

    } else if (modoSeleccionado == ModoJuego.COMPUTADORA_VS_COMPUTADORA) {

        PantallaJuegoCpuVsCpu(
            partida = partida!!,
            dificultadX = dificultadXActual,
            dificultadO = dificultadOActual,
            onNuevaPartida = { partida = null }
        )

    } else if (modoSeleccionado == ModoJuego.HUMANO_VS_HUMANO) {

        PantallaJuegoHumanoVsHumano(
            partida = partida!!,
            onNuevaPartida = { partida = null }
        )

    } else {

        PantallaJuego(
            partida = partida!!,
            dificultad = dificultadActual,
            onNuevaPartida = { partida = null }
        )
    }
}

@Composable
fun FondoAplicacion(contenido: @Composable () -> Unit) {
    Box(modifier = Modifier.fillMaxSize().background(FondoAplicacionColor)) { contenido() }
}


// ============================================================================
// SECCIÓN 4: PANTALLA DE INICIO
// Selección de símbolo, modalidad y dificultad antes de comenzar una partida.
// ============================================================================

@Composable
fun PantallaInicio(
    modoSeleccionado: ModoJuego,
    onModoCambiado: (ModoJuego) -> Unit,
    onComenzar: (Char, Boolean, Dificultad) -> Unit,
    onComenzarCpuVsCpu: (Char, Dificultad, Dificultad) -> Unit,
    onComenzarHvH: (Char) -> Unit,
    onVerEstadisticas: () -> Unit
) {
    var simboloSeleccionado by rememberSaveable { mutableStateOf('X') }
    var iniciaHumano by rememberSaveable { mutableStateOf(true) }
    var dificultadSeleccionada by rememberSaveable { mutableStateOf(Dificultad.DIFICIL) }
    var cpuIniciaSimbolo by rememberSaveable { mutableStateOf('X') }
    var dificultadXSeleccionada by rememberSaveable { mutableStateOf(Dificultad.DIFICIL) }
    var dificultadOSeleccionada by rememberSaveable { mutableStateOf(Dificultad.DIFICIL) }
    var hvhIniciaSimbolo by rememberSaveable { mutableStateOf('X') }
    var mostrarDesarrolladores by rememberSaveable { mutableStateOf(false) }
    var mostrarReglas by rememberSaveable { mutableStateOf(false) }

    val estadoBarraLateral = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scopeBarraLateral = rememberCoroutineScope()
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    BackHandler(enabled = estadoBarraLateral.isOpen) { scopeBarraLateral.launch { estadoBarraLateral.close() } }
    BackHandler(enabled = mostrarDesarrolladores) { mostrarDesarrolladores = false }
    BackHandler(enabled = mostrarReglas) { mostrarReglas = false }

    ModalNavigationDrawer(
        drawerState = estadoBarraLateral,
        drawerContent = {
            BarraLateral(
                onDesarrolladoresClick = { mostrarDesarrolladores = true; mostrarReglas = false; scopeBarraLateral.launch { estadoBarraLateral.close() } },
                onReglasClick = { mostrarReglas = true; mostrarDesarrolladores = false; scopeBarraLateral.launch { estadoBarraLateral.close() } },
                onEstadisticasClick = { onVerEstadisticas(); scopeBarraLateral.launch { estadoBarraLateral.close() } },
                modoSeleccionado = modoSeleccionado,
                onHumanoVsComputadoraClick = { onModoCambiado(ModoJuego.HUMANO_VS_COMPUTADORA); mostrarDesarrolladores = false; mostrarReglas = false; scopeBarraLateral.launch { estadoBarraLateral.close() } },
                onHumanoVsHumanoClick = { onModoCambiado(ModoJuego.HUMANO_VS_HUMANO); mostrarDesarrolladores = false; mostrarReglas = false; scopeBarraLateral.launch { estadoBarraLateral.close() } },
                onComputadoraVsComputadoraClick = { onModoCambiado(ModoJuego.COMPUTADORA_VS_COMPUTADORA); mostrarDesarrolladores = false; mostrarReglas = false; scopeBarraLateral.launch { estadoBarraLateral.close() } }
            )
        }
    ) {
        if (mostrarDesarrolladores) { PantallaDesarrolladores(onVolver = { mostrarDesarrolladores = false }) }
        else if (mostrarReglas) { PantallaReglas(onVolver = { mostrarReglas = false }) }
        else {
            FondoAplicacion {
                if (isLandscape) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .statusBarsPadding()
                            .padding(horizontal = 24.dp, vertical = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // --- Columna izquierda: 3 zonas fijas (arriba / centro / abajo) ---
                        Column(
                            modifier = Modifier.weight(1f).fillMaxHeight(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Zona superior: a la misma altura que el menú desplegable
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                    IconButton(
                                        onClick = { scopeBarraLateral.launch { estadoBarraLateral.open() } },
                                        modifier = Modifier
                                            .align(Alignment.CenterStart)
                                            // Compensamos el padding interno del botón para alinear el ícono con las secciones de abajo
                                            .offset(x = (-12).dp)
                                    ) { IconoMenuHamburguesa() }
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        InsigniaSimbolo('X', AzulX)
                                        Text("VS", color = TextoSecundario, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        InsigniaSimbolo('O', RojoO)
                                    }
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(text = "Tres en Raya", color = TextoPrincipal, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                            }

                            // Zona central: modalidad activa y su frase
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                EtiquetaPildora(texto = when (modoSeleccionado) { ModoJuego.HUMANO_VS_COMPUTADORA -> "Humano vs Computadora"; ModoJuego.HUMANO_VS_HUMANO -> "Humano vs Humano"; ModoJuego.COMPUTADORA_VS_COMPUTADORA -> "Computadora vs Computadora" }, color = VerdePrincipal)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = when (modoSeleccionado) { ModoJuego.HUMANO_VS_COMPUTADORA -> "Desafía a la computadora"; ModoJuego.HUMANO_VS_HUMANO -> "Juega por turnos con otra persona"; ModoJuego.COMPUTADORA_VS_COMPUTADORA -> "Observa a la IA jugar sola" }, color = TextoSecundario, fontSize = 13.sp, textAlign = TextAlign.Center)
                            }

                            // Zona inferior: botón de jugar y la pista del menú
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Button(
                                    modifier = Modifier.fillMaxWidth().height(50.dp),
                                    shape = RoundedCornerShape(25.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = VerdePrincipal, contentColor = Color(0xFF06220F)),
                                    onClick = {
                                        when (modoSeleccionado) {
                                            ModoJuego.HUMANO_VS_COMPUTADORA -> onComenzar(simboloSeleccionado, iniciaHumano, dificultadSeleccionada)
                                            ModoJuego.COMPUTADORA_VS_COMPUTADORA -> onComenzarCpuVsCpu(cpuIniciaSimbolo, dificultadXSeleccionada, dificultadOSeleccionada)
                                            ModoJuego.HUMANO_VS_HUMANO -> onComenzarHvH(hvhIniciaSimbolo)
                                        }
                                    }
                                ) {
                                    Text(text = if (modoSeleccionado == ModoJuego.COMPUTADORA_VS_COMPUTADORA) "Iniciar simulación" else "Jugar", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(text = "→", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(text = "Toca ☰ para ver más opciones", color = TextoSecundario, fontSize = 12.sp)
                            }
                        }

                        // --- Columna derecha: solo las opciones de la modalidad, compactas para no necesitar scroll ---
                        Box(
                            modifier = Modifier.weight(1.2f).fillMaxHeight().verticalScroll(rememberScrollState()),
                            contentAlignment = Alignment.Center
                        ) {
                            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = FondoSeccion), elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)) {
                                Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp)) {
                                    when (modoSeleccionado) {
                                        ModoJuego.HUMANO_VS_COMPUTADORA -> {
                                            EtiquetaSeccion(texto = "ELIGE TU SÍMBOLO")
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                                OpcionSimbolo(simbolo = 'X', seleccionado = simboloSeleccionado == 'X', colorSimbolo = AzulX, modifier = Modifier.weight(1f), onClick = { simboloSeleccionado = 'X' })
                                                OpcionSimbolo(simbolo = 'O', seleccionado = simboloSeleccionado == 'O', colorSimbolo = RojoO, modifier = Modifier.weight(1f), onClick = { simboloSeleccionado = 'O' })
                                            }
                                            Spacer(modifier = Modifier.height(12.dp))
                                            EtiquetaSeccion(texto = "¿QUIÉN COMIENZA?")
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                                OpcionInicio(texto = "Yo", seleccionado = iniciaHumano, modifier = Modifier.weight(1f), onClick = { iniciaHumano = true })
                                                OpcionInicio(texto = "Computadora", seleccionado = !iniciaHumano, modifier = Modifier.weight(1f), onClick = { iniciaHumano = false })
                                            }
                                            Spacer(modifier = Modifier.height(12.dp))
                                            SelectorDificultad(dificultadSeleccionada = dificultadSeleccionada, onSeleccion = { dificultadSeleccionada = it })
                                        }
                                        ModoJuego.HUMANO_VS_HUMANO -> {
                                            EtiquetaSeccion(texto = "¿QUÉ SÍMBOLO COMIENZA?")
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                                OpcionSimbolo(simbolo = 'X', seleccionado = hvhIniciaSimbolo == 'X', colorSimbolo = AzulX, modifier = Modifier.weight(1f), onClick = { hvhIniciaSimbolo = 'X' })
                                                OpcionSimbolo(simbolo = 'O', seleccionado = hvhIniciaSimbolo == 'O', colorSimbolo = RojoO, modifier = Modifier.weight(1f), onClick = { hvhIniciaSimbolo = 'O' })
                                            }
                                        }
                                        ModoJuego.COMPUTADORA_VS_COMPUTADORA -> {
                                            EtiquetaSeccion(texto = "¿QUÉ SÍMBOLO COMIENZA?")
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                                OpcionSimbolo(simbolo = 'X', seleccionado = cpuIniciaSimbolo == 'X', colorSimbolo = AzulX, modifier = Modifier.weight(1f), onClick = { cpuIniciaSimbolo = 'X' })
                                                OpcionSimbolo(simbolo = 'O', seleccionado = cpuIniciaSimbolo == 'O', colorSimbolo = RojoO, modifier = Modifier.weight(1f), onClick = { cpuIniciaSimbolo = 'O' })
                                            }
                                            Spacer(modifier = Modifier.height(12.dp))
                                            SelectorDificultad(titulo = "DIFICULTAD DE 'X'", dificultadSeleccionada = dificultadXSeleccionada, onSeleccion = { dificultadXSeleccionada = it })
                                            Spacer(modifier = Modifier.height(10.dp))
                                            SelectorDificultad(titulo = "DIFICULTAD DE 'O'", dificultadSeleccionada = dificultadOSeleccionada, onSeleccion = { dificultadOSeleccionada = it })
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    // CÓDIGO VERTICAL ORIGINAL
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .statusBarsPadding()
                            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            IconButton(
                                onClick = { scopeBarraLateral.launch { estadoBarraLateral.open() } },
                                modifier = Modifier
                                    .align(Alignment.CenterStart)
                                    // Compensamos también en vertical para que se vea alineado a la perfección
                                    .offset(x = (-12).dp)
                            ) { IconoMenuHamburguesa() }
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                InsigniaSimbolo(simbolo = 'X', color = AzulX)
                                Text(text = "VS", color = TextoSecundario, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                InsigniaSimbolo(simbolo = 'O', color = RojoO)
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(text = "Tres en Raya", color = TextoPrincipal, fontSize = 26.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.weight(1f))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            EtiquetaPildora(texto = when (modoSeleccionado) { ModoJuego.HUMANO_VS_COMPUTADORA -> "Humano vs Computadora"; ModoJuego.HUMANO_VS_HUMANO -> "Humano vs Humano"; ModoJuego.COMPUTADORA_VS_COMPUTADORA -> "Computadora vs Computadora" }, color = VerdePrincipal)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = when (modoSeleccionado) { ModoJuego.HUMANO_VS_COMPUTADORA -> "Desafía a la computadora"; ModoJuego.HUMANO_VS_HUMANO -> "Juega por turnos con otra persona"; ModoJuego.COMPUTADORA_VS_COMPUTADORA -> "Observa a la IA jugar sola" }, color = TextoSecundario, fontSize = 13.sp)
                            Spacer(modifier = Modifier.height(20.dp))
                            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = FondoSeccion), elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)) {
                                Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 18.dp)) {
                                    when (modoSeleccionado) {
                                        ModoJuego.HUMANO_VS_COMPUTADORA -> {
                                            EtiquetaSeccion(texto = "ELIGE TU SÍMBOLO")
                                            Spacer(modifier = Modifier.height(10.dp))
                                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                                OpcionSimbolo(simbolo = 'X', seleccionado = simboloSeleccionado == 'X', colorSimbolo = AzulX, modifier = Modifier.weight(1f), onClick = { simboloSeleccionado = 'X' })
                                                OpcionSimbolo(simbolo = 'O', seleccionado = simboloSeleccionado == 'O', colorSimbolo = RojoO, modifier = Modifier.weight(1f), onClick = { simboloSeleccionado = 'O' })
                                            }
                                            Spacer(modifier = Modifier.height(16.dp))
                                            DivisorSutil()
                                            Spacer(modifier = Modifier.height(16.dp))
                                            EtiquetaSeccion(texto = "¿QUIÉN COMIENZA?")
                                            Spacer(modifier = Modifier.height(10.dp))
                                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                                OpcionInicio(texto = "Yo", seleccionado = iniciaHumano, modifier = Modifier.weight(1f), onClick = { iniciaHumano = true })
                                                OpcionInicio(texto = "Computadora", seleccionado = !iniciaHumano, modifier = Modifier.weight(1f), onClick = { iniciaHumano = false })
                                            }
                                            Spacer(modifier = Modifier.height(16.dp))
                                            DivisorSutil()
                                            Spacer(modifier = Modifier.height(16.dp))
                                            SelectorDificultad(dificultadSeleccionada = dificultadSeleccionada, onSeleccion = { dificultadSeleccionada = it })
                                        }
                                        ModoJuego.HUMANO_VS_HUMANO -> {
                                            EtiquetaSeccion(texto = "¿QUÉ SÍMBOLO COMIENZA?")
                                            Spacer(modifier = Modifier.height(10.dp))
                                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                                OpcionSimbolo(simbolo = 'X', seleccionado = hvhIniciaSimbolo == 'X', colorSimbolo = AzulX, modifier = Modifier.weight(1f), onClick = { hvhIniciaSimbolo = 'X' })
                                                OpcionSimbolo(simbolo = 'O', seleccionado = hvhIniciaSimbolo == 'O', colorSimbolo = RojoO, modifier = Modifier.weight(1f), onClick = { hvhIniciaSimbolo = 'O' })
                                            }
                                            Spacer(modifier = Modifier.height(16.dp))
                                            DivisorSutil()
                                            Spacer(modifier = Modifier.height(16.dp))
                                            Text(text = "Dos jugadores se turnan en el mismo dispositivo.", color = TextoSecundario, fontSize = 12.sp)
                                        }
                                        ModoJuego.COMPUTADORA_VS_COMPUTADORA -> {
                                            EtiquetaSeccion(texto = "¿QUÉ SÍMBOLO COMIENZA?")
                                            Spacer(modifier = Modifier.height(10.dp))
                                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                                OpcionSimbolo(simbolo = 'X', seleccionado = cpuIniciaSimbolo == 'X', colorSimbolo = AzulX, modifier = Modifier.weight(1f), onClick = { cpuIniciaSimbolo = 'X' })
                                                OpcionSimbolo(simbolo = 'O', seleccionado = cpuIniciaSimbolo == 'O', colorSimbolo = RojoO, modifier = Modifier.weight(1f), onClick = { cpuIniciaSimbolo = 'O' })
                                            }
                                            Spacer(modifier = Modifier.height(16.dp))
                                            DivisorSutil()
                                            Spacer(modifier = Modifier.height(16.dp))
                                            SelectorDificultad(titulo = "DIFICULTAD DE 'X'", dificultadSeleccionada = dificultadXSeleccionada, onSeleccion = { dificultadXSeleccionada = it })
                                            Spacer(modifier = Modifier.height(12.dp))
                                            SelectorDificultad(titulo = "DIFICULTAD DE 'O'", dificultadSeleccionada = dificultadOSeleccionada, onSeleccion = { dificultadOSeleccionada = it })
                                        }
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Button(
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            shape = RoundedCornerShape(25.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = VerdePrincipal, contentColor = Color(0xFF06220F)),
                            onClick = {
                                when (modoSeleccionado) {
                                    ModoJuego.HUMANO_VS_COMPUTADORA -> onComenzar(simboloSeleccionado, iniciaHumano, dificultadSeleccionada)
                                    ModoJuego.COMPUTADORA_VS_COMPUTADORA -> onComenzarCpuVsCpu(cpuIniciaSimbolo, dificultadXSeleccionada, dificultadOSeleccionada)
                                    ModoJuego.HUMANO_VS_HUMANO -> onComenzarHvH(hvhIniciaSimbolo)
                                }
                            }
                        ) {
                            Text(text = if (modoSeleccionado == ModoJuego.COMPUTADORA_VS_COMPUTADORA) "Iniciar simulación" else "Jugar", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "→", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(text = "Toca ☰ para ver más opciones", color = TextoSecundario, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}


// ============================================================================
// SECCIÓN 5: BARRA LATERAL (MENÚ DESPLEGABLE)
// El menú lateral y sus botones/opciones internas.
// ============================================================================

@Composable
fun BarraLateral(
    onDesarrolladoresClick: () -> Unit = {},
    onReglasClick: () -> Unit = {},
    onEstadisticasClick: () -> Unit = {},
    modoSeleccionado: ModoJuego = ModoJuego.HUMANO_VS_COMPUTADORA,
    onHumanoVsComputadoraClick: () -> Unit = {},
    onHumanoVsHumanoClick: () -> Unit = {},
    onComputadoraVsComputadoraClick: () -> Unit = {}
) {
    var modalidadExpandida by remember { mutableStateOf(false) }

    ModalDrawerSheet(drawerContainerColor = FondoSeccion) {
        Box(modifier = Modifier.fillMaxSize()) {

            // --- Contenido principal: se desliza si no entra completo
            // (por ejemplo, en horizontal con "Modalidad de Juego" abierta) ---
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 90.dp)
            ) {
                Text(text = "Menú Principal", color = TextoPrincipal, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                DivisorSutil()
                Spacer(modifier = Modifier.height(20.dp))

                BotonMenuLateralDesplegable(texto = "Modalidad de Juego", expandido = modalidadExpandida, onClick = { modalidadExpandida = !modalidadExpandida })

                if (modalidadExpandida) {
                    Column(modifier = Modifier.fillMaxWidth().padding(start = 16.dp, top = 8.dp, bottom = 4.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        SubOpcionMenuLateral(texto = "Humano vs Computador", seleccionado = modoSeleccionado == ModoJuego.HUMANO_VS_COMPUTADORA, onClick = onHumanoVsComputadoraClick)
                        SubOpcionMenuLateral(texto = "Humano vs Humano", seleccionado = modoSeleccionado == ModoJuego.HUMANO_VS_HUMANO, onClick = onHumanoVsHumanoClick)
                        SubOpcionMenuLateral(texto = "Computador vs Computador", seleccionado = modoSeleccionado == ModoJuego.COMPUTADORA_VS_COMPUTADORA, onClick = onComputadoraVsComputadoraClick)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                BotonMenuLateral(texto = "Estadísticas Globales", onClick = onEstadisticasClick)
                Spacer(modifier = Modifier.height(12.dp))
                BotonMenuLateral(texto = "Reglas e Instrucciones", onClick = onReglasClick)
                Spacer(modifier = Modifier.height(12.dp))
                BotonMenuLateral(texto = "Desarrolladores", onClick = onDesarrolladoresClick)
                Spacer(modifier = Modifier.height(12.dp))

                // Switch de tema: oscuro (por defecto) / claro. modoOscuro es
                // una variable global, así que cambiarla aquí recompone toda la app
                Surface(
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = SuperficieSecundaria,
                    border = BorderStroke(1.dp, BordeSutil)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (modoOscuro) "Modo oscuro" else "Modo claro",
                            color = TextoPrincipal,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Switch(
                            checked = !modoOscuro,
                            onCheckedChange = {
                                modoOscuro = !it
                                Preferencias.guardarModoOscuro(modoOscuro)
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = VerdePrincipal,
                                checkedTrackColor = VerdePrincipal.copy(alpha = 0.4f)
                            )
                        )
                    }
                }
            }

            // --- Pie de página: siempre fijo y visible, sin importar el scroll ni la orientación ---
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(FondoSeccion)
                    .padding(horizontal = 20.dp, vertical = 10.dp)
            ) {
                DivisorSutil()
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Tres en Raya · Grupo 03", color = TextoSecundario, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Versión 1.0.0", color = TextoSecundario, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun BotonMenuLateral(texto: String, onClick: () -> Unit = {}) {
    Surface(modifier = Modifier.fillMaxWidth().height(50.dp).clickable { onClick() }, shape = RoundedCornerShape(12.dp), color = SuperficieSecundaria, border = BorderStroke(1.dp, BordeSutil)) {
        Row(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.width(14.dp))
            Text(text = texto, color = TextoPrincipal, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun BotonMenuLateralDesplegable(texto: String, expandido: Boolean, onClick: () -> Unit) {
    Surface(modifier = Modifier.fillMaxWidth().height(50.dp).clickable { onClick() }, shape = RoundedCornerShape(12.dp), color = SuperficieSecundaria, border = BorderStroke(1.dp, if (expandido) VerdePrincipal else BordeSutil)) {
        Row(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Spacer(modifier = Modifier.width(14.dp))
                Text(text = texto, color = TextoPrincipal, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }
            Text(text = if (expandido) "▲" else "▼", color = if (expandido) VerdePrincipal else TextoSecundario, fontSize = 12.sp)
        }
    }
}

@Composable
fun SubOpcionMenuLateral(texto: String, seleccionado: Boolean = false, onClick: () -> Unit = {}) {
    Surface(modifier = Modifier.fillMaxWidth().height(44.dp).clickable { onClick() }, shape = RoundedCornerShape(10.dp), color = if (seleccionado) VerdePrincipal.copy(alpha = 0.12f) else SuperficieOscura, border = BorderStroke(1.dp, if (seleccionado) VerdePrincipal.copy(alpha = 0.6f) else BordeSutil)) {
        Row(modifier = Modifier.fillMaxSize().padding(horizontal = 14.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = texto, color = if (seleccionado) TextoPrincipal else TextoSecundario, fontSize = 13.sp, fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Normal)
            if (seleccionado) { Surface(modifier = Modifier.size(8.dp), shape = RoundedCornerShape(50), color = VerdePrincipal) {} }
        }
    }
}


// ============================================================================
// SECCIÓN 6: PANTALLAS DE JUEGO (LAS 3 MODALIDADES)
// Humano vs Computadora, Humano vs Humano, y Computadora vs Computadora.
// ============================================================================

@Composable
fun PantallaJuego(partida: Partida, dificultad: Dificultad, onNuevaPartida: () -> Unit) {
    var versionTablero by rememberSaveable { mutableStateOf(0) }
    var mostrarResultado by rememberSaveable { mutableStateOf(false) }
    var mostrarAnalisis by rememberSaveable { mutableStateOf(false) }
    var mostrarHistorial by rememberSaveable { mutableStateOf(false) }
    var recomendacionHumano by rememberSaveable { mutableStateOf<IntArray?>(null) }
    var estadisticaRegistrada by rememberSaveable { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    BackHandler { onNuevaPartida() }

    DisenoPantallaJuego(
        titulo = "TRES EN RAYA",
        subtitulo = "Humano vs Computadora · ${nombreDificultad(dificultad)}",
        estadoPartida = { EstadoPartida(partida = partida, version = versionTablero) },
        tablero = {
            Card(modifier = Modifier.fillMaxWidth(0.95f), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = FondoSeccion), elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)) {
                Box(modifier = Modifier.padding(16.dp)) {
                    TableroGrafico(partida = partida, version = versionTablero, recomendacionHumano = recomendacionHumano, onCasillaClick = { fila, columna ->
                        recomendacionHumano = null
                        if (partida.jugarHumano(fila, columna)) {
                            versionTablero++
                            if (partida.partidaTerminada()) {
                                if (!estadisticaRegistrada) { Estadisticas.getInstancia().registrarHvC(partida.obtenerGanador(), partida.getSimboloHumano(), dificultad, partida.esEmpate()); estadisticaRegistrada = true }
                                mostrarResultado = true
                            } else {
                                scope.launch {
                                    delay(850)
                                    partida.jugarComputadora()
                                    versionTablero++
                                    if (partida.partidaTerminada()) {
                                        if (!estadisticaRegistrada) { Estadisticas.getInstancia().registrarHvC(partida.obtenerGanador(), partida.getSimboloHumano(), dificultad, partida.esEmpate()); estadisticaRegistrada = true }
                                        mostrarResultado = true
                                    }
                                }
                            }
                        }
                    })
                }
            }
        },
        controles = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Button(modifier = Modifier.fillMaxWidth().height(50.dp), shape = RoundedCornerShape(25.dp), colors = ButtonDefaults.buttonColors(containerColor = VerdePrincipal, contentColor = Color(0xFF102516)), enabled = partida.getTurnoActual() == partida.getSimboloHumano() && !partida.partidaTerminada(), onClick = { recomendacionHumano = partida.recomendarJugadaHumano() }) { Text(text = "Recomendar Jugada", fontWeight = FontWeight.Bold) }
                Spacer(modifier = Modifier.height(14.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    BotonSecundarioAccion(texto = "Análisis", habilitado = true, modifier = Modifier.weight(1f), onClick = { mostrarAnalisis = true })
                    BotonSecundarioAccion(texto = "Historial", habilitado = true, modifier = Modifier.weight(1f), onClick = { mostrarHistorial = true })
                    BotonSecundarioAccion(texto = "Nueva partida", habilitado = true, modifier = Modifier.weight(1f), onClick = onNuevaPartida)
                }
            }
        }
    )

    if (mostrarResultado) {
        val ganador = partida.obtenerGanador()
        val simboloHumano = partida.getSimboloHumano()
        val humanoGano = ganador != Tablero.vacio && ganador == simboloHumano
        AlertDialog(
            onDismissRequest = { mostrarResultado = false },
            containerColor = FondoSeccion,
            title = { Text(text = when { partida.esEmpate() -> "¡EMPATE!"; humanoGano -> "¡GANASTE!"; else -> "¡GANÓ LA COMPUTADORA!" }, color = when { partida.esEmpate() -> TextoPrincipal; humanoGano -> VerdePrincipal; else -> RojoO }, fontWeight = FontWeight.Bold) },
            text = { Text(text = when { partida.esEmpate() -> "La partida terminó sin ganador."; humanoGano -> "¡Felicidades! Ganaste la partida jugando con '$ganador'."; else -> "La computadora ganó esta vez jugando con '$ganador'." }, color = TextoSecundario) },
            confirmButton = { TextButton(onClick = { mostrarResultado = false; onNuevaPartida() }) { Text("Nueva Partida", color = VerdePrincipal, fontWeight = FontWeight.Bold) } },
            dismissButton = { TextButton(onClick = { mostrarResultado = false }) { Text("Ver Tablero", color = TextoSecundario) } }
        )
    }

    DialogoHistorial(mostrar = mostrarHistorial, historial = partida.getHistorial(), onDismiss = { mostrarHistorial = false })
    DialogoAnalisis(mostrar = mostrarAnalisis, partida = partida, onDismiss = { mostrarAnalisis = false })
}

@Composable
fun PantallaJuegoHumanoVsHumano(partida: Partida, onNuevaPartida: () -> Unit) {
    var versionTablero by rememberSaveable { mutableStateOf(0) }
    var mostrarResultado by rememberSaveable { mutableStateOf(false) }
    var mostrarHistorial by rememberSaveable { mutableStateOf(false) }
    var mostrarAnalisis by rememberSaveable { mutableStateOf(false) }
    var recomendacionHumano by rememberSaveable { mutableStateOf<IntArray?>(null) }
    var estadisticaRegistrada by rememberSaveable { mutableStateOf(false) }

    BackHandler { onNuevaPartida() }

    DisenoPantallaJuego(
        titulo = "TRES EN RAYA",
        subtitulo = "Humano vs Humano",
        estadoPartida = { EstadoPartidaHvH(partida = partida, version = versionTablero) },
        tablero = {
            Card(modifier = Modifier.fillMaxWidth(0.95f), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = FondoSeccion), elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)) {
                Box(modifier = Modifier.padding(16.dp)) {
                    TableroGrafico(partida = partida, version = versionTablero, recomendacionHumano = recomendacionHumano, onCasillaClick = { fila, columna ->
                        recomendacionHumano = null
                        if (partida.jugarTurnoHumanoVsHumano(fila, columna)) {
                            versionTablero++
                            if (partida.partidaTerminada()) {
                                if (!estadisticaRegistrada) { Estadisticas.getInstancia().registrarHvH(partida.obtenerGanador(), partida.esEmpate()); estadisticaRegistrada = true }
                                mostrarResultado = true
                            }
                        }
                    })
                }
            }
        },
        controles = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Button(modifier = Modifier.fillMaxWidth().height(50.dp), shape = RoundedCornerShape(25.dp), colors = ButtonDefaults.buttonColors(containerColor = VerdePrincipal, contentColor = Color(0xFF102516)), enabled = !partida.partidaTerminada(), onClick = { recomendacionHumano = partida.recomendarJugadaHumanoVsHumano() }) { Text(text = "Recomendar Jugada", fontWeight = FontWeight.Bold) }
                Spacer(modifier = Modifier.height(14.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    BotonSecundarioAccion(texto = "Análisis", habilitado = true, modifier = Modifier.weight(1f), onClick = { mostrarAnalisis = true })
                    BotonSecundarioAccion(texto = "Historial", habilitado = true, modifier = Modifier.weight(1f), onClick = { mostrarHistorial = true })
                    BotonSecundarioAccion(texto = "Nueva partida", habilitado = true, modifier = Modifier.weight(1f), onClick = onNuevaPartida)
                }
            }
        }
    )

    if (mostrarResultado) {
        val ganador = partida.obtenerGanador()
        AlertDialog(
            onDismissRequest = { mostrarResultado = false },
            containerColor = FondoSeccion,
            title = { Text(text = when { partida.esEmpate() -> "¡EMPATE!"; ganador != Tablero.vacio -> "¡GANÓ '$ganador'!"; else -> "PARTIDA FINALIZADA" }, color = when { partida.esEmpate() -> TextoPrincipal; ganador == 'X' -> AzulX; ganador == 'O' -> RojoO; else -> TextoPrincipal }, fontWeight = FontWeight.Bold) },
            text = { Text(text = when { partida.esEmpate() -> "La partida terminó sin ganador."; ganador != Tablero.vacio -> "El Jugador '$ganador' ha ganado la partida."; else -> "" }, color = TextoSecundario) },
            confirmButton = { TextButton(onClick = { mostrarResultado = false; onNuevaPartida() }) { Text("Nueva Partida", color = VerdePrincipal, fontWeight = FontWeight.Bold) } },
            dismissButton = { TextButton(onClick = { mostrarResultado = false }) { Text("Ver Tablero", color = TextoSecundario) } }
        )
    }

    DialogoHistorial(mostrar = mostrarHistorial, historial = partida.getHistorial(), onDismiss = { mostrarHistorial = false })
    DialogoAnalisis(mostrar = mostrarAnalisis, partida = partida, onDismiss = { mostrarAnalisis = false })
}

@Composable
fun PantallaJuegoCpuVsCpu(partida: Partida, dificultadX: Dificultad, dificultadO: Dificultad, onNuevaPartida: () -> Unit) {
    var versionTablero by rememberSaveable { mutableStateOf(0) }
    var enEjecucion by rememberSaveable { mutableStateOf(true) }
    var velocidad by rememberSaveable { mutableStateOf(VelocidadSimulacion.NORMAL) }
    var mostrarResultado by rememberSaveable { mutableStateOf(false) }
    var mostrarHistorial by rememberSaveable { mutableStateOf(false) }
    var mostrarAnalisis by rememberSaveable { mutableStateOf(false) }
    var estadisticaRegistrada by rememberSaveable { mutableStateOf(false) }

    BackHandler { onNuevaPartida() }

    LaunchedEffect(enEjecucion, versionTablero) {
        if (enEjecucion && !partida.partidaTerminada()) {
            delay(velocidad.delayMs)
            if (partida.jugarTurnoAutomatico()) {
                versionTablero++
                if (partida.partidaTerminada()) {
                    if (!estadisticaRegistrada) { Estadisticas.getInstancia().registrarCvC(partida.obtenerGanador(), partida.esEmpate(), dificultadX, dificultadO); estadisticaRegistrada = true }
                    mostrarResultado = true
                }
            }
        }
    }

    DisenoPantallaJuego(
        titulo = "TRES EN RAYA",
        subtitulo = "Computadora vs Computadora",
        estadoPartida = { EstadoPartidaCpuVsCpu(partida = partida, version = versionTablero, dificultadX = dificultadX, dificultadO = dificultadO) },
        tablero = {
            Card(modifier = Modifier.fillMaxWidth(0.95f), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = FondoSeccion), elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)) {
                Box(modifier = Modifier.padding(16.dp)) { TableroGrafico(partida = partida, version = versionTablero, recomendacionHumano = null, interactivo = false, onCasillaClick = { _, _ -> }) }
            }
        },
        controles = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(modifier = Modifier.weight(1f).height(48.dp), shape = RoundedCornerShape(24.dp), colors = ButtonDefaults.buttonColors(containerColor = VerdePrincipal, contentColor = Color(0xFF102516)), enabled = !partida.partidaTerminada(), onClick = { enEjecucion = !enEjecucion }) {
                        Text(text = if (enEjecucion) "Pausar" else "Reproducir", fontWeight = FontWeight.Bold)
                    }
                    Button(modifier = Modifier.weight(1f).height(48.dp), shape = RoundedCornerShape(24.dp), colors = ButtonDefaults.buttonColors(containerColor = BordeSutil, contentColor = TextoPrincipal), enabled = !enEjecucion && !partida.partidaTerminada(), onClick = {
                        if (partida.jugarTurnoAutomatico()) {
                            versionTablero++
                            if (partida.partidaTerminada()) {
                                if (!estadisticaRegistrada) { Estadisticas.getInstancia().registrarCvC(partida.obtenerGanador(), partida.esEmpate(), dificultadX, dificultadO); estadisticaRegistrada = true }
                                mostrarResultado = true
                            }
                        }
                    }) { Text(text = "Siguiente", fontWeight = FontWeight.Bold, fontSize = 13.sp) }
                }
                Spacer(modifier = Modifier.height(14.dp))
                SelectorVelocidad(velocidadSeleccionada = velocidad, onSeleccion = { velocidad = it })
                Spacer(modifier = Modifier.height(14.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    BotonSecundarioAccion(texto = "Análisis", habilitado = true, modifier = Modifier.weight(1f), onClick = { mostrarAnalisis = true })
                    BotonSecundarioAccion(texto = "Historial", habilitado = true, modifier = Modifier.weight(1f), onClick = { mostrarHistorial = true })
                    BotonSecundarioAccion(texto = "Nueva partida", habilitado = true, modifier = Modifier.weight(1f), onClick = onNuevaPartida)
                }
            }
        }
    )

    if (mostrarResultado) {
        val ganador = partida.obtenerGanador()
        AlertDialog(
            onDismissRequest = { mostrarResultado = false },
            containerColor = FondoSeccion,
            title = { Text(text = when { partida.esEmpate() -> "¡EMPATE!"; ganador != Tablero.vacio -> "¡GANÓ '$ganador'!"; else -> "PARTIDA FINALIZADA" }, color = when { partida.esEmpate() -> TextoPrincipal; ganador == 'X' -> AzulX; ganador == 'O' -> RojoO; else -> TextoPrincipal }, fontWeight = FontWeight.Bold) },
            text = { Text(text = when { partida.esEmpate() -> "La simulación terminó sin ganador."; ganador != Tablero.vacio -> "La IA que jugaba con '$ganador' ganó la partida."; else -> "" }, color = TextoSecundario) },
            confirmButton = { TextButton(onClick = { mostrarResultado = false; onNuevaPartida() }) { Text("Nueva Partida", color = VerdePrincipal, fontWeight = FontWeight.Bold) } },
            dismissButton = { TextButton(onClick = { mostrarResultado = false }) { Text("Ver Tablero", color = TextoSecundario) } }
        )
    }

    DialogoHistorial(mostrar = mostrarHistorial, historial = partida.getHistorial(), onDismiss = { mostrarHistorial = false })
    DialogoAnalisis(mostrar = mostrarAnalisis, partida = partida, onDismiss = { mostrarAnalisis = false })
}


// ============================================================================
// SECCIÓN 7: DISEÑO COMPARTIDO Y ESTADO DE LA PARTIDA
// Layout adaptativo (vertical/horizontal) y las tarjetas de estado/tablero que usan las 3 pantallas de juego.
// ============================================================================

@Composable
fun DisenoPantallaJuego(
    titulo: String,
    subtitulo: String,
    estadoPartida: @Composable () -> Unit,
    tablero: @Composable () -> Unit,
    controles: @Composable () -> Unit
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    FondoAplicacion {
        if (isLandscape) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Mitad Izquierda: Controles y Estado
                Column(
                    modifier = Modifier.weight(1f).fillMaxHeight().verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = titulo, color = TextoPrincipal, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        EtiquetaPildora(texto = subtitulo, color = VerdePrincipal)
                    }
                    estadoPartida()
                    controles()
                }
                // Mitad Derecha: Tablero
                Box(modifier = Modifier.weight(1f).fillMaxHeight(), contentAlignment = Alignment.Center) {
                    tablero()
                }
            }
        } else {
            // Diseño para orientación vertical
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .verticalScroll(rememberScrollState())
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = titulo, color = TextoPrincipal, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    EtiquetaPildora(texto = subtitulo, color = VerdePrincipal)
                }
                Spacer(modifier = Modifier.height(22.dp))
                estadoPartida()
                Spacer(modifier = Modifier.height(22.dp))
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) { tablero() }
                Spacer(modifier = Modifier.height(26.dp))
                controles()
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun EstadoPartida(partida: Partida, version: Int) {
    key(version) {
        val simboloHumano = partida.getSimboloHumano()
        val simboloComputadora = if (simboloHumano == 'X') 'O' else 'X'
        val turno = partida.getTurnoActual()
        val terminada = partida.partidaTerminada()
        Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = FondoSeccion), elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)) {
            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 18.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    ChipJugador(etiqueta = "Tú", simbolo = simboloHumano, color = if (simboloHumano == 'X') AzulX else RojoO, activo = turno == simboloHumano && !terminada)
                    Text(text = "VS", color = TextoSecundario, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    ChipJugador(etiqueta = "Computadora", simbolo = simboloComputadora, color = if (simboloComputadora == 'X') AzulX else RojoO, activo = turno == simboloComputadora && !terminada)
                }
                Spacer(modifier = Modifier.height(16.dp))
                DivisorSutil()
                Spacer(modifier = Modifier.height(14.dp))
                when {
                    partida.esEmpate() -> Text(text = "¡Empate!", color = TextoPrincipal, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    partida.obtenerGanador() != Tablero.vacio -> {
                        val ganador = partida.obtenerGanador()
                        Text(text = "¡Ganó '$ganador'!", color = if (ganador == 'X') AzulX else RojoO, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                    else -> Text(text = if (turno == simboloHumano) "Tu turno" else "Turno de la computadora", color = if (turno == 'X') AzulX else RojoO, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun EstadoPartidaHvH(partida: Partida, version: Int) {
    key(version) {
        val turno = partida.getTurnoActual()
        val terminada = partida.partidaTerminada()
        Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = FondoSeccion), elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)) {
            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 18.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    ChipJugador(etiqueta = "Jugador 1", simbolo = 'X', color = AzulX, activo = turno == 'X' && !terminada)
                    Text(text = "VS", color = TextoSecundario, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    ChipJugador(etiqueta = "Jugador 2", simbolo = 'O', color = RojoO, activo = turno == 'O' && !terminada)
                }
                Spacer(modifier = Modifier.height(16.dp))
                DivisorSutil()
                Spacer(modifier = Modifier.height(14.dp))
                when {
                    partida.esEmpate() -> Text(text = "¡Empate!", color = TextoPrincipal, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    partida.obtenerGanador() != Tablero.vacio -> {
                        val ganador = partida.obtenerGanador()
                        Text(text = "¡Ganó '$ganador'!", color = if (ganador == 'X') AzulX else RojoO, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                    else -> Text(text = "Turno de: '$turno'", color = if (turno == 'X') AzulX else RojoO, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun EstadoPartidaCpuVsCpu(
    partida: Partida,
    version: Int,
    dificultadX: Dificultad,
    dificultadO: Dificultad
) {
    key(version) {
        val turno = partida.getTurnoActual()
        val terminada = partida.partidaTerminada()
        Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = FondoSeccion), elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)) {
            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 18.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    ChipJugador(etiqueta = "IA · ${nombreDificultad(dificultadX)}", simbolo = 'X', color = AzulX, activo = turno == 'X' && !terminada)
                    Text(text = "VS", color = TextoSecundario, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    ChipJugador(etiqueta = "IA · ${nombreDificultad(dificultadO)}", simbolo = 'O', color = RojoO, activo = turno == 'O' && !terminada)
                }
                Spacer(modifier = Modifier.height(16.dp))
                DivisorSutil()
                Spacer(modifier = Modifier.height(14.dp))
                when {
                    partida.esEmpate() -> Text(text = "¡Empate!", color = TextoPrincipal, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    partida.obtenerGanador() != Tablero.vacio -> {
                        val ganador = partida.obtenerGanador()
                        Text(text = "¡Ganó '$ganador'!", color = if (ganador == 'X') AzulX else RojoO, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                    else -> Text(text = "Jugando: '$turno'", color = if (turno == 'X') AzulX else RojoO, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun ChipJugador(etiqueta: String, simbolo: Char, color: Color, activo: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Surface(modifier = Modifier.size(32.dp), shape = RoundedCornerShape(10.dp), color = if (activo) color.copy(alpha = 0.2f) else SuperficieSecundaria, border = if (activo) BorderStroke(1.5.dp, color) else BorderStroke(1.dp, BordeSutil)) {
            Box(contentAlignment = Alignment.Center) { Text(text = simbolo.toString(), color = color, fontSize = 14.sp, fontWeight = FontWeight.Bold) }
        }
        Text(text = etiqueta, color = if (activo) TextoPrincipal else TextoSecundario, fontSize = 13.sp, fontWeight = if (activo) FontWeight.Bold else FontWeight.Normal)
    }
}

@Composable
fun TableroGrafico(partida: Partida, version: Int, recomendacionHumano: IntArray?, onCasillaClick: (Int, Int) -> Unit, interactivo: Boolean = true) {
    key(version, recomendacionHumano) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .background(BordeCasilla),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            for (fila in 0 until Tablero.tamanio) {
                Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                    for (columna in 0 until Tablero.tamanio) {
                        val contenido = partida.getTablero().obtenerCasilla(fila, columna)
                        val habilitada = contenido == Tablero.vacio && !partida.partidaTerminada() && interactivo
                        val esRecomendada = recomendacionHumano != null && recomendacionHumano[0] == fila && recomendacionHumano[1] == columna
                        Surface(modifier = Modifier.weight(1f).aspectRatio(1f).clickable(enabled = habilitada) { onCasillaClick(fila, columna) }, shape = RoundedCornerShape(0.dp), color = if (esRecomendada) CasillaResaltada else if (habilitada) CasillaLibre else CasillaDeshabilitada) {
                            Box(contentAlignment = Alignment.Center) {
                                if (contenido != Tablero.vacio) Text(text = contenido.toString(), color = if (contenido == 'X') AzulX else RojoO, fontSize = 58.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}


// ============================================================================
// SECCIÓN 8: DIÁLOGOS DE LA PARTIDA (HISTORIAL Y ANÁLISIS)
// Ventanas emergentes para ver el historial de jugadas y el análisis de Minimax.
// ============================================================================

@Composable
fun DialogoHistorial(mostrar: Boolean, historial: List<HistorialJugada>, onDismiss: () -> Unit) {
    if (!mostrar) return
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Card(
            modifier = Modifier
                .fillMaxWidth(if (isLandscape) 0.85f else 0.95f)
                .fillMaxHeight(if (isLandscape) 0.9f else 0.85f)
                .padding(8.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = FondoAplicacionColor),
            border = BorderStroke(1.dp, BordeSutil)
        ) {
            Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
                Column {
                    Text("Historial de Jugadas", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = TextoPrincipal)
                    if (historial.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("${historial.size} movimiento(s) registrados", fontSize = 13.sp, color = TextoSecundario)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                    if (historial.isEmpty()) {
                        Text("Aún no se han realizado jugadas.", color = TextoSecundario, modifier = Modifier.align(Alignment.Center))
                    } else {
                        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()), horizontalAlignment = Alignment.CenterHorizontally) {
                            historial.forEachIndexed { indice, jugada ->
                                TarjetaMovimiento(numero = indice + 1, jugada = jugada)
                                if (indice != historial.lastIndex) Spacer(modifier = Modifier.height(12.dp))
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth().height(48.dp), colors = ButtonDefaults.buttonColors(containerColor = VerdePrincipal)) {
                    Text("Cerrar", color = Color(0xFF102516), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun DialogoAnalisis(mostrar: Boolean, partida: Partida, onDismiss: () -> Unit) {
    if (!mostrar) return
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val analisis = partida.getAnalisisUltimaJugada()

    // Detectamos a qué símbolo representa el oponente dependiendo de la modalidad
    val simboloOponente = if (partida.isModoHumanoVsHumano() || partida.isModoComputadoraVsComputadora()) {
        val jugadorUltimaJugada = partida.getHistorial().lastOrNull()?.jugador ?: 'X'
        if (jugadorUltimaJugada == 'X') 'O' else 'X'
    } else {
        partida.getSimboloHumano()
    }

    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Card(
            modifier = Modifier
                .fillMaxWidth(if (isLandscape) 0.85f else 0.95f)
                .fillMaxHeight(if (isLandscape) 0.9f else 0.85f)
                .padding(8.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = FondoAplicacionColor),
            border = BorderStroke(1.dp, BordeSutil)
        ) {
            Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
                Column {
                    Text("Análisis de la Jugada", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = TextoPrincipal)
                    if (analisis.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("${analisis.size} jugada(s) posible(s) evaluadas", fontSize = 13.sp, color = TextoSecundario)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                    if (analisis.isEmpty()) {
                        Text("Aún no hay un análisis disponible.", color = TextoSecundario, modifier = Modifier.align(Alignment.Center))
                    } else {
                        var indicesExpandidos by remember { mutableStateOf(analisis.mapIndexedNotNull { i, j -> if (j.isElegida()) i else null }.toSet()) }
                        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Toca una jugada para ver los detalles", color = TextoSecundario, fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(16.dp))
                            analisis.forEachIndexed { indice, jugada ->
                                val expandido = indice in indicesExpandidos
                                val derrotaPosible = jugada.getUtilidadMinima() == Integer.MIN_VALUE
                                val elegida = jugada.isElegida()

                                Card(modifier = Modifier.fillMaxWidth().clickable { indicesExpandidos = if (expandido) indicesExpandidos - indice else indicesExpandidos + indice }, shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = if (elegida) VerdePrincipal.copy(alpha = 0.08f) else SuperficieSecundaria), border = BorderStroke(1.dp, if (elegida) VerdePrincipal.copy(alpha = 0.6f) else BordeSutil)) {
                                    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                                        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                                Text(text = "Jugada ${indice + 1}", color = TextoPrincipal, fontWeight = FontWeight.Bold)
                                                if (elegida) EtiquetaPildora(texto = "✓ Elegida", color = VerdePrincipal)
                                            }
                                            Text(text = if (expandido) "▲" else "▼", color = TextoSecundario, fontSize = 14.sp)
                                        }
                                        Spacer(modifier = Modifier.height(12.dp))
                                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) { MiniTablero(tablero = jugada.getTablero()) }
                                        Spacer(modifier = Modifier.height(12.dp))
                                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                            if (derrotaPosible) EtiquetaPildora(texto = "⚠ Derrota posible", color = Color(0xFFFACC15)) else EtiquetaPildora(texto = "Utilidad mínima: ${jugada.getUtilidadMinima()}", color = TextoSecundario)
                                        }
                                        if (expandido) {
                                            Spacer(modifier = Modifier.height(16.dp))
                                            DivisorSutil()
                                            Spacer(modifier = Modifier.height(16.dp))
                                            if (jugada.getRespuestas().isNotEmpty()) {
                                                EtiquetaSeccion(texto = "RESPUESTAS DEL OPONENTE")
                                                Spacer(modifier = Modifier.height(12.dp))
                                                jugada.getRespuestas().forEachIndexed { indiceRespuesta, respuesta ->
                                                    val esVictoriaOponente = respuesta.getTablero().isWinner(simboloOponente)
                                                    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = SuperficieOscura), border = BorderStroke(1.dp, if (esVictoriaOponente) RojoO.copy(alpha = 0.5f) else BordeSutil)) {
                                                        Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
                                                            Text(text = "Respuesta ${indiceRespuesta + 1}", color = TextoPrincipal, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                                                            Spacer(modifier = Modifier.height(8.dp))
                                                            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) { MiniTablero(tablero = respuesta.getTablero()) }
                                                            Spacer(modifier = Modifier.height(8.dp))
                                                            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) { EtiquetaPildora(texto = "Utilidad: ${respuesta.getUtilidad()}", color = TextoSecundario) }
                                                            if (esVictoriaOponente) {
                                                                Spacer(modifier = Modifier.height(8.dp))
                                                                Text(text = "⚠ Esta jugada le da la victoria a '$simboloOponente'", color = RojoO, fontWeight = FontWeight.Bold, fontSize = 12.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                                                            }
                                                        }
                                                    }
                                                    if (indiceRespuesta != jugada.getRespuestas().lastIndex) Spacer(modifier = Modifier.height(10.dp))
                                                }
                                            } else {
                                                Text(text = "No existen respuestas posteriores.", color = TextoSecundario, fontSize = 13.sp)
                                            }
                                        }
                                    }
                                }
                                if (indice != analisis.lastIndex) Spacer(modifier = Modifier.height(14.dp))
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth().height(48.dp), colors = ButtonDefaults.buttonColors(containerColor = VerdePrincipal)) {
                    Text("Cerrar", color = Color(0xFF102516), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun TarjetaMovimiento(numero: Int, jugada: HistorialJugada) {
    val colorJugador = if (jugada.jugador == 'X') AzulX else RojoO
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = SuperficieSecundaria), border = BorderStroke(1.dp, colorJugador.copy(alpha = 0.35f))) {
        Column(modifier = Modifier.fillMaxWidth().padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Surface(modifier = Modifier.size(28.dp), shape = RoundedCornerShape(50), color = colorJugador.copy(alpha = 0.15f), border = BorderStroke(1.dp, colorJugador)) {
                    Box(contentAlignment = Alignment.Center) { Text(text = numero.toString(), color = colorJugador, fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                }
                Column {
                    Text(text = "Jugador ${jugada.jugador}", color = colorJugador, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Fila ${jugada.fila} · Columna ${jugada.columna}", color = TextoSecundario, fontSize = 12.sp)
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) { MiniTablero(tablero = jugada.tableroResultante) }
        }
    }
}

@Composable
fun MiniTablero(tablero: Tablero) {
    Column(modifier = Modifier.size(150.dp), verticalArrangement = Arrangement.spacedBy(0.dp)) {
        for (fila in 0 until Tablero.tamanio) {
            Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.spacedBy(0.dp)) {
                for (columna in 0 until Tablero.tamanio) {
                    val contenido = tablero.obtenerCasilla(fila, columna)
                    Surface(modifier = Modifier.weight(1f).aspectRatio(1f).border(width = 1.dp, color = BordeCasilla, shape = RoundedCornerShape(0.dp)), shape = RoundedCornerShape(0.dp), color = CasillaLibre) {
                        Box(contentAlignment = Alignment.Center) {
                            if (contenido != Tablero.vacio) Text(text = contenido.toString(), color = if (contenido == 'X') AzulX else RojoO, fontSize = 25.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}


// ============================================================================
// SECCIÓN 9: PANTALLA DE ESTADÍSTICAS (ANÁLISIS DE DATOS)
// Resumen global y tablas de rendimiento por modalidad y dificultad.
// ============================================================================

@Composable
fun PantallaEstadisticas(onVolver: () -> Unit) {
    var refrescar by remember { mutableStateOf(0) }
    var mostrarConfirmacionReinicio by remember { mutableStateOf(false) }

    FondoAplicacion {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
                .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 40.dp)
        ) {
            // ENCABEZADO
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    modifier = Modifier.size(40.dp).clickable { onVolver() },
                    shape = RoundedCornerShape(12.dp),
                    color = SuperficieSecundaria,
                    border = BorderStroke(1.dp, BordeSutil)
                ) {
                    Box(contentAlignment = Alignment.Center) { Text(text = "<", color = TextoPrincipal, fontSize = 18.sp, fontWeight = FontWeight.Bold) }
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(text = "Estadísticas Globales", color = TextoPrincipal, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Rendimiento global y por modalidades", color = TextoSecundario, fontSize = 13.sp)
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            key(refrescar) {
                val stats = Estadisticas.getInstancia()

                // 1. DASHBOARD GLOBAL
                TarjetaResumenGlobal(stats)

                Spacer(modifier = Modifier.height(24.dp))

                // 2. HUMANO VS COMPUTADORA (TABLA)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = FondoSeccion),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth().padding(22.dp)) {
                        EtiquetaSeccion(texto = "HUMANO VS COMPUTADORA")
                        Spacer(modifier = Modifier.height(16.dp))

                        EncabezadoTabla("Dificultad", "Jug", "Gan", "Der", "Emp")
                        DivisorSutil()
                        FilaTablaEstadistica("Fácil", stats.hvcFacil.jugadas, stats.hvcFacil.victoriasHumano, stats.hvcFacil.victoriasIA, stats.hvcFacil.empates)
                        DivisorSutil()
                        FilaTablaEstadistica("Medio", stats.hvcMedio.jugadas, stats.hvcMedio.victoriasHumano, stats.hvcMedio.victoriasIA, stats.hvcMedio.empates)
                        DivisorSutil()
                        FilaTablaEstadistica("Difícil", stats.hvcDificil.jugadas, stats.hvcDificil.victoriasHumano, stats.hvcDificil.victoriasIA, stats.hvcDificil.empates)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 3. HUMANO VS HUMANO
                TarjetaEstadisticasHvH(stats = stats.hvh)

                Spacer(modifier = Modifier.height(16.dp))

                // 4. COMPUTADORA VS COMPUTADORA (NUEVO DISEÑO CON BARRAS Y DIFICULTADES)
                TarjetaEstadisticasCvC(stats = stats)
            }

            Spacer(modifier = Modifier.height(28.dp))

            // --- Botón para restablecer las estadísticas guardadas ---
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { mostrarConfirmacionReinicio = true },
                shape = RoundedCornerShape(14.dp),
                color = SuperficieSecundaria,
                border = BorderStroke(1.dp, RojoO.copy(alpha = 0.4f))
            ) {
                Box(modifier = Modifier.fillMaxWidth().padding(vertical = 14.dp), contentAlignment = Alignment.Center) {
                    Text(text = "Restablecer estadísticas", color = RojoO, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    if (mostrarConfirmacionReinicio) {
        AlertDialog(
            onDismissRequest = { mostrarConfirmacionReinicio = false },
            containerColor = FondoSeccion,
            title = { Text(text = "¿Restablecer estadísticas?", color = TextoPrincipal, fontWeight = FontWeight.Bold) },
            text = { Text(text = "Esto borrará permanentemente todas las partidas registradas en todas las modalidades. Esta acción no se puede deshacer.", color = TextoSecundario) },
            confirmButton = {
                TextButton(onClick = {
                    Estadisticas.getInstancia().reiniciar()
                    refrescar++
                    mostrarConfirmacionReinicio = false
                }) { Text("Restablecer", color = RojoO, fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                TextButton(onClick = { mostrarConfirmacionReinicio = false }) { Text("Cancelar", color = TextoSecundario) }
            }
        )
    }
}

@Composable
fun TarjetaResumenGlobal(stats: Estadisticas) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SuperficieOscura),
        border = BorderStroke(1.dp, BordeSutil)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(22.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text(text = "Rendimiento Global", color = TextoPrincipal, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Distribución de victorias por símbolo", color = TextoSecundario, fontSize = 12.sp)
                }
                Surface(shape = RoundedCornerShape(10.dp), color = VerdePrincipal.copy(alpha = 0.15f)) {
                    Text(text = "${stats.totalPartidas} Partidas", color = VerdePrincipal, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp))
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            // BARRA PROPORCIONAL
            BarraDistribucionSimbolos(total = stats.totalPartidas, vicX = stats.victoriasGlobalesX, vicO = stats.victoriasGlobalesO, empates = stats.empatesGlobales)

            Spacer(modifier = Modifier.height(16.dp))

            // LEYENDA
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                LeyendaSimbolo(simbolo = "Símbolo 'X'", cantidad = stats.victoriasGlobalesX, color = AzulX)
                LeyendaSimbolo(simbolo = "Empates", cantidad = stats.empatesGlobales, color = TextoSecundario)
                LeyendaSimbolo(simbolo = "Símbolo 'O'", cantidad = stats.victoriasGlobalesO, color = RojoO)
            }
        }
    }
}

@Composable
fun TarjetaEstadisticasHvH(stats: Estadisticas.StatsGeneral) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = FondoSeccion),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(22.dp)) {
            EtiquetaSeccion(texto = "HUMANO VS HUMANO")
            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                ItemEstadistica(valor = stats.jugadas, etiqueta = "Partidas", color = TextoPrincipal, modifier = Modifier.weight(1f))
                ItemEstadistica(valor = stats.empates, etiqueta = "Empates", color = TextoSecundario, modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(20.dp))
            DivisorSutil()
            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "Rendimiento por Símbolo", color = TextoPrincipal, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(12.dp))
            BarraDistribucionSimbolos(total = stats.jugadas, vicX = stats.victoriasX, vicO = stats.victoriasO, empates = stats.empates)
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                LeyendaSimbolo(simbolo = "Victorias 'X'", cantidad = stats.victoriasX, color = AzulX)
                LeyendaSimbolo(simbolo = "Victorias 'O'", cantidad = stats.victoriasO, color = RojoO)
            }
        }
    }
}

@Composable
fun TarjetaEstadisticasCvC(stats: Estadisticas) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = FondoSeccion),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(22.dp)) {
            EtiquetaSeccion(texto = "COMPUTADORA VS COMPUTADORA (IA VS IA)")
            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                ItemEstadistica(valor = stats.cvc.jugadas, etiqueta = "Simulaciones", color = TextoPrincipal, modifier = Modifier.weight(1f))
                ItemEstadistica(valor = stats.cvc.empates, etiqueta = "Empates", color = TextoSecundario, modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(20.dp))
            DivisorSutil()
            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "Rendimiento por Símbolo", color = TextoPrincipal, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(12.dp))
            BarraDistribucionSimbolos(total = stats.cvc.jugadas, vicX = stats.cvc.victoriasX, vicO = stats.cvc.victoriasO, empates = stats.cvc.empates)
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                LeyendaSimbolo(simbolo = "IA Gana con 'X'", cantidad = stats.cvc.victoriasX, color = AzulX)
                LeyendaSimbolo(simbolo = "IA Gana con 'O'", cantidad = stats.cvc.victoriasO, color = RojoO)
            }

            Spacer(modifier = Modifier.height(20.dp))
            DivisorSutil()
            Spacer(modifier = Modifier.height(20.dp))

            // El desglose de victorias por dificultad de IA
            Text(text = "Victorias según dificultad IA", color = TextoPrincipal, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                ItemEstadistica(valor = stats.cvcVictoriasFacil, etiqueta = "IA Fácil", color = VerdePrincipal, modifier = Modifier.weight(1f))
                ItemEstadistica(valor = stats.cvcVictoriasMedio, etiqueta = "IA Media", color = AzulX, modifier = Modifier.weight(1f))
                ItemEstadistica(valor = stats.cvcVictoriasDificil, etiqueta = "IA Difícil", color = RojoO, modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun BarraDistribucionSimbolos(total: Int, vicX: Int, vicO: Int, empates: Int) {
    val pesoX = if (total > 0) vicX.toFloat() / total else 0f
    val pesoO = if (total > 0) vicO.toFloat() / total else 0f
    val pesoEmp = if (total > 0) empates.toFloat() / total else 1f // Si no hay partidas, la barra se ve gris

    Row(modifier = Modifier.fillMaxWidth().height(14.dp).clip(RoundedCornerShape(50))) {
        if (pesoX > 0) Box(modifier = Modifier.weight(pesoX).fillMaxSize().background(AzulX))
        if (pesoEmp > 0) Box(modifier = Modifier.weight(pesoEmp).fillMaxSize().background(BordeSutil))
        if (pesoO > 0) Box(modifier = Modifier.weight(pesoO).fillMaxSize().background(RojoO))
    }
}

@Composable
fun LeyendaSimbolo(simbolo: String, cantidad: Int, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(color))
        Column {
            Text(text = cantidad.toString(), color = TextoPrincipal, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text(text = simbolo, color = TextoSecundario, fontSize = 11.sp)
        }
    }
}

@Composable
fun EncabezadoTabla(col1: String, col2: String, col3: String, col4: String, col5: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = col1, color = TextoSecundario, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1.5f))
        Text(text = col2, color = TextoSecundario, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(0.6f), textAlign = TextAlign.Center)
        Text(text = col3, color = TextoSecundario, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(0.6f), textAlign = TextAlign.Center)
        Text(text = col4, color = TextoSecundario, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(0.6f), textAlign = TextAlign.Center)
        Text(text = col5, color = TextoSecundario, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(0.6f), textAlign = TextAlign.Center)
    }
}

@Composable
fun FilaTablaEstadistica(etiqueta: String, jugadas: Int, ganadas: Int, perdidas: Int, empates: Int) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(text = etiqueta, color = TextoPrincipal, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1.5f))
        Text(text = jugadas.toString(), color = TextoPrincipal, fontSize = 14.sp, modifier = Modifier.weight(0.6f), textAlign = TextAlign.Center)
        Text(text = ganadas.toString(), color = VerdePrincipal, fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(0.6f), textAlign = TextAlign.Center)
        Text(text = perdidas.toString(), color = RojoO, fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(0.6f), textAlign = TextAlign.Center)
        Text(text = empates.toString(), color = TextoSecundario, fontSize = 14.sp, modifier = Modifier.weight(0.6f), textAlign = TextAlign.Center)
    }
}

@Composable
fun ItemEstadistica(valor: Int, etiqueta: String, color: Color, modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = valor.toString(), color = color, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = etiqueta, color = TextoSecundario, fontSize = 11.sp, textAlign = TextAlign.Center)
    }
}


// ============================================================================
// SECCIÓN 10: PANTALLA DE DESARROLLADORES
// Perfil de cada integrante del equipo.
// ============================================================================

@Composable
fun PantallaDesarrolladores(onVolver: () -> Unit) {
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    FondoAplicacion {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                // Restauramos el padding a 40.dp para que coincida con las demás pantallas y no choque con el reloj
                .statusBarsPadding()
                .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 40.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Surface(modifier = Modifier.size(40.dp).clickable { onVolver() }, shape = RoundedCornerShape(12.dp), color = SuperficieSecundaria, border = BorderStroke(1.dp, BordeSutil)) {
                    Box(contentAlignment = Alignment.Center) { Text(text = "<", color = TextoPrincipal, fontSize = 18.sp, fontWeight = FontWeight.Bold) }
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(text = "Desarrolladores", color = TextoPrincipal, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Conoce al equipo detrás de este proyecto", color = TextoSecundario, fontSize = 13.sp)
                }
            }

            if (isLandscape) {
                // DISEÑO HORIZONTAL: 3 tarjetas una al lado de la otra
                Spacer(modifier = Modifier.height(24.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Box(modifier = Modifier.weight(1f)) {
                        TarjetaDesarrollador(nombre = "Steven Guzman", rol = "Desarrollador Android", colorAvatar = AzulX, correo = "stguzman@espol.edu.ec", universidad = "Escuela Superior Politécnica del Litoral (ESPOL)", carrera = "Ingeniería en Computación", telefono = "+593 0978936743", ciudad = "Guayaquil", fotoResId = R.drawable.foto_steven)
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        TarjetaDesarrollador(nombre = "Carlos Garcia", rol = "Diseño de interfaz", colorAvatar = RojoO, correo = "cargarci@espol.edu.ec", universidad = "Escuela Superior Politécnica del Litoral (ESPOL)", carrera = "Ingeniería en Computación", telefono = "+593 0963735381", ciudad = "Guayaquil", fotoResId = R.drawable.foto_carlos)
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        TarjetaDesarrollador(nombre = "David Jalon", rol = "Lógica del juego", colorAvatar = VerdePrincipal, correo = "dejalon@espol.edu.ec", universidad = "Escuela Superior Politécnica del Litoral (ESPOL)", carrera = "Ingeniería en Computación", telefono = "+593 0984252637", ciudad = "Guayaquil", fotoResId = R.drawable.foto_david)
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            } else {
                // DISEÑO VERTICAL: Original
                Spacer(modifier = Modifier.height(32.dp))
                TarjetaDesarrollador(nombre = "Steven Guzman", rol = "Desarrollador Android", colorAvatar = AzulX, correo = "stguzman@espol.edu.ec", universidad = "Escuela Superior Politécnica del Litoral (ESPOL)", carrera = "Ingeniería en Computación", telefono = "+593 0978936743", ciudad = "Guayaquil", fotoResId = R.drawable.foto_steven)
                Spacer(modifier = Modifier.height(16.dp))
                TarjetaDesarrollador(nombre = "Carlos Garcia", rol = "Diseño de interfaz", colorAvatar = RojoO, correo = "cargarci@espol.edu.ec", universidad = "Escuela Superior Politécnica del Litoral (ESPOL)", carrera = "Ingeniería en Computación", telefono = "+593 0963735381", ciudad = "Guayaquil", fotoResId = R.drawable.foto_carlos)
                Spacer(modifier = Modifier.height(16.dp))
                TarjetaDesarrollador(nombre = "David Jalon", rol = "Lógica del juego", colorAvatar = VerdePrincipal, correo = "dejalon@espol.edu.ec", universidad = "Escuela Superior Politécnica del Litoral (ESPOL)", carrera = "Ingeniería en Computación", telefono = "+593 0984252637", ciudad = "Guayaquil", fotoResId = R.drawable.foto_david)
                Spacer(modifier = Modifier.height(28.dp))
            }

            Text(text = "Grupo 03 · Estructura de Datos", color = TextoSecundario, fontSize = 12.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
fun TarjetaDesarrollador(nombre: String, rol: String, colorAvatar: Color, correo: String, universidad: String, carrera: String, telefono: String, ciudad: String, fotoResId: Int? = null) {
    var mostrarPerfil by remember { mutableStateOf(false) }
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    Card(modifier = Modifier.fillMaxWidth().clickable { mostrarPerfil = true }, shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = FondoSeccion), elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)) {
        if (isLandscape) {
            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                if (fotoResId != null) { Image(painter = painterResource(id = fotoResId), contentDescription = "Foto de $nombre", contentScale = ContentScale.Crop, modifier = Modifier.size(56.dp).clip(CircleShape).border(1.5.dp, colorAvatar, CircleShape)) }
                else { AvatarIniciales(nombre = nombre, color = colorAvatar) }
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = nombre, color = TextoPrincipal, fontSize = 15.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = rol, color = colorAvatar, fontSize = 12.sp, fontWeight = FontWeight.Medium, textAlign = TextAlign.Center)
            }
        } else {
            Row(modifier = Modifier.fillMaxWidth().padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                if (fotoResId != null) { Image(painter = painterResource(id = fotoResId), contentDescription = "Foto de $nombre", contentScale = ContentScale.Crop, modifier = Modifier.size(56.dp).clip(CircleShape).border(1.5.dp, colorAvatar, CircleShape)) }
                else { AvatarIniciales(nombre = nombre, color = colorAvatar) }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = nombre, color = TextoPrincipal, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = rol, color = colorAvatar, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
    if (mostrarPerfil) {
        DialogoPerfilDesarrollador(nombre = nombre, rol = rol, colorAvatar = colorAvatar, correo = correo, universidad = universidad, carrera = carrera, telefono = telefono, ciudad = ciudad, fotoResId = fotoResId, onCerrar = { mostrarPerfil = false })
    }
}

@Composable
fun DialogoPerfilDesarrollador(nombre: String, rol: String, colorAvatar: Color, correo: String, universidad: String, carrera: String, telefono: String, ciudad: String, fotoResId: Int?, onCerrar: () -> Unit) {
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
    val maxDialogHeight = LocalConfiguration.current.screenHeightDp.dp * if (isLandscape) 0.95f else 0.85f

    Dialog(
        onDismissRequest = onCerrar,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(if (isLandscape) 0.65f else 0.9f)
                .padding(8.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = FondoSeccion),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            border = BorderStroke(1.dp, colorAvatar.copy(alpha = 0.35f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = maxDialogHeight)
                    .verticalScroll(rememberScrollState())
                    .padding(if (isLandscape) 16.dp else 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = nombre, color = TextoPrincipal, fontSize = 20.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(8.dp))
                EtiquetaPildora(texto = rol, color = colorAvatar)
                Spacer(modifier = Modifier.height(20.dp))
                if (fotoResId != null) { Image(painter = painterResource(id = fotoResId), contentDescription = "Foto de $nombre", contentScale = ContentScale.Crop, modifier = Modifier.size(96.dp).clip(CircleShape).border(2.dp, colorAvatar, CircleShape)) }
                else { AvatarIniciales(nombre = nombre, color = colorAvatar, tamano = 96.dp) }
                Spacer(modifier = Modifier.height(16.dp))
                Spacer(modifier = Modifier.height(22.dp))
                Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    FilaDatoPerfil(etiqueta = "Correo", valor = correo, colorAcento = colorAvatar)
                    FilaDatoPerfil(etiqueta = "Teléfono", valor = telefono, colorAcento = colorAvatar)
                    // Fila de Edad eliminada exitosamente de aquí
                    FilaDatoPerfil(etiqueta = "Ciudad", valor = ciudad, colorAcento = colorAvatar)
                    FilaDatoPerfil(etiqueta = "Universidad", valor = universidad, colorAcento = colorAvatar)
                    FilaDatoPerfil(etiqueta = "Carrera", valor = carrera, colorAcento = colorAvatar)
                }
                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = onCerrar, modifier = Modifier.fillMaxWidth().height(48.dp), shape = RoundedCornerShape(24.dp), colors = ButtonDefaults.buttonColors(containerColor = colorAvatar)) { Text(text = "Cerrar", color = FondoAplicacionColor, fontWeight = FontWeight.Bold) }
            }
        }
    }
}

@Composable
fun FilaDatoPerfil(etiqueta: String, valor: String, colorAcento: Color) {
    Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), color = SuperficieSecundaria, border = BorderStroke(1.dp, colorAcento.copy(alpha = 0.35f))) {
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 12.dp), verticalAlignment = Alignment.Top) {
            Text(text = etiqueta, color = TextoSecundario, fontSize = 11.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = valor, color = TextoPrincipal, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.End, modifier = Modifier.weight(0.1f))
        }
    }
}


// ============================================================================
// SECCIÓN 11: PANTALLA DE REGLAS E INSTRUCCIONES
// Explicación del juego, las modalidades y consejos.
// ============================================================================

@Composable
fun PantallaReglas(onVolver: () -> Unit) {
    FondoAplicacion {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
                .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 40.dp)
        ) {
            // ENCABEZADO
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Surface(modifier = Modifier.size(40.dp).clickable { onVolver() }, shape = RoundedCornerShape(12.dp), color = SuperficieSecundaria, border = BorderStroke(1.dp, BordeSutil)) {
                    Box(contentAlignment = Alignment.Center) { Text(text = "<", color = TextoPrincipal, fontSize = 18.sp, fontWeight = FontWeight.Bold) }
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(text = "Reglas e Instrucciones", color = TextoPrincipal, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Todo lo que necesitas saber para jugar", color = TextoSecundario, fontSize = 13.sp)
                }
            }
            Spacer(modifier = Modifier.height(28.dp))

            // 1. OBJETIVO DEL JUEGO
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = FondoSeccion),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 22.dp, vertical = 26.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    EtiquetaSeccion(texto = "OBJETIVO DEL JUEGO")
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Tres en Raya se juega en un tablero de 3x3. El objetivo es formar una línea de tres símbolos iguales (X u O), ya sea horizontal, vertical o diagonal, antes que tu oponente.",
                        color = TextoSecundario,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Justify,
                        modifier = Modifier.fillMaxWidth(0.9f) // <-- Centra el bloque de texto (90% de ancho), el texto interno sigue justificado
                    )
                    Spacer(modifier = Modifier.height(22.dp))
                    TableroEjemplo(patron = listOf(listOf('X', 'X', 'X'), listOf('O', 'O', ' '), listOf(' ', ' ', ' ')), resaltadas = listOf(0 to 0, 0 to 1, 0 to 2))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = "Ejemplo: 'X' gana con una línea horizontal", color = TextoSecundario, fontSize = 12.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // 2. MODALIDADES DE JUEGO
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = FondoSeccion),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 22.dp, vertical = 26.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    EtiquetaSeccion(texto = "MODALIDADES DE JUEGO")
                    Spacer(modifier = Modifier.height(20.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        ModalidadItem(nombre = "Humano vs Computadora", descripcion = "Compite contra la IA en diferentes dificultades. Tú decides qué símbolo inicia la partida.", color = AzulX)
                        ModalidadItem(nombre = "Humano vs Humano", descripcion = "Partida local por turnos. Permite seleccionar el símbolo de inicio y analizar los movimientos de ambos.", color = RojoO)
                        ModalidadItem(nombre = "Computador vs Computador", descripcion = "Simulación entre algoritmos Minimax. Incluye controles de reproducción, pausa, avance manual y selector de velocidad.", color = VerdePrincipal)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // 3. HERRAMIENTAS
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = FondoSeccion),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 22.dp, vertical = 26.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    EtiquetaSeccion(texto = "HERRAMIENTAS")
                    Spacer(modifier = Modifier.height(20.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        ModalidadItem(nombre = "Recomendar Jugada", descripcion = "Calcula en tiempo real y resalta en verde la casilla óptima basándose en el algoritmo subyacente.", color = VerdePrincipal)
                        ModalidadItem(nombre = "Panel de Análisis", descripcion = "Despliega el árbol de decisiones: muestra las utilidades calculadas, posibles derrotas y las respuestas esperadas del oponente.", color = AzulX)
                        ModalidadItem(nombre = "Panel de Historial", descripcion = "Registra y grafica cada tablero anterior para repasar la estrategia empleada paso a paso.", color = RojoO)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // 4. FORMAS DE GANAR
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = FondoSeccion),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 22.dp, vertical = 26.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    EtiquetaSeccion(texto = "FORMAS DE GANAR")
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Una línea puede completarse de tres maneras distintas:",
                        color = TextoSecundario,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Justify,
                        modifier = Modifier.fillMaxWidth(0.9f) // <-- Centra el bloque de texto (90% de ancho), el texto interno sigue justificado
                    )
                    Spacer(modifier = Modifier.height(22.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            TableroEjemplo(patron = listOf(listOf('X', 'X', 'X'), listOf('O', ' ', 'O'), listOf(' ', ' ', ' ')), resaltadas = listOf(0 to 0, 0 to 1, 0 to 2), tamano = 80.dp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "Horizontal", color = TextoSecundario, fontSize = 11.sp)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            TableroEjemplo(patron = listOf(listOf('O', 'X', ' '), listOf('O', 'X', ' '), listOf('O', ' ', ' ')), resaltadas = listOf(0 to 0, 1 to 0, 2 to 0), tamano = 80.dp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "Vertical", color = TextoSecundario, fontSize = 11.sp)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            TableroEjemplo(patron = listOf(listOf('X', 'O', ' '), listOf('O', 'X', ' '), listOf(' ', ' ', 'X')), resaltadas = listOf(0 to 0, 1 to 1, 2 to 2), tamano = 80.dp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "Diagonal", color = TextoSecundario, fontSize = 11.sp)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // 5. EMPATE
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = FondoSeccion),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 22.dp, vertical = 26.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    EtiquetaSeccion(texto = "EMPATE")
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Si el tablero se llena por completo y ninguno de los dos jugadores logra formar una línea, la partida termina en empate.",
                        color = TextoSecundario,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Justify,
                        modifier = Modifier.fillMaxWidth(0.9f) // <-- Centra el bloque de texto (90% de ancho), el texto interno sigue justificado
                    )
                    Spacer(modifier = Modifier.height(22.dp))
                    TableroEjemplo(patron = listOf(listOf('X', 'O', 'X'), listOf('X', 'O', 'O'), listOf('O', 'X', 'X')))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = "Ejemplo: tablero completo sin ganador", color = TextoSecundario, fontSize = 12.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // 6. CONSEJOS
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = FondoSeccion),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 22.dp, vertical = 26.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    EtiquetaSeccion(texto = "CONSEJOS")
                    Spacer(modifier = Modifier.height(20.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(18.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        ConsejoItem(texto = "Controla el centro del tablero: es la casilla que intersecta más líneas posibles.", color = VerdePrincipal)
                        ConsejoItem(texto = "Bloquea a tu oponente siempre que esté a un movimiento de completar su línea.", color = AzulX)
                        ConsejoItem(texto = "Busca crear una doble amenaza (bifurcación): dos formas de ganar al mismo tiempo.", color = RojoO)
                        ConsejoItem(texto = "Utiliza el panel de análisis para estudiar cómo la IA previene las derrotas.", color = VerdePrincipal)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // 7. ¿QUÉ ES MINIMAX?
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = FondoSeccion),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 22.dp, vertical = 26.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    EtiquetaSeccion(texto = "¿QUÉ ES MINIMAX?")
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Minimax es el algoritmo que usa la computadora para decidir su jugada. Antes de mover, simula todas las jugadas posibles, imagina cómo respondería el oponente a cada una, y elige la opción que le da el mejor resultado garantizado, incluso si el oponente juega también de la mejor manera posible.",
                        color = TextoSecundario,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Justify,
                        modifier = Modifier.fillMaxWidth(0.9f) // <-- Centra el bloque de texto (90% de ancho), el texto interno sigue justificado
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        ModalidadItem(
                            nombre = "Jugador que Maximiza",
                            descripcion = "Representa a la computadora. Entre todas sus jugadas posibles, busca la que le dé el resultado más alto (la mejor utilidad) considerando la respuesta del oponente.",
                            color = VerdePrincipal
                        )
                        ModalidadItem(
                            nombre = "Jugador que Minimiza",
                            descripcion = "Representa al oponente. Se asume que, en su turno, siempre elegirá la respuesta que más perjudica a la computadora (la de menor utilidad para ella). De ahí el nombre 'Minimax'.",
                            color = RojoO
                        )
                        ModalidadItem(
                            nombre = "Función de Utilidad",
                            descripcion = "Cada tablero posible recibe un puntaje: se cuentan las líneas (filas, columnas o diagonales) que la computadora todavía puede completar, y se restan las que puede completar el oponente. A mayor puntaje, mejor es ese tablero para la IA.",
                            color = AzulX
                        )
                        ModalidadItem(
                            nombre = "Árbol de Decisiones",
                            descripcion = "El algoritmo organiza estas posibilidades como un árbol: primero las casillas donde podría jugar la computadora, y debajo de cada una, las respuestas que podría dar el oponente. Este es el mismo árbol que se muestra en el Panel de Análisis.",
                            color = VerdePrincipal
                        )
                        ModalidadItem(
                            nombre = "Relación con la Dificultad",
                            descripcion = "En Difícil, la computadora siempre juega el movimiento que Minimax calcula como óptimo. En Medio y Fácil, a veces ignora ese resultado a propósito y juega una casilla al azar, para que la partida sea más accesible.",
                            color = RojoO
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun ModalidadItem(nombre: String, descripcion: String, color: Color) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        EtiquetaPildora(texto = nombre, color = color)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = descripcion,
            color = TextoSecundario,
            fontSize = 13.sp,
            textAlign = TextAlign.Justify,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun ConsejoItem(texto: String, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(1f),
        verticalAlignment = Alignment.Top
    ) {
        // Punto indicador al lado izquierdo
        Box(modifier = Modifier.padding(top = 6.dp).size(6.dp).background(color = color, shape = RoundedCornerShape(50)))
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = texto,
            color = TextoSecundario,
            fontSize = 13.sp,
            textAlign = TextAlign.Justify,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun TableroEjemplo(patron: List<List<Char>>, resaltadas: List<Pair<Int, Int>> = emptyList(), tamano: androidx.compose.ui.unit.Dp = 110.dp) {
    Column(modifier = Modifier.size(tamano), verticalArrangement = Arrangement.spacedBy(0.dp)) {
        for (fila in patron.indices) {
            Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.spacedBy(0.dp)) {
                for (columna in patron[fila].indices) {
                    val contenido = patron[fila][columna]
                    val esGanadora = resaltadas.contains(fila to columna)
                    Surface(modifier = Modifier.weight(1f).aspectRatio(1f).border(width = 1.dp, color = BordeCasilla, shape = RoundedCornerShape(0.dp)), shape = RoundedCornerShape(0.dp), color = if (esGanadora) CasillaResaltada else CasillaLibre) {
                        Box(contentAlignment = Alignment.Center) {
                            if (contenido != ' ') { Text(text = contenido.toString(), color = if (contenido == 'X') AzulX else RojoO, fontSize = (tamano.value / 5.5).sp, fontWeight = FontWeight.Bold) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PasoInstruccion(numero: Int, titulo: String, descripcion: String, color: Color) {
    Row(verticalAlignment = Alignment.Top) {
        Surface(modifier = Modifier.size(28.dp), shape = RoundedCornerShape(50), color = color.copy(alpha = 0.15f), border = BorderStroke(1.dp, color)) {
            Box(contentAlignment = Alignment.Center) { Text(text = numero.toString(), color = color, fontSize = 12.sp, fontWeight = FontWeight.Bold) }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = titulo, color = TextoPrincipal, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = descripcion, color = TextoSecundario, fontSize = 13.sp)
        }
    }
}

@Composable
fun NotaDestacada(texto: String, color: Color = VerdePrincipal) {
    Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), color = color.copy(alpha = 0.12f), border = BorderStroke(1.dp, color.copy(alpha = 0.4f))) {
        Text(text = texto, color = TextoPrincipal, fontSize = 13.sp, modifier = Modifier.padding(12.dp))
    }
}


// ============================================================================
// SECCIÓN 12: COMPONENTES VISUALES REUTILIZABLES
// Piezas pequeñas (botones, etiquetas, selectores) usadas en varias pantallas de la app.
// ============================================================================

@Composable
fun EtiquetaSeccion(texto: String) {
    Text(text = texto, color = TextoSecundario, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
}

@Composable
fun DivisorSutil() {
    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(TextoSecundario.copy(alpha = 0.15f)))
}

@Composable
fun EtiquetaPildora(texto: String, color: Color) {
    Surface(shape = RoundedCornerShape(50), color = color.copy(alpha = 0.15f), border = BorderStroke(1.dp, color.copy(alpha = 0.5f))) {
        Text(text = texto, color = color, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
    }
}

@Composable
fun InsigniaSimbolo(simbolo: Char, color: Color) {
    Surface(modifier = Modifier.size(36.dp), shape = RoundedCornerShape(10.dp), color = color.copy(alpha = 0.15f), border = BorderStroke(1.dp, color.copy(alpha = 0.5f))) {
        Box(contentAlignment = Alignment.Center) { Text(text = simbolo.toString(), color = color, fontSize = 16.sp, fontWeight = FontWeight.Bold) }
    }
}

@Composable
fun AvatarIniciales(nombre: String, color: Color, tamano: androidx.compose.ui.unit.Dp = 56.dp) {
    val iniciales = nombre.split(" ").filter { it.isNotBlank() }.take(2).joinToString("") { it.first().uppercaseChar().toString() }
    Surface(modifier = Modifier.size(tamano), shape = RoundedCornerShape(50), color = color.copy(alpha = 0.15f), border = BorderStroke(1.5.dp, color)) {
        Box(contentAlignment = Alignment.Center) { Text(text = iniciales, color = color, fontSize = (tamano.value / 3).sp, fontWeight = FontWeight.Bold) }
    }
}

@Composable
fun IconoMenuHamburguesa(color: Color = TextoPrincipal, modifier: Modifier = Modifier) {
    Column(modifier = modifier.size(width = 24.dp, height = 16.dp), verticalArrangement = Arrangement.SpaceBetween) {
        repeat(3) { Box(modifier = Modifier.fillMaxWidth().height(2.dp).background(color)) }
    }
}

@Composable
fun OpcionSimbolo(simbolo: Char, seleccionado: Boolean, colorSimbolo: Color, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Surface(modifier = modifier.height(56.dp).clickable { onClick() }, shape = RoundedCornerShape(14.dp), color = if (seleccionado) colorSimbolo.copy(alpha = 0.15f) else SuperficieSecundaria, border = if (seleccionado) BorderStroke(2.dp, colorSimbolo) else BorderStroke(1.dp, BordeSutil)) {
        Box(contentAlignment = Alignment.Center) { Text(text = simbolo.toString(), color = colorSimbolo, fontSize = 24.sp, fontWeight = FontWeight.Bold) }
    }
}

@Composable
fun OpcionInicio(texto: String, seleccionado: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Surface(modifier = modifier.height(44.dp).clickable { onClick() }, shape = RoundedCornerShape(12.dp), color = if (seleccionado) VerdePrincipal.copy(alpha = 0.15f) else SuperficieSecundaria, border = if (seleccionado) BorderStroke(1.5.dp, VerdePrincipal) else BorderStroke(1.dp, BordeSutil)) {
        Box(contentAlignment = Alignment.Center) { Text(text = texto, color = if (seleccionado) TextoPrincipal else TextoSecundario, fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Normal, textAlign = TextAlign.Center, fontSize = 13.sp) }
    }
}

@Composable
fun OpcionDificultad(texto: String, seleccionado: Boolean, colorAcento: Color, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Surface(
        modifier = modifier.height(44.dp).clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        color = if (seleccionado) colorAcento.copy(alpha = 0.15f) else SuperficieSecundaria,
        border = if (seleccionado) BorderStroke(1.5.dp, colorAcento) else BorderStroke(1.dp, BordeSutil)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(text = texto, color = if (seleccionado) TextoPrincipal else TextoSecundario, fontSize = 13.sp, fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Medium)
        }
    }
}

@Composable
fun SelectorDificultad(
    dificultadSeleccionada: Dificultad,
    onSeleccion: (Dificultad) -> Unit,
    titulo: String = "DIFICULTAD"
) {
    Column {
        EtiquetaSeccion(texto = titulo)
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OpcionDificultad(texto = "Fácil", seleccionado = dificultadSeleccionada == Dificultad.FACIL, colorAcento = VerdePrincipal, modifier = Modifier.weight(1f), onClick = { onSeleccion(Dificultad.FACIL) })
            OpcionDificultad(texto = "Medio", seleccionado = dificultadSeleccionada == Dificultad.MEDIO, colorAcento = AzulX, modifier = Modifier.weight(1f), onClick = { onSeleccion(Dificultad.MEDIO) })
            OpcionDificultad(texto = "Difícil", seleccionado = dificultadSeleccionada == Dificultad.DIFICIL, colorAcento = RojoO, modifier = Modifier.weight(1f), onClick = { onSeleccion(Dificultad.DIFICIL) })
        }
    }
}

@Composable
fun SelectorVelocidad(velocidadSeleccionada: VelocidadSimulacion, onSeleccion: (VelocidadSimulacion) -> Unit) {
    Column {
        EtiquetaSeccion(texto = "VELOCIDAD DE SIMULACIÓN")
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            VelocidadSimulacion.values().forEach { opcion ->
                OpcionDificultad(texto = opcion.etiqueta, seleccionado = velocidadSeleccionada == opcion, colorAcento = VerdePrincipal, modifier = Modifier.weight(1f), onClick = { onSeleccion(opcion) })
            }
        }
    }
}

fun nombreDificultad(dificultad: Dificultad): String {
    return when (dificultad) {
        Dificultad.FACIL -> "Fácil"
        Dificultad.MEDIO -> "Medio"
        Dificultad.DIFICIL -> "Difícil"
    }
}

@Composable
fun BotonSecundarioAccion(texto: String, habilitado: Boolean = true, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Surface(modifier = modifier.height(64.dp).clickable(enabled = habilitado) { onClick() }, shape = RoundedCornerShape(14.dp), color = BordeSutil.copy(alpha = if (habilitado) 1f else 0.4f)) {
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = texto, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = if (habilitado) TextoPrincipal else TextoSecundario, textAlign = TextAlign.Center)
        }
    }
}
