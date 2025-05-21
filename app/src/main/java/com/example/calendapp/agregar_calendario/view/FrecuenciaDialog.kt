package com.example.calendapp.agregar_calendario.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.calendapp.agregar_calendario.viewmodel.FrecuenciaViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FrecuenciaDialog(
    onDismiss: () -> Unit,
    onOptionSelected: (String) -> Unit
) {
    val viewModel: FrecuenciaViewModel = viewModel()
    val opciones = listOf(
        "No se repite",
        "Todos los días",
        "Todas las semanas",
        "Todos los meses",
        "Todos los años",
        "Personalización..."
    )

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
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Personalización de frecuencia",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    opciones.forEach { opcion ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable { 
                                    viewModel.actualizarTipoFrecuencia(opcion)
                                    if (opcion == "Personalización...") {
                                        // Mostrar diálogo de personalización
                                    } else {
                                        onOptionSelected(opcion)
                                        onDismiss()
                                    }
                                }
                        ) {
                            RadioButton(
                                selected = opcion == viewModel.tipoFrecuencia.value,
                                onClick = { 
                                    viewModel.actualizarTipoFrecuencia(opcion)
                                    if (opcion == "Personalización...") {
                                        // Mostrar diálogo de personalización
                                    } else {
                                        onOptionSelected(opcion)
                                        onDismiss()
                                    }
                                }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = opcion,
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = onDismiss,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF0A446D)
                            )
                        ) {
                            Text("Cancelar", color = Color.White)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                onOptionSelected(viewModel.tipoFrecuencia.value)
                                if (viewModel.tipoFrecuencia.value != "Personalización...") {
                                    onDismiss()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF0A446D)
                            )
                        ) {
                            Text("Aceptar", color = Color.White)
                        }
                    }
                }
            }
        }
    }
} 