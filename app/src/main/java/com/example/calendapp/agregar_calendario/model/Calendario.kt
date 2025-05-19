package com.example.calendapp.agregar_calendario.model

data class Calendario(
    val id: Int = 0,
    val nombre: String = "",
    val horaInicio: String = "",
    val horaFin: String = "",
    val ubicacion: String = "",
    val descripcion: String = "",
    val frecuencia: String = "",
    val usuarios: List<Usuario> = emptyList(),
    val fechas: List<String> = emptyList()
) 