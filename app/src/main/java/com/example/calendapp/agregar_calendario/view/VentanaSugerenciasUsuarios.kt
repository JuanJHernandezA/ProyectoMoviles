package com.example.calendapp.agregar_calendario.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.calendapp.agregar_calendario.model.Usuario
import com.example.calendapp.agregar_calendario.viewmodel.AgregarCalendarioViewModel

@Composable
fun VentanaSugerenciasUsuarios(
    usuarios: List<Usuario>,
    onSeleccionar: (Int) -> Unit,
    onDismiss: () -> Unit,
    viewModel: AgregarCalendarioViewModel
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 400.dp)
            .border(
                width = 1.dp,
                color = Color.White,
                shape = RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2A3C53)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Título y botón de cerrar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Sugerencias de Usuarios",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cerrar",
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Lista de usuarios
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(usuarios) { usuario ->
                    UsuarioSugeridoItem(
                        usuario = usuario,
                        onClick = { onSeleccionar(usuarios.indexOf(usuario)) },
                        viewModel = viewModel
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de confirmar
            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0A446D)
                )
            ) {
                Text("Confirmar selección", color = Color.White)
            }
        }
    }
}

@Composable
private fun UsuarioSugeridoItem(
    usuario: Usuario,
    onClick: () -> Unit,
    viewModel: AgregarCalendarioViewModel
) {
    val isSelected = viewModel.usuariosSeleccionados.value.any { it.id == usuario.id }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFF0A446D) else Color(0xFF1A2530)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${usuario.nombre} ${usuario.apellido}",
                color = Color.White,
                fontSize = 16.sp
            )
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Seleccionado",
                    tint = Color.White
                )
            }
        }
    }
} 