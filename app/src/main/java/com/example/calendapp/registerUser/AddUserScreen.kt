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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.calendapp.R
import com.example.calendapp.login.LoginScreen
import com.example.calendapp.login.LoginViewModel
import com.example.calendapp.ui.theme.*

//@Preview()

@Composable
fun AddUserScreen(
    viewModel: AddUserViewModel = viewModel(),
    onUserCreated: () -> Unit,
    onBackClick: () -> Unit = {}
) {
    val userState by viewModel.userState.collectAsStateWithLifecycle()

    LaunchedEffect(userState.isSuccess) {
        if (userState.isSuccess) {
            onUserCreated()
            viewModel.resetState()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_back),
                    contentDescription = "Botón de regreso",
                    modifier = Modifier.size(24.dp),
                    tint = White
                )
            }
            
            Text(
                text = "Registro de Usuario",
                fontSize = 24.sp,
                color = White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        // Campo de Cédula
        OutlinedTextField(
            value = userState.user.cedula,
            onValueChange = { viewModel.onFieldChanged("cedula", it) },
            label = { Text("Cédula", color = White) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            textStyle = TextStyle(color = White),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Border,
                unfocusedBorderColor = White.copy(alpha = 0.5f),
                cursorColor = Border,
                focusedTextColor = White,
                unfocusedTextColor = White,
                focusedLabelColor = ButtonBackground,
                unfocusedLabelColor = White.copy(alpha = 0.5f)
            ),
            enabled = !userState.isLoading
        )

        // Campo de Nombre
        OutlinedTextField(
            value = userState.user.nombre,
            onValueChange = { viewModel.onFieldChanged("nombre", it) },
            label = { Text("Nombre", color = White) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            textStyle = TextStyle(color = White),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Border,
                unfocusedBorderColor = White.copy(alpha = 0.5f),
                cursorColor = Border,
                focusedTextColor = White,
                unfocusedTextColor = White,
                focusedLabelColor = ButtonBackground,
                unfocusedLabelColor = White.copy(alpha = 0.5f)
            ),
            enabled = !userState.isLoading
        )

        // Campo de Apellido
        OutlinedTextField(
            value = userState.user.apellido,
            onValueChange = { viewModel.onFieldChanged("apellido", it) },
            label = { Text("Apellido", color = White) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            textStyle = TextStyle(color = White),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Border,
                unfocusedBorderColor = White.copy(alpha = 0.5f),
                cursorColor = Border,
                focusedTextColor = White,
                unfocusedTextColor = White,
                focusedLabelColor = ButtonBackground,
                unfocusedLabelColor = White.copy(alpha = 0.5f)
            ),
            enabled = !userState.isLoading
        )

        // Campo de Género
        OutlinedTextField(
            value = userState.user.genero,
            onValueChange = { viewModel.onFieldChanged("genero", it) },
            label = { Text("Género", color = White) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            textStyle = TextStyle(color = White),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Border,
                unfocusedBorderColor = White.copy(alpha = 0.5f),
                cursorColor = Border,
                focusedTextColor = White,
                unfocusedTextColor = White,
                focusedLabelColor = ButtonBackground,
                unfocusedLabelColor = White.copy(alpha = 0.5f)
            ),
            enabled = !userState.isLoading
        )

        // Campo de Edad
        OutlinedTextField(
            value = userState.user.edad,
            onValueChange = { viewModel.onFieldChanged("edad", it) },
            label = { Text("Edad", color = White) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            textStyle = TextStyle(color = White),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Border,
                unfocusedBorderColor = White.copy(alpha = 0.5f),
                cursorColor = Border,
                focusedTextColor = White,
                unfocusedTextColor = White,
                focusedLabelColor = ButtonBackground,
                unfocusedLabelColor = White.copy(alpha = 0.5f)
            ),
            enabled = !userState.isLoading
        )

        // Campo de Teléfono
        OutlinedTextField(
            value = userState.user.telefono,
            onValueChange = { viewModel.onFieldChanged("telefono", it) },
            label = { Text("Teléfono", color = White) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            textStyle = TextStyle(color = White),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Border,
                unfocusedBorderColor = White.copy(alpha = 0.5f),
                cursorColor = Border,
                focusedTextColor = White,
                unfocusedTextColor = White,
                focusedLabelColor = ButtonBackground,
                unfocusedLabelColor = White.copy(alpha = 0.5f)
            ),
            enabled = !userState.isLoading
        )

        // Campo de Rol
        OutlinedTextField(
            value = userState.user.rol,
            onValueChange = { viewModel.onFieldChanged("rol", it) },
            label = { Text("Rol", color = White) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            textStyle = TextStyle(color = White),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Border,
                unfocusedBorderColor = White.copy(alpha = 0.5f),
                cursorColor = Border,
                focusedTextColor = White,
                unfocusedTextColor = White,
                focusedLabelColor = ButtonBackground,
                unfocusedLabelColor = White.copy(alpha = 0.5f)
            ),
            enabled = !userState.isLoading
        )

        // Campo de Correo
        OutlinedTextField(
            value = userState.user.correo,
            onValueChange = { viewModel.onFieldChanged("correo", it) },
            label = { Text("Correo", color = White) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            textStyle = TextStyle(color = White),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Border,
                unfocusedBorderColor = White.copy(alpha = 0.5f),
                cursorColor = Border,
                focusedTextColor = White,
                unfocusedTextColor = White,
                focusedLabelColor = ButtonBackground,
                unfocusedLabelColor = White.copy(alpha = 0.5f)
            ),
            enabled = !userState.isLoading
        )

        // Campo de Contraseña
        OutlinedTextField(
            value = userState.user.contrasena,
            onValueChange = { viewModel.onFieldChanged("contrasena", it) },
            label = { Text("Contraseña", color = White) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            textStyle = TextStyle(color = White),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Border,
                unfocusedBorderColor = White.copy(alpha = 0.5f),
                cursorColor = Border,
                focusedTextColor = White,
                unfocusedTextColor = White,
                focusedLabelColor = ButtonBackground,
                unfocusedLabelColor = White.copy(alpha = 0.5f)
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
                .padding(horizontal = 16.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = ButtonBackground
            ),
            shape = RoundedCornerShape(8.dp),
            enabled = !userState.isLoading
        ) {
            if (userState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = White
                )
            } else {
                Text("Registrar Usuario", color = White, fontSize = 16.sp)
            }
        }

        userState.error?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
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