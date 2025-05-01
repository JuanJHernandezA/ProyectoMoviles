package com.example.calendapp.registerUser

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.calendapp.viewmodel.AddUserViewModel
import com.example.calendapp.viewmodel.AddUserUiState

@Composable
fun AddUserScreen(
    viewModel: AddUserViewModel = viewModel(),
    onUserCreated: () -> Unit
) {
    val userState by viewModel.userState.collectAsStateWithLifecycle()

    LaunchedEffect(userState.isSuccess) {
        if (userState.isSuccess) {
            onUserCreated()
            viewModel.resetState()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1C23))
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Registro de Usuario",
                fontSize = 24.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            // Campo de Cédula
            OutlinedTextField(
                value = userState.user.cedula,
                onValueChange = { viewModel.onFieldChanged("cedula", it) },
                label = { Text("Cédula", color = Color.White) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF0077FF),
                    unfocusedBorderColor = Color.Gray,
                    cursorColor = Color.White
                ),
                enabled = !userState.isLoading
            )

            // Campo de Nombre
            OutlinedTextField(
                value = userState.user.nombre,
                onValueChange = { viewModel.onFieldChanged("nombre", it) },
                label = { Text("Nombre", color = Color.White) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF0077FF),
                    unfocusedBorderColor = Color.Gray,
                    cursorColor = Color.White
                ),
                enabled = !userState.isLoading
            )

            // Campo de Apellido
            OutlinedTextField(
                value = userState.user.apellido,
                onValueChange = { viewModel.onFieldChanged("apellido", it) },
                label = { Text("Apellido", color = Color.White) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF0077FF),
                    unfocusedBorderColor = Color.Gray,
                    cursorColor = Color.White
                ),
                enabled = !userState.isLoading
            )

            // Campo de Género
            OutlinedTextField(
                value = userState.user.genero,
                onValueChange = { viewModel.onFieldChanged("genero", it) },
                label = { Text("Género", color = Color.White) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF0077FF),
                    unfocusedBorderColor = Color.Gray,
                    cursorColor = Color.White
                ),
                enabled = !userState.isLoading
            )

            // Campo de Edad
            OutlinedTextField(
                value = userState.user.edad,
                onValueChange = { viewModel.onFieldChanged("edad", it) },
                label = { Text("Edad", color = Color.White) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF0077FF),
                    unfocusedBorderColor = Color.Gray,
                    cursorColor = Color.White
                ),
                enabled = !userState.isLoading
            )

            // Campo de Teléfono
            OutlinedTextField(
                value = userState.user.telefono,
                onValueChange = { viewModel.onFieldChanged("telefono", it) },
                label = { Text("Teléfono", color = Color.White) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF0077FF),
                    unfocusedBorderColor = Color.Gray,
                    cursorColor = Color.White
                ),
                enabled = !userState.isLoading
            )

            // Campo de Rol
            OutlinedTextField(
                value = userState.user.rol,
                onValueChange = { viewModel.onFieldChanged("rol", it) },
                label = { Text("Rol", color = Color.White) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF0077FF),
                    unfocusedBorderColor = Color.Gray,
                    cursorColor = Color.White
                ),
                enabled = !userState.isLoading
            )

            // Campo de Correo
            OutlinedTextField(
                value = userState.user.correo,
                onValueChange = { viewModel.onFieldChanged("correo", it) },
                label = { Text("Correo", color = Color.White) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF0077FF),
                    unfocusedBorderColor = Color.Gray,
                    cursorColor = Color.White
                ),
                enabled = !userState.isLoading
            )

            // Campo de Contraseña
            OutlinedTextField(
                value = userState.user.contrasena,
                onValueChange = { viewModel.onFieldChanged("contrasena", it) },
                label = { Text("Contraseña", color = Color.White) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF0077FF),
                    unfocusedBorderColor = Color.Gray,
                    cursorColor = Color.White
                ),
                visualTransformation = PasswordVisualTransformation(),
                enabled = !userState.isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de Registrar
            Button(
                onClick = { viewModel.createUser() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF004080)
                ),
                shape = RoundedCornerShape(8.dp),
                enabled = !userState.isLoading
            ) {
                if (userState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White
                    )
                } else {
                    Text("Registrar Usuario", color = Color.White, fontSize = 16.sp)
                }
            }

            userState.error?.let { error ->
                Text(
                    text = error,
                    color = Color.Red,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AddUserScreenPreview() {
    AddUserScreen(
        viewModel = AddUserViewModel(),
        onUserCreated = {}
    )
} 