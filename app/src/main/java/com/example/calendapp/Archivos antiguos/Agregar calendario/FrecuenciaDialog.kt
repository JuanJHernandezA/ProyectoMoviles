package com.example.calendapp.agregar_calendario

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.foundation.clickable

@Composable
fun FrecuenciaDialog(
    onDismiss: () -> Unit,
    onOptionSelected: (String) -> Unit
) {
    val opciones = listOf(
        "No se repite",
        "Todos los días",
        "Todas las semanas",
        "Todos los meses",
        "Todos los años",
        "Personalización..."
    )

    var seleccion by remember { mutableStateOf(opciones[0]) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
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
                            .clickable { seleccion = opcion }
                    ) {
                        RadioButton(
                            selected = opcion == seleccion,
                            onClick = { seleccion = opcion }
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
                    horizontalArrangement = Arrangement.Center, // Cambiado a Center para centrar los botones
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(onClick = onDismiss) {
                        Text("Cancelar")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        onOptionSelected(seleccion)
                        // Solo cerrar el diálogo si no es "Personalización..."
                        if (seleccion != "Personalización...") {
                            onDismiss()
                        }
                    }) {
                        Text("Aceptar")
                    }
                }
            }
        }
    }
}
