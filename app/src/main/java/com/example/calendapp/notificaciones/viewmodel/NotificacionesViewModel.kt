package com.example.calendapp.notificaciones.viewmodel

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
                val snapshot = db.collection("notificaciones")
                    .whereEqualTo("cedulaEmpleado", cedula)
                    .get()
                    .await()

                val notificacionesList = snapshot.documents.mapNotNull { doc ->
                    val notificacion = doc.toObject(Notificacion::class.java)
                    notificacion?.copy(id = doc.id)
                }
                _notificaciones.value = notificacionesList
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
} 