package com.example.calendapp.notificaciones.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calendapp.notificaciones.model.Notificacion
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*

class NotificacionesViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val _notificaciones = MutableStateFlow<List<Notificacion>>(emptyList())
    val notificaciones: StateFlow<List<Notificacion>> = _notificaciones
    
    private val _tieneNotificacionesNoLeidas = MutableStateFlow(false)
    val tieneNotificacionesNoLeidas: StateFlow<Boolean> = _tieneNotificacionesNoLeidas

    fun cargarNotificaciones(cedula: String) {
        viewModelScope.launch {
            try {
                Log.d("NotificacionesViewModel", "Cargando notificaciones para cédula: $cedula")
                
                val snapshot = db.collection("notificaciones")
                    .whereEqualTo("cedulaEmpleado", cedula)
                    .get()
                    .await()

                Log.d("NotificacionesViewModel", "Notificaciones encontradas: ${snapshot.documents.size}")

                val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                
                val notificacionesList = snapshot.documents.mapNotNull { doc ->
                    try {
                        val notificacion = Notificacion(
                            id = doc.id,
                            descripcion = doc.getString("descripcion") ?: "",
                            cedulaEmpleado = doc.getString("cedulaEmpleado") ?: "",
                            fecha = doc.getString("fecha") ?: "",
                            leida = doc.getBoolean("leida") ?: false
                        )
                        Log.d("NotificacionesViewModel", "Notificación procesada: $notificacion")
                        notificacion
                    } catch (e: Exception) {
                        Log.e("NotificacionesViewModel", "Error al procesar notificación: ${e.message}")
                        null
                    }
                }.sortedByDescending { notificacion ->
                    try {
                        dateFormat.parse(notificacion.fecha)?.time ?: 0L
                    } catch (e: Exception) {
                        Log.e("NotificacionesViewModel", "Error al parsear fecha: ${e.message}")
                        0L
                    }
                }
                
                Log.d("NotificacionesViewModel", "Total de notificaciones cargadas: ${notificacionesList.size}")
                _notificaciones.value = notificacionesList
                
                // Actualizar el estado de notificaciones no leídas
                _tieneNotificacionesNoLeidas.value = notificacionesList.any { !it.leida }
            } catch (e: Exception) {
                Log.e("NotificacionesViewModel", "Error al cargar notificaciones: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    fun marcarNotificacionComoLeida(notificacionId: String) {
        viewModelScope.launch {
            try {
                db.collection("notificaciones")
                    .document(notificacionId)
                    .update("leida", true)
                    .await()

                // Actualizar la lista local de notificaciones
                val notificacionesActualizadas = _notificaciones.value.map { notificacion ->
                    if (notificacion.id == notificacionId) {
                        notificacion.copy(leida = true)
                    } else {
                        notificacion
                    }
                }
                _notificaciones.value = notificacionesActualizadas

                // Actualizar el estado de notificaciones no leídas
                _tieneNotificacionesNoLeidas.value = notificacionesActualizadas.any { !it.leida }
            } catch (e: Exception) {
                Log.e("NotificacionesViewModel", "Error al marcar notificación como leída: ${e.message}")
            }
        }
    }

    fun marcarTodasComoLeidas() {
        viewModelScope.launch {
            try {
                val notificacionesNoLeidas = _notificaciones.value.filter { !it.leida }
                
                // Actualizar todas las notificaciones no leídas en la base de datos
                notificacionesNoLeidas.forEach { notificacion ->
                    db.collection("notificaciones")
                        .document(notificacion.id)
                        .update("leida", true)
                        .await()
                }

                // Actualizar la lista local de notificaciones
                val notificacionesActualizadas = _notificaciones.value.map { it.copy(leida = true) }
                _notificaciones.value = notificacionesActualizadas

                // Actualizar el estado de notificaciones no leídas
                _tieneNotificacionesNoLeidas.value = false
            } catch (e: Exception) {
                Log.e("NotificacionesViewModel", "Error al marcar todas las notificaciones como leídas: ${e.message}")
            }
        }
    }
} 