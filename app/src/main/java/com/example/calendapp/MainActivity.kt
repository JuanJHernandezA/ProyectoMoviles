package com.example.calendapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.navigation.compose.NavHost
import com.example.calendapp.login.Login
import com.example.calendapp.login.LoginScreen
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.calendapp.administrador.AdministradorScreen
import com.example.calendapp.deleteUser.DeleteUserScreen
import com.example.calendapp.empleado.EmpleadoScreen
import com.example.calendapp.agregar_calendario.view.AgregarCalendarioUI
import com.example.calendapp.agregar_calendario.view.FrecuenciaDialog
import com.example.calendapp.agregar_calendario.view.PersonalizacionFrecuenciaDialog
import com.example.calendapp.agregar_calendario.viewmodel.AgregarCalendarioViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.calendapp.notificaciones.view.NotificacionesUI

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                val showFrecuenciaDialog = remember { mutableStateOf(false) }
                val showPersonalizacionDialog = remember { mutableStateOf(false) }
                val viewModel = remember { AgregarCalendarioViewModel() }

                NavHost(navController, startDestination = "login") {
                    composable("login") { LoginScreen(onLoginSuccess = { navController.navigate("administrador") }) }
                    composable("administrador") {
                        AdministradorScreen(navController)
                    }
                    composable("notificaciones") {
                        NotificacionesUI(navController)
                    }
                    composable("agregar_usuario") {
                        // AgregarUsuarioScreen(navController)
                    }
                    composable("agregar_turno") {
                        AgregarCalendarioUI(
                            navController = navController,
                            viewModel = viewModel,
                            onFrecuenciaClick = {
                                showFrecuenciaDialog.value = true
                            }
                        )

                        if (showFrecuenciaDialog.value) {
                            FrecuenciaDialog(
                                onDismiss = { showFrecuenciaDialog.value = false },
                                onOptionSelected = { opcion ->
                                    showFrecuenciaDialog.value = false
                                    if (opcion == "Personalización...") {
                                        showPersonalizacionDialog.value = true
                                    } else {
                                        viewModel.actualizarFrecuencia(opcion)
                                    }
                                }
                            )
                        }

                        if (showPersonalizacionDialog.value) {
                            PersonalizacionFrecuenciaDialog(
                                onDismiss = { showPersonalizacionDialog.value = false },
                                onComplete = { frecuencia ->
                                    showPersonalizacionDialog.value = false
                                    viewModel.actualizarFrecuencia(frecuencia)
                                }
                            )
                        }
                    }
                    composable("eliminar_usuario") {
                        DeleteUserScreen(navController)
                    }
                    composable("editar_perfil") {
                        // EditarPerfilScreen(navController)
                    }
                    composable("empleado") {
                        EmpleadoScreen(navController)
                    }
                }
            }
        }
    }
}
