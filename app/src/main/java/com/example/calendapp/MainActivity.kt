package com.example.calendapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.calendapp.ui.theme.CalendappTheme
import com.example.calendapp.notificaciones.NotificationCenter // Importamos el composable NotificationCenter

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()  // Configura la interfaz para usar el modo edge-to-edge
        setContent {
            // Aquí puedes usar el NotificationCenter
            CalendappTheme {
                NotificationCenter() // Mostrar las notificaciones
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CalendappTheme {
        // Previsualizamos el NotificationCenter
        NotificationCenter()
    }
}