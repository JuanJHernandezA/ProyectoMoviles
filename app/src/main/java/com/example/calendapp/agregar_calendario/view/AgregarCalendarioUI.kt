package com.example.calendapp.agregar_calendario.view

import android.app.TimePickerDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.calendapp.R
import com.example.calendapp.agregar_calendario.viewmodel.AgregarCalendarioViewModel
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun AgregarCalendarioUI(
    navController: NavHostController,
    viewModel: AgregarCalendarioViewModel = viewModel(),
    onFrecuenciaClick: () -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // TimePickerDialogs
    val timePickerInicio = TimePickerDialog(
        context,
        { _, hourOfDay, minute ->
            viewModel.actualizarHoraInicio(String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute))
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        true
    )

    val timePickerFin = TimePickerDialog(
        context,
        { _, hourOfDay, minute ->
            viewModel.actualizarHoraFin(String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute))
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        true
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121C2B))
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
            // Ícono de menú y nombre de usuario
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menú",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Text(
                    text = "Usuario Temporal",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily.Default
                    ),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            // Ícono de notificaciones
            IconButton(onClick = { navController.navigate("notificaciones") }) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notificaciones",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
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
                text = "Nuevo Horario",
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
            Box {
                OutlinedTextField(
                    value = viewModel.nombreUsuario.value,
                    onValueChange = { viewModel.actualizarNombreUsuario(it) },
                    label = { Text("Escribe el nombre del usuario", color = Color.White) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.White) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF2A3C53),
                        unfocusedContainerColor = Color(0xFF2A3C53),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedIndicatorColor = Color.White,
                        unfocusedIndicatorColor = Color.Gray,
                        focusedLabelColor = Color.Gray,
                        unfocusedLabelColor = Color.Gray
                    )
                )
            }

            // Campo: Usuarios asignados
            OutlinedTextField(
                value = viewModel.usuariosSeleccionados.joinToString(", ") { it.nombre },
                onValueChange = {},
                label = { Text("Usuarios asignados", color = Color.White) },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = Color.White) },
                trailingIcon = {
                    IconButton(onClick = { viewModel.mostrarVentanaAsignados.value = true }) {
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

            // Etiqueta: Franja horaria
            Text(
                text = "Ingresa la franja horaria",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            // Campos: Inicio y Fin
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = { timePickerInicio.show() },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFF2A3C53))
                ) {
                    Icon(Icons.Default.AccessTime, contentDescription = null, tint = Color.White)
                    Spacer(Modifier.width(4.dp))
                    Text(viewModel.horaInicio.value, color = Color.White)
                }

                OutlinedButton(
                    onClick = { timePickerFin.show() },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFF2A3C53))
                ) {
                    Icon(Icons.Default.AccessTime, contentDescription = null, tint = Color.White)
                    Spacer(Modifier.width(4.dp))
                    Text(viewModel.horaFin.value, color = Color.White)
                }
            }

            // Campo: Frecuencia
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onFrecuenciaClick)
            ) {
                OutlinedTextField(
                    value = viewModel.frecuencia.value,
                    onValueChange = { },
                    label = { Text("Personalizar frecuencia", color = Color.White) },
                    leadingIcon = { Icon(Icons.Default.Refresh, contentDescription = null, tint = Color.White) },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    enabled = false,
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

            // Campo: Ubicación
            OutlinedTextField(
                value = viewModel.ubicacion.value,
                onValueChange = { viewModel.actualizarUbicacion(it) },
                label = { Text("Ubicación", color = Color.White) },
                leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.White) },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF2A3C53),
                    unfocusedContainerColor = Color(0xFF2A3C53),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedIndicatorColor = Color.White,
                    unfocusedIndicatorColor = Color.Gray,
                    focusedLabelColor = Color.Gray,
                    unfocusedLabelColor = Color.Gray
                )
            )

            // Campo: Descripción
            OutlinedTextField(
                value = viewModel.descripcion.value,
                onValueChange = { viewModel.actualizarDescripcion(it) },
                label = { Text("Descripción", color = Color.White) },
                leadingIcon = { Icon(Icons.Default.Description, contentDescription = null, tint = Color.White) },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF2A3C53),
                    unfocusedContainerColor = Color(0xFF2A3C53),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedIndicatorColor = Color.White,
                    unfocusedIndicatorColor = Color.Gray,
                    focusedLabelColor = Color.Gray,
                    unfocusedLabelColor = Color.Gray
                )
            )

            Spacer(modifier = Modifier.weight(1f))

            // Botones: Guardar y Cancelar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Botón Cancelar
                Button(
                    onClick = {
                        viewModel.limpiarCampos()
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = "Se canceló con éxito",
                                duration = SnackbarDuration.Short
                            )
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2A3C53)
                    )
                ) {
                    Text("Cancelar", color = Color.White)
                }

                // Botón Guardar
                Button(
                    onClick = {
                        viewModel.guardarCalendario()
                        viewModel.limpiarCampos()
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = "Se guardó con éxito",
                                duration = SnackbarDuration.Short
                            )
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0A446D)
                    )
                ) {
                    Text("Guardar", color = Color.White)
                }
            }
        }

        // Ventana de sugerencias de usuarios (posicionada absolutamente)
        if (viewModel.mostrarVentanaSugerencias.value) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .offset(y = 290.dp) // Ajustado para bajar un poco más
                    .zIndex(1f)
            ) {
                VentanaSugerenciasUsuarios(
                    usuarios = viewModel.usuariosSugeridos,
                    onSeleccionar = { index -> viewModel.seleccionarUsuario(index) },
                    onDismiss = { viewModel.mostrarVentanaSugerencias.value = false }
                )
            }
        }

        // Ventana de usuarios asignados
        if (viewModel.mostrarVentanaAsignados.value) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                VentanaUsuariosAsignados(
                    usuarios = viewModel.usuariosSeleccionados,
                    onEliminar = { index -> viewModel.eliminarUsuario(index) },
                    onCerrar = { viewModel.mostrarVentanaAsignados.value = false }
                )
            }
        }

        // Snackbar
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
} 