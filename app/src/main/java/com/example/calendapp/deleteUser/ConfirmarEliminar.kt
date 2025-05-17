package com.example.calendapp.deleteUser

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ConfirmarEliminarScreen(
    navController: NavController,
    email: String,
    viewModel: DeleteUserViewModel = viewModel()
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("¿Estás seguro que deseas eliminar este empleado?")

            Spacer(modifier = Modifier.height(24.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(onClick = {
                    viewModel.deleteUserByEmail(
                        email = email,
                        onSuccess = { navController.navigate("confirmado_eliminar") },
                        onFailure = { /* manejar error */ }
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
