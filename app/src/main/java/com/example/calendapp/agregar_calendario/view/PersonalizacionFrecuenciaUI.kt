package com.example.calendapp.agregar_calendario.view

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.calendapp.agregar_calendario.viewmodel.FrecuenciaViewModel
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalizacionFrecuenciaDialog(
    onDismiss: () -> Unit,
    onComplete: (String) -> Unit
) {
    val viewModel: FrecuenciaViewModel = viewModel()
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

    val dias = listOf("L", "M", "M", "J", "V", "S", "D")
    var menuDesplegableAbierto by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .align(Alignment.Center),
                color = Color(0xFF1C1F2B),
                shape = RoundedCornerShape(16.dp)
            ) {
                Box {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        // Encabezado
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 24.dp),
                            horizontalArrangement = Arrangement.Center
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
                            OutlinedTextField(
                                value = viewModel.repeticionCada.value,
                                onValueChange = { if (it.all { char -> char.isDigit() }) viewModel.actualizarRepeticionCada(it) },
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

                            Box(modifier = Modifier.weight(1f)) {
                                OutlinedTextField(
                                    value = viewModel.periodo.value,
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
                                    modifier = Modifier.background(Color(0xFF1C1F2B))
                                ) {
                                    listOf("Días", "Semanas", "Meses", "Años").forEach { periodo ->
                                        DropdownMenuItem(
                                            text = { Text(periodo, color = Color.White) },
                                            onClick = {
                                                viewModel.actualizarPeriodo(periodo)
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

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            dias.forEachIndexed { index, dia ->
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(
                                            color = if (viewModel.diasSeleccionados[index]) Color(0xFF4FC3F7) else Color.Transparent,
                                            shape = CircleShape
                                        )
                                        .border(
                                            width = 1.dp,
                                            color = if (viewModel.diasSeleccionados[index]) Color(0xFF4FC3F7) else Color.White,
                                            shape = CircleShape
                                        )
                                        .clickable {
                                            viewModel.toggleDiaSeleccionado(index)
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = dia,
                                        color = if (viewModel.diasSeleccionados[index]) Color.White else Color.White,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Finaliza
                        Text("Finaliza", color = Color.White)
                        Spacer(modifier = Modifier.height(8.dp))

                        Column {
                            // Opción "Nunca"
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                RadioButton(
                                    selected = viewModel.tipoFinalizacion.value == "Nunca",
                                    onClick = { viewModel.actualizarTipoFinalizacion("Nunca") },
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
                                        viewModel.actualizarTipoFinalizacion("El dd/mm/aaaa")
                                        showDatePicker = true
                                    }
                            ) {
                                RadioButton(
                                    selected = viewModel.tipoFinalizacion.value == "El dd/mm/aaaa",
                                    onClick = {
                                        viewModel.actualizarTipoFinalizacion("El dd/mm/aaaa")
                                        showDatePicker = true
                                    },
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = Color(0xFF4FC3F7),
                                        unselectedColor = Color.White
                                    )
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (viewModel.fechaFinalizacion.value.isEmpty()) "El dd/mm/aaaa" else "El ${viewModel.fechaFinalizacion.value}",
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
                                    selected = viewModel.tipoFinalizacion.value == "Despues de # repeticion",
                                    onClick = { viewModel.actualizarTipoFinalizacion("Despues de # repeticion") },
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = Color(0xFF4FC3F7),
                                        unselectedColor = Color.White
                                    )
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                
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
                                    
                                    OutlinedTextField(
                                        value = viewModel.finalizaDespuesDe.value,
                                        onValueChange = { if (it.all { char -> char.isDigit() }) viewModel.actualizarFinalizaDespuesDe(it) },
                                        placeholder = { Text("#") },
                                        modifier = Modifier
                                            .widthIn(min = 50.dp, max = 54.dp)
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
                                    
                                    Text(
                                        text = "repeticiones",
                                        color = Color.White,
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Botones
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(
                                onClick = {
                                    mostrarMensaje("Se canceló con éxito")
                                    onDismiss()
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF0A446D)
                                )
                            ) {
                                Text("Cancelar", color = Color.White)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = {
                                    mostrarMensaje("Se guardó con éxito")
                                    onComplete(viewModel.obtenerFrecuencia())
                                    onDismiss()
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF0A446D)
                                )
                            ) {
                                Text("Aceptar", color = Color.White)
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
    }

    // Mostrar diálogo nativo cuando sea necesario
    if (showDatePicker) {
        val context = LocalContext.current
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                viewModel.actualizarFechaFinalizacion("$dayOfMonth/${month + 1}/$year")
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
        showDatePicker = false
    }
} 