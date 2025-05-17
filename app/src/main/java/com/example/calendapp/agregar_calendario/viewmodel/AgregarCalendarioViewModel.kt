package com.example.calendapp.agregar_calendario.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.calendapp.agregar_calendario.model.Calendario
import com.example.calendapp.agregar_calendario.model.UsuarioSugerido
import com.example.calendapp.agregar_calendario.model.UsuariosSugeridosEjemplo

class AgregarCalendarioViewModel : ViewModel() {
    // Estado para los campos de entrada
    val nombreUsuario = mutableStateOf("")
    val horaInicio = mutableStateOf("Inicio")
    val horaFin = mutableStateOf("Fin")
    val ubicacion = mutableStateOf("")
    val descripcion = mutableStateOf("")
    val frecuencia = mutableStateOf("")

    // Estado para las ventanas emergentes
    val mostrarVentanaSugerencias = mutableStateOf(false)
    val mostrarVentanaAsignados = mutableStateOf(false)

    // Listas de usuarios
    val usuariosSeleccionados = mutableStateListOf<UsuarioSugerido>()
    val usuariosSugeridos = UsuariosSugeridosEjemplo.lista

    init {
        // Inicialización de valores por defecto
        limpiarCampos()
    }

    // Métodos para actualizar el estado
    fun actualizarNombreUsuario(nuevoNombre: String) {
        try {
            nombreUsuario.value = nuevoNombre
            mostrarVentanaSugerencias.value = nuevoNombre.isNotEmpty()
        } catch (e: Exception) {
            // Manejo de error silencioso
        }
    }

    fun actualizarHoraInicio(nuevaHora: String) {
        try {
            horaInicio.value = nuevaHora
        } catch (e: Exception) {
            // Manejo de error silencioso
        }
    }

    fun actualizarHoraFin(nuevaHora: String) {
        try {
            horaFin.value = nuevaHora
        } catch (e: Exception) {
            // Manejo de error silencioso
        }
    }

    fun actualizarUbicacion(nuevaUbicacion: String) {
        try {
            ubicacion.value = nuevaUbicacion
        } catch (e: Exception) {
            // Manejo de error silencioso
        }
    }

    fun actualizarDescripcion(nuevaDescripcion: String) {
        try {
            descripcion.value = nuevaDescripcion
        } catch (e: Exception) {
            // Manejo de error silencioso
        }
    }

    fun actualizarFrecuencia(nuevaFrecuencia: String) {
        try {
            frecuencia.value = nuevaFrecuencia
        } catch (e: Exception) {
            // Manejo de error silencioso
        }
    }

    // Métodos para manejar usuarios
    fun seleccionarUsuario(index: Int) {
        try {
            if (index in usuariosSugeridos.indices) {
                val usuario = usuariosSugeridos[index]
                if (!usuariosSeleccionados.contains(usuario)) {
                    usuariosSeleccionados.add(usuario)
                } else {
                    usuariosSeleccionados.remove(usuario)
                }
                // Ya no cerramos la ventana de sugerencias
                // mostrarVentanaSugerencias.value = false
            }
        } catch (e: Exception) {
            // Manejo de error silencioso
        }
    }

    fun eliminarUsuario(index: Int) {
        try {
            if (index in usuariosSeleccionados.indices) {
                usuariosSeleccionados.removeAt(index)
            }
        } catch (e: Exception) {
            // Manejo de error silencioso
        }
    }

    // Método para limpiar todos los campos
    fun limpiarCampos() {
        try {
            nombreUsuario.value = ""
            horaInicio.value = "Inicio"
            horaFin.value = "Fin"
            ubicacion.value = ""
            descripcion.value = ""
            frecuencia.value = ""
            usuariosSeleccionados.clear()
        } catch (e: Exception) {
            // Manejo de error silencioso
        }
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
                usuarios = usuariosSeleccionados
            )
        } catch (e: Exception) {
            Calendario(
                id = 0,
                nombre = "",
                horaInicio = "Inicio",
                horaFin = "Fin",
                ubicacion = "",
                descripcion = "",
                frecuencia = "",
                usuarios = emptyList()
            )
        }
    }
} 