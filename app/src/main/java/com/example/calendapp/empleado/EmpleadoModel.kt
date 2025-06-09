package com.example.calendapp.empleado

import android.util.Log
import com.example.calendapp.administrador.Horario
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class EmpleadoModel {
    private val db = FirebaseFirestore.getInstance()
    private val TAG = "EmpleadoModel"

    suspend fun getHorarios(selectedDate: String): List<Horario> {
        Log.d(TAG, "Buscando horarios para fecha: $selectedDate")
        
        val snapshot = db.collection("horarios").get().await()
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("America/Bogota")
        }

        Log.d(TAG, "Documentos encontrados: ${snapshot.documents.size}")

        val fetchedHorarios = snapshot.documents.mapNotNull { doc ->
            runCatching {
                val fechasArray = (doc.get("fechas") as? List<*>)?.mapNotNull { it.toString() } ?: emptyList()
                val fechaTimestamp = doc.getTimestamp("fecha")?.toDate()
                val fechaFormateada = fechaTimestamp?.let { dateFormatter.format(it) }
                
                Log.d(TAG, "Procesando documento:")
                Log.d(TAG, "- fechas array: $fechasArray")
                Log.d(TAG, "- fecha timestamp: $fechaFormateada")
                
                Horario(
                    descripcion = doc.getString("descripcion") ?: "",
                    empleadoId = doc.getString("empleadoId") ?: "",
                    fecha = fechaTimestamp,
                    fechas = fechasArray,
                    horaInicio = doc.getString("horaInicio") ?: "",
                    horaFin = doc.getString("horaFin") ?: "",
                    ubicacion = doc.getString("ubicacion") ?: "",
                    empleado = doc.getString("empleado") ?: ""
                )
            }.getOrNull()
        }

        Log.d(TAG, "Horarios procesados: ${fetchedHorarios.size}")

        val filteredHorarios = fetchedHorarios.filter { horario ->
            // Verificar si la fecha seleccionada está en la lista de fechas
            val matchesFechas = horario.fechas.any { fecha ->
                Log.d(TAG, "Comparando fecha en array: $fecha con $selectedDate")
                fecha == selectedDate
            }
            
            // Verificar si la fecha del horario coincide con la fecha seleccionada
            val matchesFecha = horario.fecha?.let { fecha ->
                val fechaFormateada = dateFormatter.format(fecha)
                Log.d(TAG, "Comparando fecha timestamp: $fechaFormateada con $selectedDate")
                fechaFormateada == selectedDate
            } ?: false

            // Solo retornar true si la fecha está en el array de fechas
            val matches = matchesFechas
            if (matches) {
                Log.d(TAG, "Horario encontrado para fecha $selectedDate:")
                Log.d(TAG, "- Descripción: ${horario.descripcion}")
                Log.d(TAG, "- Hora inicio: ${horario.horaInicio}")
                Log.d(TAG, "- Hora fin: ${horario.horaFin}")
                Log.d(TAG, "- Fechas: ${horario.fechas}")
                Log.d(TAG, "- Fecha timestamp: ${horario.fecha?.let { dateFormatter.format(it) }}")
            }
            matches
        }

        Log.d(TAG, "Horarios filtrados para fecha $selectedDate: ${filteredHorarios.size}")
        filteredHorarios.forEach { horario ->
            Log.d(TAG, "Horario final: ${horario.descripcion} - ${horario.horaInicio} a ${horario.horaFin}")
        }

        return filteredHorarios
    }

    suspend fun getWeatherInfo(): String = withContext(Dispatchers.IO) {
        try {
            val apiKey = "304a09c76ebe4314ab0231049252105"
            val city = "Tulua"
            val url = "https://api.weatherapi.com/v1/current.json?key=$apiKey&q=$city&lang=es&aqi=no"
            
            Log.d(TAG, "Intentando obtener clima de: $url")
            
            val connection = java.net.URL(url).openConnection() as java.net.HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 5000
            connection.readTimeout = 5000
            
            val responseCode = connection.responseCode
            Log.d(TAG, "Código de respuesta del clima: $responseCode")
            
            if (responseCode == 200) {
                val response = connection.inputStream.bufferedReader().use { it.readText() }
                val jsonObj = org.json.JSONObject(response)
                val current = jsonObj.getJSONObject("current")
                val temp = current.getDouble("temp_c")
                Log.d(TAG, "Clima obtenido exitosamente: $temp°C")
                "Tulua: ${temp.toInt()}°C"
            } else {
                Log.e(TAG, "Error al obtener el clima. Código: $responseCode")
                "No se pudo cargar el clima"
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error al obtener el clima: ${e.message}", e)
            "No se pudo cargar el clima"
        }
    }

    fun getCurrentTime(): String {
        val timeFormatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("America/Bogota")
        }
        return timeFormatter.format(Date())
    }

    fun getCurrentDate(): String {
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("America/Bogota")
        }
        return dateFormatter.format(Date())
    }
} 