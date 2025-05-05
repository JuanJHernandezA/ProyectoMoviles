package com.example.calendapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.calendapp.agregar_calendario.FrecuenciaDialog
import com.example.calendapp.ui.theme.CalendappTheme
import com.example.calendapp.agregar_calendario.NuevoHorarioScreen
import com.example.calendapp.notificaciones.CentroNotificacionesScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalendappTheme {
                NuevoHorarioScreen() // Inicia con la pantalla principal
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CalendappTheme {
        //CentroNotificacionesScreen()
        NuevoHorarioScreen()
        //FrecuenciaDialog()
    }
}