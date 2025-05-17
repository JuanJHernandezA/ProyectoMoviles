package com.example.calendapp.notificaciones.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.calendapp.notificaciones.model.Notificacion
import com.example.calendapp.notificaciones.model.NotificacionesEjemplo

class NotificacionesViewModel : ViewModel() {
    var notificaciones by mutableStateOf(NotificacionesEjemplo.notificaciones)
        private set

    fun marcarComoLeida(id: Int) {
        notificaciones = notificaciones.map { notificacion ->
            if (notificacion.id == id) {
                notificacion.copy(leida = true)
            } else {
                notificacion
            }
        }
    }

    fun eliminarNotificacion(id: Int) {
        notificaciones = notificaciones.filter { it.id != id }
    }

    fun filtrarNotificaciones(filtro: String) {
        if (filtro.isEmpty()) {
            notificaciones = NotificacionesEjemplo.notificaciones
        } else {
            notificaciones = NotificacionesEjemplo.notificaciones.filter {
                it.mensaje.contains(filtro, ignoreCase = true)
            }
        }
    }
} 