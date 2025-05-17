package com.example.calendapp.agregar_calendario

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Dialog
import java.util.*
import androidx.compose.foundation.border
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.text.style.TextAlign
import kotlinx.coroutines.launch

@Composable
fun PersonalizacionFrecuenciaDialog(
    onDismiss: () -> Unit,
    onComplete: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Función para mostrar mensaje
    fun mostrarMensaje(mensaje: String) {
        scope.launch {
            snackbarHostState.showSnackbar(
                message = mensaje,
                duration = SnackbarDuration.Short
            )
        }
    }

    var selectedRadio by remember { mutableStateOf("Nunca") }
    val dias = listOf("L", "M", "M", "J", "V", "S", "D")
    val diasSeleccionados = remember { mutableStateListOf(*Array(dias.size) { false }) }
    var numeroRepeticionesSeRepiteCada by remember { mutableStateOf("") } // Para la caja de texto "Se repite cada"
    var numeroRepeticionesFinaliza by remember { mutableStateOf("") } // Para la caja de texto "Después de # repeticiones"
    var periodoSeleccionado by remember { mutableStateOf("Días") } // Para el menú desplegable
    var menuDesplegableAbierto by remember { mutableStateOf(false) } // Estado del menú desplegable
    var fechaSeleccionada by remember { mutableStateOf("") } // Para la fecha seleccionada
    var showDatePicker by remember { mutableStateOf(false) } // Estado para mostrar el DatePicker

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 16.dp),
            color = Color(0xFF1C1F2B),
            shape = RoundedCornerShape(16.dp)
        ) {
            Box {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    // Contenido principal con scroll
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                    ) {
                        // Encabezado
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 24.dp),
                            horizontalArrangement = Arrangement.Center // Centrar el título
                        ) {
                            Text(
                                text = "Frecuencia personalizada",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )
                        }

                        // Se repite cada
                        Text("Se repite cada", color = Color.White)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row {
                            // Caja de texto para el numeral "Se repite cada"
                            OutlinedTextField(
                                value = numeroRepeticionesSeRepiteCada,
                                onValueChange = { if (it.all { char -> char.isDigit() }) numeroRepeticionesSeRepiteCada = it },
                                placeholder = { Text("#") },
                                modifier = Modifier
                                    .width(80.dp)
                                    .height(56.dp),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    disabledTextColor = Color.Gray,
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    disabledContainerColor = Color.Transparent,
                                    cursorColor = Color(0xFF4FC3F7),
                                    errorCursorColor = Color.Red,
                                    focusedBorderColor = Color(0xFF4FC3F7),
                                    unfocusedBorderColor = Color.Gray,
                                    disabledBorderColor = Color.Transparent,
                                    errorBorderColor = Color.Red,
                                    focusedLabelColor = Color.Gray,
                                    unfocusedLabelColor = Color.Gray,
                                    focusedPlaceholderColor = Color.Gray,
                                    unfocusedPlaceholderColor = Color.Gray,
                                    disabledPlaceholderColor = Color.LightGray
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))

                            // Menú desplegable para el periodo
                            Box(modifier = Modifier.weight(1f)) {
                                OutlinedTextField(
                                    value = periodoSeleccionado,
                                    onValueChange = {},
                                    readOnly = true,
                                    trailingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.ArrowDropDown,
                                            contentDescription = "Abrir menú",
                                            tint = Color.White,
                                            modifier = Modifier.clickable { menuDesplegableAbierto = true }
                                        )
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(56.dp),
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White,
                                        disabledTextColor = Color.Gray,
                                        focusedContainerColor = Color.Transparent,
                                        unfocusedContainerColor = Color.Transparent,
                                        disabledContainerColor = Color.Transparent,
                                        cursorColor = Color(0xFF4FC3F7),
                                        errorCursorColor = Color.Red,
                                        focusedBorderColor = Color(0xFF4FC3F7),
                                        unfocusedBorderColor = Color.Gray,
                                        disabledBorderColor = Color.Transparent,
                                        errorBorderColor = Color.Red,
                                        focusedLabelColor = Color.Gray,
                                        unfocusedLabelColor = Color.Gray,
                                        focusedPlaceholderColor = Color.Gray,
                                        unfocusedPlaceholderColor = Color.Gray,
                                        disabledPlaceholderColor = Color.LightGray
                                    )
                                )

                                DropdownMenu(
                                    expanded = menuDesplegableAbierto,
                                    onDismissRequest = { menuDesplegableAbierto = false },
                                    modifier = Modifier.background(Color(0xFF1C1F2B)) // Color oscuro para el menú desplegable
                                ) {
                                    listOf("Días", "Semanas", "Meses", "Años").forEach { periodo ->
                                        DropdownMenuItem(
                                            text = { Text(periodo, color = Color.White) }, // Texto blanco
                                            onClick = {
                                                periodoSeleccionado = periodo
                                                menuDesplegableAbierto = false
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Sección de días de la semana
                        Text("Se repite el", color = Color.White)
                        Spacer(modifier = Modifier.height(8.dp))

                        // Fila de días de la semana
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            dias.forEachIndexed { index, dia ->
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(
                                            color = if (diasSeleccionados[index]) Color(0xFF4FC3F7) else Color.Transparent,
                                            shape = CircleShape
                                        )
                                        .border(
                                            width = 1.dp,
                                            color = if (diasSeleccionados[index]) Color(0xFF4FC3F7) else Color.White,
                                            shape = CircleShape
                                        )
                                        .clickable {
                                            diasSeleccionados[index] = !diasSeleccionados[index]
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = dia,
                                        color = if (diasSeleccionados[index]) Color.White else Color.White,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Finaliza
                        Text("Finaliza", color = Color.White)
                        Spacer(modifier = Modifier.height(8.dp))

                        // Opciones de finalización
                        Column {
                            // Opción "Nunca"
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                RadioButton(
                                    selected = selectedRadio == "Nunca",
                                    onClick = { selectedRadio = "Nunca" },
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = Color(0xFF4FC3F7),
                                        unselectedColor = Color.White
                                    )
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Nunca",
                                    color = Color.White,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }

                            // Opción "El dd/mm/aaaa"
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable {
                                        selectedRadio = "El dd/mm/aaaa"
                                        showDatePicker = true
                                    }
                            ) {
                                RadioButton(
                                    selected = selectedRadio == "El dd/mm/aaaa" || selectedRadio.startsWith("El "),
                                    onClick = {
                                        selectedRadio = "El dd/mm/aaaa"
                                        showDatePicker = true
                                    },
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = Color(0xFF4FC3F7),
                                        unselectedColor = Color.White
                                    )
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (fechaSeleccionada.isEmpty()) "El dd/mm/aaaa" else "El $fechaSeleccionada",
                                    color = Color.White,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }

                            // Opción "Después de # repeticiones"
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                RadioButton(
                                    selected = selectedRadio == "Despues de # repeticion",
                                    onClick = { selectedRadio = "Despues de # repeticion" },
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = Color(0xFF4FC3F7),
                                        unselectedColor = Color.White
                                    )
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                
                                // Contenedor flexible para el texto y la caja de texto
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = "Después de",
                                        color = Color.White,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    
                                    // Caja de texto con ancho dinámico pero limitado
                                    OutlinedTextField(
                                        value = numeroRepeticionesFinaliza,
                                        onValueChange = { if (it.all { char -> char.isDigit() }) numeroRepeticionesFinaliza = it },
                                        placeholder = { Text("#") },
                                        modifier = Modifier
                                            .widthIn(min = 50.dp, max = 54.dp) // Limitamos el ancho máximo para dejar espacio a "repeticiones"
                                            .height(57.dp),
                                        singleLine = true,
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedTextColor = Color.White,
                                            unfocusedTextColor = Color.White,
                                            disabledTextColor = Color.Gray,
                                            focusedContainerColor = Color.Transparent,
                                            unfocusedContainerColor = Color.Transparent,
                                            disabledContainerColor = Color.Transparent,
                                            cursorColor = Color(0xFF4FC3F7),
                                            errorCursorColor = Color.Red,
                                            focusedBorderColor = Color(0xFF4FC3F7),
                                            unfocusedBorderColor = Color.Gray,
                                            disabledBorderColor = Color.Transparent,
                                            errorBorderColor = Color.Red,
                                            focusedLabelColor = Color.Gray,
                                            unfocusedLabelColor = Color.Gray,
                                            focusedPlaceholderColor = Color.Gray,
                                            unfocusedPlaceholderColor = Color.Gray,
                                            disabledPlaceholderColor = Color.LightGray
                                        )
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    
                                    // Texto "repeticiones" con espacio garantizado
                                    Text(
                                        text = "repeticiones",
                                        color = Color.White,
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.weight(1f) // Toma el espacio restante
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Botones centrados en la parte inferior
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(onClick = {
                            mostrarMensaje("Se canceló con éxito")
                            onDismiss()
                        }) {
                            Text("Cancelar")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = {
                            mostrarMensaje("Se guardó con éxito")
                            onComplete()
                            onDismiss()
                        }) {
                            Text("Aceptar")
                        }
                    }
                }

                // SnackbarHost personalizado
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
            }
        }
    }

    // Mostrar diálogo nativo cuando sea necesario
    if (showDatePicker) {
        val context = LocalContext.current
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                fechaSeleccionada = "$dayOfMonth/${month + 1}/$year"
                selectedRadio = "El dd/mm/aaaa" // Mantenemos el valor base para la selección
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
        showDatePicker = false
    }
}

@Composable
fun RadioButtonWithLabel(text: String, selectedValue: String, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(vertical = 4.dp)
            .fillMaxWidth()
    ) {
        RadioButton(
            selected = text == selectedValue,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = Color(0xFF4FC3F7),
                unselectedColor = Color.White
            )
        )
        Text(text = text, color = Color.White)
    }
}