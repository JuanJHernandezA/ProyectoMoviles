package com.example.calendapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.calendapp.login.LoginScreen
import com.example.calendapp.login.LoginViewModel
import com.example.calendapp.administrador.AdministradorScreen
import com.example.calendapp.deleteUser.DeleteUserScreen
import com.example.calendapp.empleado.EmpleadoScreen
import com.example.calendapp.agregar_calendario.view.AgregarCalendarioUI
import com.example.calendapp.agregar_calendario.viewmodel.AgregarCalendarioViewModel
import com.example.calendapp.notificaciones.view.NotificacionesUI
import com.example.calendapp.config.UserViewModel
import com.example.calendapp.deleteUser.ConfirmadoEliminarScreen
import com.example.calendapp.deleteUser.ConfirmarEliminarScreen
import com.example.calendapp.editar_usuario.EditUserScreen
import com.example.calendapp.registerUser.AddUserScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                val viewModel = remember { AgregarCalendarioViewModel() }
                val userViewModel = remember { UserViewModel() }

                NavHost(navController, startDestination = "login") {
                    composable("login") { 
                        val loginViewModel: LoginViewModel = viewModel()
                        val loginState by loginViewModel.loginState.collectAsState()
                        
                        LoginScreen(
                            viewModel = loginViewModel,
                            onLoginSuccess = { 
                                userViewModel.updateUser(
                                    loginState.userName,
                                    loginState.userRole,
                                    loginState.cedula
                                )
                                when (loginState.userRole) {
                                    "admin" -> navController.navigate("administrador")
                                    "usuario" -> navController.navigate("empleado")
                                    else -> {
                                        userViewModel.updateUser("", "", "")
                                        navController.navigate("login") {
                                            popUpTo("login") { inclusive = true }
                                        }
                                    }
                                }

                            })
                        
                    }
                    composable("administrador") {
                        AdministradorScreen(navController)
                    }
                    composable("notificaciones") {
                        NotificacionesUI(
                            navController = navController,
                            userViewModel = userViewModel
                        )
                    }
                    composable("agregar_usuario") {
                        AddUserScreen(
                            onUserCreated = { navController.navigateUp() },
                            onBackClick = { navController.navigateUp() }
                        )
                    }
                    composable("agregar_turno") {
                        AgregarCalendarioUI(
                            navController = navController,
                            viewModel = viewModel,
                            userViewModel = userViewModel
                        )
                    }
                    composable("eliminar_usuario") {
                        DeleteUserScreen(navController)
                    }
                    composable("confirmar_eliminar/{email}") { backStackEntry ->
                        val email = backStackEntry.arguments?.getString("email") ?: ""
                        ConfirmarEliminarScreen(navController, email)
                    }
                    composable("confirmado_eliminar") {
                        ConfirmadoEliminarScreen(navController)
                    }
                    composable("editar_perfil") {
                        EditUserScreen(
                            onBackClick = { navController.navigateUp() }
                        )
                    }
                    composable("empleado") {
                        EmpleadoScreen(navController)
                    }
                }
            }
        }
    }
}
