package com.example.calendapp.administrador.model

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

data class WeatherInfo(
    val temperature: Int = 0,
    val city: String = "Tuluá"
)

data class EventoConPosicion(
    
    val horario: Horario,
    val columna: Int,
    val totalColumnas: Int
) 