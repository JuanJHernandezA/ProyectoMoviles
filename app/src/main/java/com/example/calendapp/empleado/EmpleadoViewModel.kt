package com.example.calendapp.empleado

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.calendapp.administrador.Horario

data class EmpleadoState(
    val horarios: List<Horario> = emptyList(),
    val selectedDate: String = "",
    val currentTime: String = "",
    val weatherInfo: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class EmpleadoViewModel : ViewModel() {
    private val _state = MutableStateFlow(EmpleadoState())
    val state: StateFlow<EmpleadoState> = _state.asStateFlow()
    private val model = EmpleadoModel()

    init {
        _state.value = _state.value.copy(selectedDate = model.getCurrentDate())
        startTimeUpdates()
        startWeatherUpdates()
    }

    private fun startTimeUpdates() {
        viewModelScope.launch {
            while (true) {
                _state.value = _state.value.copy(currentTime = model.getCurrentTime())
                delay(1000)
            }
        }
    }

    private fun startWeatherUpdates() {
        viewModelScope.launch {
            while (true) {
                _state.value = _state.value.copy(weatherInfo = model.getWeatherInfo())
                delay(60000) // Update weather every minute
            }
        }
    }

    fun updateSelectedDate(date: String) {
        _state.value = _state.value.copy(selectedDate = date)
        loadHorarios()
    }

    fun loadHorarios() {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true, error = null)
                val horarios = model.getHorarios(_state.value.selectedDate)
                _state.value = _state.value.copy(
                    horarios = horarios,
                    isLoading = false
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "Error al cargar horarios: ${e.localizedMessage}"
                )
            }
        }
    }
} 