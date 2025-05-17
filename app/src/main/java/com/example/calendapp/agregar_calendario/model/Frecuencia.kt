package com.example.calendapp.agregar_calendario.model

data class Frecuencia(
    val tipo: String = "No se repite",
    val repeticionCada: Int = 1,
    val periodo: String = "Días",
    val diasSeleccionados: List<Boolean> = List(7) { false },
    val finalizaDespuesDe: Int = 0,
    val fechaFinalizacion: String = ""
) 