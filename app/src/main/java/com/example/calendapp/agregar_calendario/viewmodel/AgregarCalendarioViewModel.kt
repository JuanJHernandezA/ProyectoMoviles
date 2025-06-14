package com.example.calendapp.agregar_calendario.viewmodel

import android.util.Log
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
import java.text.SimpleDateFormat
import java.util.*

class AgregarCalendarioViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    
    // Estado para los campos de entrada
    val nombreUsuario = mutableStateOf("")
    val horaInicio = mutableStateOf("00:00")
    val horaFin = mutableStateOf("00:00")
    val ubicacion = mutableStateOf("")
    val descripcion = mutableStateOf("")
    val fechasSeleccionadas = mutableStateOf<List<String>>(emptyList())

    // Estado para las ventanas emergentes
    val mostrarVentanaSugerencias = mutableStateOf(false)
    val mostrarVentanaAsignados = mutableStateOf(false)
    val mostrarCalendario = mutableStateOf(false)
    val mostrarErrores = mutableStateOf(false)

    // Estado para mensajes
    val mensajeError = mutableStateOf<String?>(null)
    val mensajeExito = mutableStateOf<String?>(null)

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
                    val cedula = doc.getString("cedula") ?: return@mapNotNull null
                    
                    Usuario(
                        id = doc.id,
                        nombre = nombre,
                        apellido = apellido,
                        rol = rol,
                        cedula = cedula
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
        // Validar que la hora de fin no sea anterior a la hora de inicio
        if (horaFin.value != "00:00" && horaFin.value < nuevaHora) {
            mensajeError.value = "La hora de finalización no puede ser anterior a la hora de inicio"
        } else {
            mensajeError.value = null
        }
    }

    fun actualizarHoraFin(nuevaHora: String) {
        if (horaInicio.value == "00:00") {
            mensajeError.value = "Debe ingresar primero la hora de inicio"
            return
        }
        if (nuevaHora < horaInicio.value) {
            mensajeError.value = "La hora de finalización no puede ser anterior a la hora de inicio"
            return
        }
        horaFin.value = nuevaHora
        mensajeError.value = null
    }

    fun actualizarUbicacion(nuevaUbicacion: String) {
        ubicacion.value = nuevaUbicacion
    }

    fun actualizarDescripcion(nuevaDescripcion: String) {
        descripcion.value = nuevaDescripcion
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
        usuariosSeleccionados.value = emptyList()
        fechasSeleccionadas.value = emptyList()
    }

    // Método para guardar el calendario
    fun guardarCalendario() {
        mostrarErrores.value = true
        viewModelScope.launch {
            try {
                // Validar campos requeridos
                if (nombreUsuario.value.isBlank()) {
                    mensajeError.value = "El nombre del usuario es obligatorio"
                    return@launch
                }
                if (usuariosSeleccionados.value.isEmpty()) {
                    mensajeError.value = "Debe seleccionar al menos un usuario"
                    return@launch
                }
                if (horaInicio.value == "00:00" || horaFin.value == "00:00") {
                    mensajeError.value = "Debe seleccionar una hora de inicio y fin"
                    return@launch
                }
                if (fechasSeleccionadas.value.isEmpty()) {
                    mensajeError.value = "Debe seleccionar al menos una fecha"
                    return@launch
                }
                if (ubicacion.value.isBlank()) {
                    mensajeError.value = "La ubicación es obligatoria"
                    return@launch
                }
                if (descripcion.value.isBlank()) {
                    mensajeError.value = "La descripción es obligatoria"
                    return@launch
                }

                Log.d("AgregarCalendario", "Guardando horario para fechas: ${fechasSeleccionadas.value}")

                // Crear el documento del horario
                val horarioData = hashMapOf(
                    "descripcion" to descripcion.value,
                    "horaInicio" to horaInicio.value,
                    "horaFin" to horaFin.value,
                    "ubicacion" to ubicacion.value,
                    "fecha" to com.google.firebase.Timestamp.now(),
                    "fechas" to fechasSeleccionadas.value
                )

                // Para cada usuario seleccionado, crear el horario y su notificación
                usuariosSeleccionados.value.forEach { usuario ->
                    Log.d("AgregarCalendario", "Guardando horario para usuario: ${usuario.cedula}")
                    
                    // Crear el horario para este usuario
                    val horarioRef = db.collection("horarios").document()
                    val horarioCompleto = horarioData + hashMapOf(
                        "empleadoId" to usuario.cedula,
                        "empleado" to "${usuario.nombre} ${usuario.apellido}"
                    )
                    
                    Log.d("AgregarCalendario", "Datos del horario: $horarioCompleto")
                    horarioRef.set(horarioCompleto).await()

                    // Crear la notificación para este usuario
                    val notificacionData = hashMapOf(
                        "cedulaEmpleado" to usuario.cedula,
                        "descripcion" to "Nuevo horario asignado: ${descripcion.value}",
                        "fecha" to SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date()),
                        "tipoNotificacion" to "Nuevo Horario",
                        "horaInicio" to horaInicio.value,
                        "horaFin" to horaFin.value,
                        "ubicacion" to ubicacion.value,
                        "fechas" to fechasSeleccionadas.value
                    )
                    Log.d("AgregarCalendario", "Guardando notificación: $notificacionData")
                    db.collection("notificaciones").add(notificacionData).await()
                }

                // Limpiar los campos después de guardar exitosamente
                limpiarCampos()
                mostrarErrores.value = false
                mensajeExito.value = "Horario guardado exitosamente"
                mensajeError.value = null
            } catch (e: Exception) {
                Log.e("AgregarCalendario", "Error al guardar el horario", e)
                mensajeError.value = "Error al guardar el horario: ${e.message}"
                mensajeExito.value = null
            }
        }
    }

    // Método para limpiar mensajes
    fun limpiarMensajes() {
        mensajeError.value = null
        mensajeExito.value = null
        mostrarErrores.value = false
    }
} 