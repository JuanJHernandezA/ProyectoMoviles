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


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val navController = rememberNavController()



                NavHost(navController, startDestination = "login") {
                    composable("login") { LoginScreen(onLoginSuccess = { navController.navigate("administrador") }) }
                    composable("administrador") {
                        AdministradorScreen(navController)

                    }
                    composable("notificaciones") {

                    }
                    composable("agregar_usuario") {

                    }
                    composable("agregar_turno") {
                    }
                    composable("eliminar_usuario") {
                        DeleteUserScreen(navController)
                    }
                    composable("editar_perfil") {
                    }
                    composable("empleado") {

                    }




                }
            }
        }
    }}
