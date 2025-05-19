package com.example.calendapp.agregar_calendario.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calendapp.agregar_calendario.model.Calendario
import com.example.calendapp.agregar_calendario.model.Usuario
import com.example.calendapp.agregar_calendario.model.UsuariosSugeridosEjemplo
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AgregarCalendarioViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    
    // Estado para los campos de entrada
    val nombreUsuario = mutableStateOf("")
    val horaInicio = mutableStateOf("00:00")
    val horaFin = mutableStateOf("00:00")
    val ubicacion = mutableStateOf("")
    val descripcion = mutableStateOf("")
    val frecuencia = mutableStateOf("")
    val fechasSeleccionadas = mutableStateOf<List<String>>(emptyList())

    // Estado para las ventanas emergentes
    val mostrarVentanaSugerencias = mutableStateOf(false)
    val mostrarVentanaAsignados = mutableStateOf(false)
    val mostrarCalendario = mutableStateOf(false)

    // Listas de usuarios
    private val _usuariosSugeridos = MutableStateFlow<List<Usuario>>(emptyList())
    val usuariosSugeridos: StateFlow<List<Usuario>> = _usuariosSugeridos

    val usuariosSeleccionados = mutableStateOf<List<Usuario>>(emptyList())

    init {
        // Inicialización de valores por defecto
        limpiarCampos()
    }

    fun buscarUsuarios(query: String) {
        viewModelScope.launch {
            try {
                if (query.isEmpty()) {
                    _usuariosSugeridos.value = emptyList()
                    return@launch
                }

                val snapshot = db.collection("users")
                    .whereEqualTo("rol", "usuario")
                    .get()
                    .await()

                val usuarios = snapshot.documents.mapNotNull { doc ->
                    val nombre = doc.getString("nombre") ?: return@mapNotNull null
                    val apellido = doc.getString("apellido") ?: return@mapNotNull null
                    val rol = doc.getString("rol") ?: return@mapNotNull null
                    
                    Usuario(
                        id = doc.id,
                        nombre = nombre,
                        apellido = apellido,
                        rol = rol
                    )
                }.filter { usuario ->
                    val nombreCompleto = "${usuario.nombre} ${usuario.apellido}".lowercase()
                    nombreCompleto.contains(query.lowercase())
                }

                _usuariosSugeridos.value = usuarios
                mostrarVentanaSugerencias.value = usuarios.isNotEmpty()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Métodos para actualizar el estado
    fun actualizarNombreUsuario(nuevoNombre: String) {
        nombreUsuario.value = nuevoNombre
        if (nuevoNombre.isNotEmpty()) {
            buscarUsuarios(nuevoNombre)
        } else {
            _usuariosSugeridos.value = emptyList()
            mostrarVentanaSugerencias.value = false
        }
    }

    fun actualizarHoraInicio(nuevaHora: String) {
        horaInicio.value = nuevaHora
    }

    fun actualizarHoraFin(nuevaHora: String) {
        horaFin.value = nuevaHora
    }

    fun actualizarUbicacion(nuevaUbicacion: String) {
        ubicacion.value = nuevaUbicacion
    }

    fun actualizarDescripcion(nuevaDescripcion: String) {
        descripcion.value = nuevaDescripcion
    }

    fun actualizarFrecuencia(nuevaFrecuencia: String) {
        frecuencia.value = nuevaFrecuencia
    }

    // Métodos para manejar usuarios
    fun seleccionarUsuario(index: Int) {
        val usuario = _usuariosSugeridos.value.getOrNull(index) ?: return
        if (!usuariosSeleccionados.value.any { it.id == usuario.id }) {
            usuariosSeleccionados.value = usuariosSeleccionados.value + usuario
        } else {
            usuariosSeleccionados.value = usuariosSeleccionados.value.filter { it.id != usuario.id }
        }
    }

    fun eliminarUsuario(index: Int) {
        usuariosSeleccionados.value = usuariosSeleccionados.value.filterIndexed { i, _ -> i != index }
    }

    fun seleccionarFecha(fecha: String) {
        if (fechasSeleccionadas.value.contains(fecha)) {
            fechasSeleccionadas.value = fechasSeleccionadas.value.filter { it != fecha }
        } else {
            fechasSeleccionadas.value = fechasSeleccionadas.value + fecha
        }
    }

    // Método para limpiar todos los campos
    fun limpiarCampos() {
        nombreUsuario.value = ""
        horaInicio.value = "00:00"
        horaFin.value = "00:00"
        ubicacion.value = ""
        descripcion.value = ""
        frecuencia.value = ""
        usuariosSeleccionados.value = emptyList()
        fechasSeleccionadas.value = emptyList()
    }

    // Método para guardar el calendario
    fun guardarCalendario(): Calendario {
        return try {
            Calendario(
                id = 0,
                nombre = nombreUsuario.value,
                horaInicio = horaInicio.value,
                horaFin = horaFin.value,
                ubicacion = ubicacion.value,
                descripcion = descripcion.value,
                frecuencia = frecuencia.value,
                usuarios = usuariosSeleccionados.value,
                fechas = fechasSeleccionadas.value
            )
        } catch (e: Exception) {
            Calendario(
                id = 0,
                nombre = "",
                horaInicio = "00:00",
                horaFin = "00:00",
                ubicacion = "",
                descripcion = "",
                frecuencia = "",
                usuarios = emptyList(),
                fechas = emptyList()
            )
        }
    }
} 