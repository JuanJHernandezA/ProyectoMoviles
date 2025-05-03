package com.example.calendapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.calendapp.ui.theme.CalendappTheme
import com.example.calendapp.agregar_calendario.NuevoHorarioScreen // Importamos NuevoHorarioScreen
import com.example.calendapp.notificaciones.NotificationCenter

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()  // Configura la interfaz para usar el modo edge-to-edge
        setContent {
            CalendappTheme {
                NuevoHorarioScreen() // Mostrar la pantalla de agregar calendario
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CalendappTheme {
        //NotificationCenter()
        NuevoHorarioScreen() // Previsualizamos la pantalla de agregar calendario
    }
}