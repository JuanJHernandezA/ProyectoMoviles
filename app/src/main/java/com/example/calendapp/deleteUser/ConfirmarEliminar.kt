package com.example.calendapp.deleteUser

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ConfirmarEliminarScreen(
    navController: NavController,
    email: String,

    viewModel: DeleteUserViewModel = viewModel()
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1C23)),
        contentAlignment = Alignment.Center // Centra todo el contenido dentro del Box
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "¿Estás seguro que deseas eliminar este empleado?",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(onClick = {

                    viewModel.deleteUserByEmail(
                        email = email,

                        onSuccess = { navController.navigate("confirmado_eliminar") },
                        onFailure = { }
                    )
                }) {
                    Text("Sí")
                }

                Button(onClick = { navController.popBackStack() }) {
                    Text("No")
                }
            }
        }
    }
}
