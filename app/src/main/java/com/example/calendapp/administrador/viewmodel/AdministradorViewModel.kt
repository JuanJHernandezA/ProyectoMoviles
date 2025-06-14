package com.example.calendapp.administrador.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calendapp.administrador.model.Horario
import com.example.calendapp.administrador.model.WeatherInfo
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class AdministradorViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    
    var horarios by mutableStateOf<List<Horario>>(emptyList())
        private set
    
    var weatherInfo by mutableStateOf<WeatherInfo?>(null)
        private set
    
    var selectedDate by mutableStateOf("")
        private set
    
    var loading by mutableStateOf(true)
        private set
    
    var errorMessage by mutableStateOf<String?>(null)
        private set
    
    var showDeleteConfirmation by mutableStateOf(false)
        private set
    
    var horarioToDelete by mutableStateOf<Horario?>(null)
        private set
    
    var showEditDialog by mutableStateOf(false)
        private set
    
    var horarioToEdit by mutableStateOf<Horario?>(null)
        private set
    
    var editedDescripcion by mutableStateOf("")
        private set
    
    var editedHoraInicio by mutableStateOf("")
        private set
    
    var editedHoraFin by mutableStateOf("")
        private set
    
    var editedUbicacion by mutableStateOf("")
        private set

    init {
        // Set today's date as default
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1 // Calendar.MONTH is 0-based
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        selectedDate = String.format("%d-%02d-%02d", year, month, day)
        loadHorarios()
    }

    fun updateSelectedDate(date: String) {
        selectedDate = date
        loadHorarios()
    }

    fun loadHorarios() {
        viewModelScope.launch {
            loading = true
            errorMessage = null
            try {
                val snapshot = db.collection("horarios").get().await()
                val fetchedHorarios = snapshot.documents.mapNotNull { doc ->
                    runCatching {
                        val fechasArray = (doc.get("fechas") as? List<*>)?.mapNotNull { it.toString() } ?: emptyList()
                        Horario(
                            documentId = doc.id,
                            descripcion = doc.getString("descripcion") ?: "",
                            empleadoId = doc.getString("empleadoId") ?: "",
                            fecha = doc.getTimestamp("fecha")?.toDate(),
                            fechas = fechasArray,
                            horaInicio = doc.getString("horaInicio") ?: "",
                            horaFin = doc.getString("horaFin") ?: "",
                            ubicacion = doc.getString("ubicacion") ?: "",
                            empleado = doc.getString("empleado") ?: ""
                        )
                    }.getOrNull()
                }.filter { horario ->
                    horario.fechas.contains(selectedDate)
                }
                horarios = fetchedHorarios
            } catch (e: Exception) {
                errorMessage = "Error al cargar horarios: ${e.localizedMessage}"
                horarios = emptyList()
            } finally {
                loading = false
            }
        }
    }

    fun loadWeatherInfo() {
        viewModelScope.launch {
            try {
                val apiKey = "304a09c76ebe4314ab0231049252105"
                val city = "Tulua"
                val response = withContext(Dispatchers.IO) {
                    URL("https://api.weatherapi.com/v1/current.json?key=$apiKey&q=$city&lang=es").readText()
                }
                val jsonObj = JSONObject(response)
                val current = jsonObj.getJSONObject("current")
                val temp = current.getDouble("temp_c")
                weatherInfo = WeatherInfo(temperature = temp.toInt(), city = city)
            } catch (e: Exception) {
                weatherInfo = null
            }
        }
    }

    fun showDeleteConfirmation(horario: Horario) {
        horarioToDelete = horario
        showDeleteConfirmation = true
    }

    fun hideDeleteConfirmation() {
        showDeleteConfirmation = false
        horarioToDelete = null
    }

    fun deleteHorario(context: Context) {
        horarioToDelete?.let { horario ->
            db.collection("horarios").document(horario.documentId)
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(context, "Horario eliminado", Toast.LENGTH_SHORT).show()
                    loadHorarios()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Error al eliminar: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
        hideDeleteConfirmation()
    }

    fun showEditDialog(horario: Horario) {
        horarioToEdit = horario
        editedDescripcion = horario.descripcion
        editedHoraInicio = horario.horaInicio
        editedHoraFin = horario.horaFin
        editedUbicacion = horario.ubicacion
        showEditDialog = true
    }

    fun hideEditDialog() {
        showEditDialog = false
        horarioToEdit = null
    }

    fun updateHorario(context: Context) {
        horarioToEdit?.let { horario ->
            val updates = hashMapOf<String, Any>(
                "descripcion" to editedDescripcion,
                "horaInicio" to editedHoraInicio,
                "horaFin" to editedHoraFin,
                "ubicacion" to editedUbicacion
            )
            
            db.collection("horarios").document(horario.documentId)
                .update(updates)
                .addOnSuccessListener {
                    Toast.makeText(context, "Horario actualizado", Toast.LENGTH_SHORT).show()
                    loadHorarios()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Error al actualizar: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
        hideEditDialog()
    }

    fun updateEditedFields(
        descripcion: String? = null,
        horaInicio: String? = null,
        horaFin: String? = null,
        ubicacion: String? = null
    ) {
        descripcion?.let { editedDescripcion = it }
        horaInicio?.let { editedHoraInicio = it }
        horaFin?.let { editedHoraFin = it }
        ubicacion?.let { editedUbicacion = it }
    }
} 