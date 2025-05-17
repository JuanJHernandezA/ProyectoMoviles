package com.example.calendapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.calendapp.agregar_calendario.view.AgregarCalendarioUI
import com.example.calendapp.agregar_calendario.view.FrecuenciaDialog
import com.example.calendapp.agregar_calendario.view.PersonalizacionFrecuenciaDialog
import com.example.calendapp.agregar_calendario.viewmodel.AgregarCalendarioViewModel
import com.example.calendapp.notificaciones.view.NotificacionesUI
import com.example.calendapp.ui.theme.CalendappTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalendappTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val viewModel: AgregarCalendarioViewModel = viewModel()
    var showFrecuenciaDialog by remember { mutableStateOf(false) }
    var showPersonalizacionDialog by remember { mutableStateOf(false) }

    NavHost(navController = navController, startDestination = "agregar_calendario") {
        composable("agregar_calendario") {
            AgregarCalendarioUI(
                navController = navController,
                viewModel = viewModel,
                onFrecuenciaClick = {
                    showFrecuenciaDialog = true
                }
            )
        }
        composable("notificaciones") {
            NotificacionesUI(navController = navController)
        }
    }

    // Diálogo de frecuencia
    if (showFrecuenciaDialog) {
        FrecuenciaDialog(
            onDismiss = { showFrecuenciaDialog = false },
            onOptionSelected = { opcion ->
                if (opcion == "Personalización...") {
                    showFrecuenciaDialog = false
                    showPersonalizacionDialog = true
                } else {
                    viewModel.actualizarFrecuencia(opcion)
                    showFrecuenciaDialog = false
                }
            }
        )
    }

    // Diálogo de personalización
    if (showPersonalizacionDialog) {
        PersonalizacionFrecuenciaDialog(
            onDismiss = { showPersonalizacionDialog = false },
            onComplete = { frecuencia ->
                viewModel.actualizarFrecuencia(frecuencia)
                showPersonalizacionDialog = false
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CalendappTheme {
        MainScreen()
    }
}