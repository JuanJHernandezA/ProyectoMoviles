package com.example.calendapp.agregar_calendario.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class FrecuenciaViewModel : ViewModel() {
    // Estado para el tipo de frecuencia
    val tipoFrecuencia = mutableStateOf("")

    // Estado para la repetición
    val repeticionCada = mutableStateOf("")
    val periodo = mutableStateOf("Días")

    // Estado para los días seleccionados
    val diasSeleccionados = mutableStateListOf(*Array(7) { false })

    // Estado para la finalización
    val tipoFinalizacion = mutableStateOf("Nunca")
    val finalizaDespuesDe = mutableStateOf("")
    val fechaFinalizacion = mutableStateOf("")

    // Métodos para actualizar el estado
    fun actualizarTipoFrecuencia(tipo: String) {
        tipoFrecuencia.value = tipo
    }

    fun actualizarRepeticionCada(valor: String) {
        repeticionCada.value = valor
    }

    fun actualizarPeriodo(periodoSeleccionado: String) {
        periodo.value = periodoSeleccionado
    }

    fun toggleDiaSeleccionado(index: Int) {
        diasSeleccionados[index] = !diasSeleccionados[index]
    }

    fun actualizarTipoFinalizacion(tipo: String) {
        tipoFinalizacion.value = tipo
    }

    fun actualizarFinalizaDespuesDe(valor: String) {
        finalizaDespuesDe.value = valor
    }

    fun actualizarFechaFinalizacion(fecha: String) {
        fechaFinalizacion.value = fecha
    }

    // Método para obtener la frecuencia como string
    fun obtenerFrecuencia(): String {
        val repeticion = if (repeticionCada.value.isNotEmpty()) "Cada ${repeticionCada.value} ${periodo.value.lowercase()}" else ""
        val dias = if (diasSeleccionados.any { it }) {
            val diasSeleccionadosStr = diasSeleccionados.mapIndexed { index, seleccionado ->
                if (seleccionado) when (index) {
                    0 -> "Lunes"
                    1 -> "Martes"
                    2 -> "Miércoles"
                    3 -> "Jueves"
                    4 -> "Viernes"
                    5 -> "Sábado"
                    6 -> "Domingo"
                    else -> ""
                } else ""
            }.filter { it.isNotEmpty() }.joinToString(", ")
            if (diasSeleccionadosStr.isNotEmpty()) " los $diasSeleccionadosStr" else ""
        } else ""

        val finalizacion = when (tipoFinalizacion.value) {
            "Nunca" -> ""
            "El dd/mm/aaaa" -> if (fechaFinalizacion.value.isNotEmpty()) " hasta el ${fechaFinalizacion.value}" else ""
            "Despues de # repeticion" -> if (finalizaDespuesDe.value.isNotEmpty()) " durante ${finalizaDespuesDe.value} repeticiones" else ""
            else -> ""
        }

        return "$repeticion$dias$finalizacion".trim()
    }

    // Método para limpiar todos los campos
    fun limpiarCampos() {
        tipoFrecuencia.value = ""
        repeticionCada.value = ""
        periodo.value = "Días"
        diasSeleccionados.replaceAll { false }
        tipoFinalizacion.value = "Nunca"
        finalizaDespuesDe.value = ""
        fechaFinalizacion.value = ""
    }
} 