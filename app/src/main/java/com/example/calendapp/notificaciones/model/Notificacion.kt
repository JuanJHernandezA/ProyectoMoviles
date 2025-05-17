package com.example.calendapp.notificaciones.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Work
import androidx.compose.ui.graphics.vector.ImageVector

data class Notificacion(
    val id: Int,
    val icono: ImageVector,
    val mensaje: String,
    val fecha: String,
    val leida: Boolean = false
)

// Lista de ejemplo de notificaciones
object NotificacionesEjemplo {
    val notificaciones = listOf(
        Notificacion(
            id = 1,
            icono = Icons.Default.AccessTime,
            mensaje = "Tu turno comienza pronto",
            fecha = "2024-03-20 09:00"
        ),
        Notificacion(
            id = 2,
            icono = Icons.Default.CheckCircle,
            mensaje = "Se ha registrado tu horario para el 20/03/2024",
            fecha = "2024-03-19 15:30"
        ),
        Notificacion(
            id = 3,
            icono = Icons.Default.Work,
            mensaje = "Trabajo pendiente",
            fecha = "2024-03-19 10:15"
        )
    )
} 