package com.example.calendapp.agregar_calendario

import android.app.TimePickerDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.calendapp.R
import java.util.*
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.ui.text.style.TextAlign
import com.example.calendapp.notificaciones.NotificacionesUI

@Composable
fun NuevoHorarioScreen() {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Estados para las horas seleccionadas
    var horaInicio by remember { mutableStateOf("Inicio") }
    var horaFin by remember { mutableStateOf("Fin") }

    // Estados para usuarios
    var nombreUsuario by remember { mutableStateOf("") }
    var mostrarVentanaSugerencias by remember { mutableStateOf(false) }
    var mostrarVentanaAsignados by remember { mutableStateOf(false) }
    var usuariosSeleccionados by remember { mutableStateOf(listOf<UsuarioSugerido>()) }
    var usuariosSugeridos by remember { 
        mutableStateOf(
            listOf(
                UsuarioSugerido("Carlos Pérez", false),
                UsuarioSugerido("María Gómez", false),
                UsuarioSugerido("Luis Martínez", false),
                UsuarioSugerido("Ana Torres", false),
                UsuarioSugerido("Diego Ríos", false)
            )
        ) 
    }

    // Agregar estos estados al inicio de la función
    var ubicacion by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

    // Función para limpiar todos los campos
    fun limpiarCampos() {
        horaInicio = "Inicio"
        horaFin = "Fin"
        ubicacion = ""
        descripcion = ""
        nombreUsuario = ""
        mostrarVentanaSugerencias = false
        mostrarVentanaAsignados = false
        usuariosSeleccionados = listOf()
        usuariosSugeridos = listOf(
            UsuarioSugerido("Carlos Pérez", false),
            UsuarioSugerido("María Gómez", false),
            UsuarioSugerido("Luis Martínez", false),
            UsuarioSugerido("Ana Torres", false),
            UsuarioSugerido("Diego Ríos", false)
        )
    }

    // Función para mostrar mensaje
    fun mostrarMensaje(mensaje: String) {
        scope.launch {
            snackbarHostState.showSnackbar(
                message = mensaje,
                duration = SnackbarDuration.Short
            )
        }
    }

    // TimePickerDialogs
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val timePickerInicio = TimePickerDialog(
        context,
        { _, hourOfDay, minute ->
            horaInicio = String.format("%02d:%02d", hourOfDay, minute)
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        true // Usar formato de 24 horas
    )

    val timePickerFin = TimePickerDialog(
        context,
        { _, hourOfDay, minute ->
            horaFin = String.format("%02d:%02d", hourOfDay, minute)
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        true // Usar formato de 24 horas
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121C2B)) // Fondo general oscuro
    ) {
        // Fondo de color específico detrás de la imagen
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(204.dp)
                .background(Color(0xFF132D46))
                .align(Alignment.TopCenter)
        )

        // Imagen de fondo encima del fondo de color
        Image(
            painter = painterResource(id = R.drawable.image3),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(204.dp)
                .align(Alignment.TopCenter)
                .graphicsLayer { alpha = 0.9f },
            contentScale = ContentScale.Crop
        )

        // Cabecera con el ícono de menú, nombre del usuario y campana de notificaciones
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Ícono de menú (tres barras) más grande
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { /* Acción para abrir el menú */ }) {
                    Icon(
                        imageVector = Icons.Default.Menu, // Cambia a ícono de menú
                        contentDescription = "Menú",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp) // Tamaño más grande
                    )
                }

                // Nombre del usuario justo al lado del menú
                Text(
                    text = "Usuario Temporal",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily.Default
                    ),
                    modifier = Modifier.padding(start = 8.dp) // Espaciado entre el ícono y el texto
                )
            }

            // Ícono de notificaciones (campana) más grande
            IconButton(onClick = { navController.navigate("notificaciones") }) {
                Icon(
                    imageVector = Icons.Default.Notifications, // Ícono de campana
                    contentDescription = "Notificaciones",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp) // Tamaño más grande
                )
            }
        }

        // Título en la parte superior centrado
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 100.dp)
        ) {
            Text(
                text = "Agregar Calendario",
                style = TextStyle(
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Default
                ),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        // Contenido principal
        Column(
            modifier = Modifier
                .padding(top = 220.dp)
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Campo: Nombre del usuario
            OutlinedTextField(
                value = nombreUsuario,
                onValueChange = { 
                    nombreUsuario = it
                    mostrarVentanaSugerencias = it.isNotEmpty()
                },
                label = { Text("Escribe el nombre del usuario", color = Color.White) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.White) },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF2A3C53),
                    unfocusedContainerColor = Color(0xFF2A3C53),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedIndicatorColor = Color.White,
                    unfocusedIndicatorColor = Color.Gray
                )
            )

            // Campo: Usuarios asignados
            OutlinedTextField(
                value = usuariosSeleccionados.joinToString(", ") { it.nombre },
                onValueChange = {},
                label = { Text("Usuarios asignados", color = Color.White) },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = Color.White) },
                trailingIcon = {
                    IconButton(onClick = { mostrarVentanaAsignados = true }) {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Ver usuarios asignados",
                            tint = Color.White
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = false,
                readOnly = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF2A3C53),
                    unfocusedContainerColor = Color(0xFF2A3C53),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedIndicatorColor = Color.White,
                    unfocusedIndicatorColor = Color.Gray,
                    disabledTextColor = Color.White,
                    disabledContainerColor = Color(0xFF2A3C53),
                    disabledIndicatorColor = Color.Gray
                )
            )

            // Etiqueta: Franja horaria centrada, más grande y en negrita
            Text(
                text = "Ingresa la franja horaria",
                color = Color.White,
                style = TextStyle(
                    fontSize = 18.sp, // 3 puntos más grande
                    fontWeight = FontWeight.Bold // Negrita
                ),
                modifier = Modifier.align(Alignment.CenterHorizontally) // Centrado
            )

            // Campos: Inicio y Fin
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = { timePickerInicio.show() }, // Mostrar TimePicker para "Inicio"
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFF2A3C53))
                ) {
                    Icon(Icons.Default.AccessTime, contentDescription = null, tint = Color.White)
                    Spacer(Modifier.width(4.dp))
                    Text(horaInicio, color = Color.White) // Mostrar la hora seleccionada o "Inicio"
                }

                OutlinedButton(
                    onClick = { timePickerFin.show() }, // Mostrar TimePicker para "Fin"
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFF2A3C53))
                ) {
                    Icon(Icons.Default.AccessTime, contentDescription = null, tint = Color.White)
                    Spacer(Modifier.width(4.dp))
                    Text(horaFin, color = Color.White) // Mostrar la hora seleccionada o "Fin"
                }
            }

            // Reemplazar el OutlinedTextField actual por un Box con OutlinedTextField
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate("frecuencia_dialog")
                    }
            ) {
                OutlinedTextField(
                    value = "", // Campo vacío ya que es solo para mostrar
                    onValueChange = { },
                    label = { Text("Personalizar frecuencia", color = Color.White) },
                    leadingIcon = { Icon(Icons.Default.Refresh, contentDescription = null, tint = Color.White) },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true, // Hacer el campo de solo lectura
                    enabled = false, // Deshabilitar el campo para evitar interacciones directas
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF2A3C53),
                        unfocusedContainerColor = Color(0xFF2A3C53),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedIndicatorColor = Color.White,
                        unfocusedIndicatorColor = Color.Gray,
                        disabledTextColor = Color.White,
                        disabledContainerColor = Color(0xFF2A3C53),
                        disabledIndicatorColor = Color.Gray
                    )
                )
            }

            // Campo: Agregar ubicación
            OutlinedTextField(
                value = ubicacion,
                onValueChange = { ubicacion = it },
                label = { Text("Agregar ubicación", color = Color.White) },
                leadingIcon = { Icon(Icons.Default.Place, contentDescription = null, tint = Color.White) },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF2A3C53),
                    unfocusedContainerColor = Color(0xFF2A3C53),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedIndicatorColor = Color.White,
                    unfocusedIndicatorColor = Color.Gray
                )
            )

            // Campo: Añadir descripción
            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Añade una descripción", color = Color.White) },
                leadingIcon = { Icon(Icons.Default.Add, contentDescription = null, tint = Color.White) },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF2A3C53),
                    unfocusedContainerColor = Color(0xFF2A3C53),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedIndicatorColor = Color.White,
                    unfocusedIndicatorColor = Color.Gray
                )
            )

            // Botones inferiores: Guardar y Cancelar (intercambiados)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        limpiarCampos()
                        mostrarMensaje("Se canceló con éxito")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0A446D))
                ) {
                    Text("Cancelar", color = Color.White)
                }

                Button(
                    onClick = {
                        limpiarCampos()
                        mostrarMensaje("Se guardó con éxito")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0A446D))
                ) {
                    Text("Guardar", color = Color.White)
                }
            }
        }

        // Overlay para las ventanas
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Overlay semi-transparente que bloquea los toques
            if (mostrarVentanaSugerencias || mostrarVentanaAsignados) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f))
                        .clickable(enabled = false) { }
                )
            }

            // Ventana de sugerencias de usuarios
            if (mostrarVentanaSugerencias) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = 283.dp)
                ) {
                    VentanaSugerenciasUsuarios(
                        usuarios = usuariosSugeridos,
                        onSeleccionar = { index ->
                            val usuario = usuariosSugeridos[index]
                            if (usuario.seleccionado) {
                                usuariosSeleccionados = usuariosSeleccionados - usuario
                            } else {
                                usuariosSeleccionados = usuariosSeleccionados + usuario
                            }
                            usuariosSugeridos = usuariosSugeridos.mapIndexed { i, u ->
                                if (i == index) u.copy(seleccionado = !u.seleccionado) else u
                            }
                        },
                        onDismiss = { mostrarVentanaSugerencias = false }
                    )
                }
            }

            // Ventana de usuarios asignados
            if (mostrarVentanaAsignados) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = 360.dp)
                ) {
                    VentanaUsuariosAsignados(
                        usuarios = usuariosSeleccionados,
                        onEliminar = { index ->
                            val usuario = usuariosSeleccionados[index]
                            usuariosSeleccionados = usuariosSeleccionados - usuario
                            usuariosSugeridos = usuariosSugeridos.map { u ->
                                if (u.nombre == usuario.nombre) u.copy(seleccionado = false) else u
                            }
                        },
                        onCerrar = { mostrarVentanaAsignados = false }
                    )
                }
            }
        }

        // Agregar el SnackbarHost con estilo personalizado
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            snackbar = { snackbarData: SnackbarData ->
                Snackbar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    containerColor = Color(0xFF2A3C53),
                    contentColor = Color.White,
                    action = null
                ) {
                    Text(
                        text = snackbarData.visuals.message,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        )

        // Navegación
        NavHost(navController = navController, startDestination = "nuevo_horario") {
            composable("nuevo_horario") { /* Pantalla actual */ }
            composable("frecuencia_dialog") {
                FrecuenciaDialog(
                    onDismiss = { navController.popBackStack() },
                    onOptionSelected = { opcion ->
                        if (opcion == "Personalización...") {
                            navController.navigate("personalizacion_frecuencia") // Navega a la pantalla de personalización
                        }
                    }
                )
            }
            composable("personalizacion_frecuencia") {
                PersonalizacionFrecuenciaDialog(
                    onDismiss = { navController.popBackStack() },
                    onComplete = { navController.popBackStack("nuevo_horario", inclusive = false) }
                )
            }
            composable("notificaciones") {
                NotificacionesUI(navController = navController)
            }
        }
    }
}
