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

class NotificacionesViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val _notificaciones = MutableStateFlow<List<Notificacion>>(emptyList())
    val notificaciones: StateFlow<List<Notificacion>> = _notificaciones

    fun cargarNotificaciones(cedula: String) {
        viewModelScope.launch {
            try {
                Log.d("NotificacionesViewModel", "Cargando notificaciones para cédula: $cedula")
                
                val snapshot = db.collection("notificaciones")
                    .whereEqualTo("cedulaEmpleado", cedula)
                    .get()
                    .await()

                Log.d("NotificacionesViewModel", "Notificaciones encontradas: ${snapshot.documents.size}")

                val notificacionesList = snapshot.documents.mapNotNull { doc ->
                    try {
                        val notificacion = Notificacion(
                            id = doc.id,
                            descripcion = doc.getString("descripcion") ?: "",
                            cedulaEmpleado = doc.getString("cedulaEmpleado") ?: "",
                            fecha = doc.getString("fecha") ?: ""
                        )
                        Log.d("NotificacionesViewModel", "Notificación procesada: $notificacion")
                        notificacion
                    } catch (e: Exception) {
                        Log.e("NotificacionesViewModel", "Error al procesar notificación: ${e.message}")
                        null
                    }
                }
                
                Log.d("NotificacionesViewModel", "Total de notificaciones cargadas: ${notificacionesList.size}")
                _notificaciones.value = notificacionesList
            } catch (e: Exception) {
                Log.e("NotificacionesViewModel", "Error al cargar notificaciones: ${e.message}")
                e.printStackTrace()
            }
        }
    }
} 