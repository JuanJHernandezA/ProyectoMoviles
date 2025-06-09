package com.example.calendapp.administrador

import java.util.*

data class Horario(
    val documentId: String = "",
    val descripcion: String = "",
    val empleadoId: String = "",
    val fecha: Date? = null,
    val fechas: List<String> = emptyList(),
    val horaInicio: String = "",
    val horaFin: String = "",
    val ubicacion: String = "",
    val empleado: String = ""
)
