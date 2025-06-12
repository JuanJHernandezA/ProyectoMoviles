package com.example.calendapp.registerUser

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import androidx.compose.foundation.Image
import androidx.compose.foundation.border


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
        // Banner
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(BannerBackground)
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

            Image(
                painter = painterResource(id = R.drawable.profile_placeholder),
                contentDescription = "Foto de perfil",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
            )

            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(
                    text = userState.currentUserName.ifEmpty { "Administrador" },
                    fontSize = 20.sp,
                    color = White
                )
                Text(
                    text = userState.currentUserRole.ifEmpty { "Admin" },
                    fontSize = 14.sp,
                    color = AccentColor
                )
            }
        }

        // Formulario
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Agregar suario",
                fontSize = 24.sp,
                color = White,
                modifier = Modifier.padding(top = 10.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Campo de Correo
            Text(
                text = "Correo electrónico",
                color = White,
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .border(3.dp, Border, RoundedCornerShape(8.dp))
            ) {
                OutlinedTextField(
                    value = userState.user.correo,
                    onValueChange = { viewModel.onFieldChanged("correo", it) },
                    label = { Text("", color = White) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(BackgroundColor, RoundedCornerShape(8.dp)),
                    textStyle = TextStyle(color = White),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        cursorColor = Border,
                        focusedTextColor = White,
                        unfocusedTextColor = White,
                        focusedLabelColor = Border,
                        unfocusedLabelColor = White.copy(alpha = 0.5f)
                    ),
                    enabled = !userState.isLoading,
                    shape = RoundedCornerShape(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de Contraseña
            Text(
                text = "Contraseña",
                color = White,
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .border(3.dp, Border, RoundedCornerShape(8.dp))
            ) {
                OutlinedTextField(
                    value = userState.user.contrasena,
                    onValueChange = { viewModel.onFieldChanged("contrasena", it) },
                    label = { Text("", color = White) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(BackgroundColor, RoundedCornerShape(8.dp)),
                    textStyle = TextStyle(color = White),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        cursorColor = Border,
                        focusedTextColor = White,
                        unfocusedTextColor = White,
                        focusedLabelColor = Border,
                        unfocusedLabelColor = White.copy(alpha = 0.5f)
                    ),
                    visualTransformation = PasswordVisualTransformation(),
                    enabled = !userState.isLoading,
                    shape = RoundedCornerShape(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de Confirmar Contraseña
            Text(
                text = "Confirmación de Contraseña",
                color = White,
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .border(3.dp, Border, RoundedCornerShape(8.dp))
            ) {
                OutlinedTextField(
                    value = userState.user.confirmarContrasena,
                    onValueChange = { viewModel.onFieldChanged("confirmarContrasena", it) },
                    label = { Text("", color = White) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(BackgroundColor, RoundedCornerShape(8.dp)),
                    textStyle = TextStyle(color = White),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        cursorColor = Border,
                        focusedTextColor = White,
                        unfocusedTextColor = White,
                        focusedLabelColor = Border,
                        unfocusedLabelColor = White.copy(alpha = 0.5f)
                    ),
                    visualTransformation = PasswordVisualTransformation(),
                    enabled = !userState.isLoading,
                    shape = RoundedCornerShape(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de Nombre
            Text(
                text = "Nombre",
                color = White,
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .border(3.dp, Border, RoundedCornerShape(8.dp))
            ) {
                OutlinedTextField(
                    value = userState.user.nombre,
                    onValueChange = { viewModel.onFieldChanged("nombre", it) },
                    label = { Text("", color = White) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(BackgroundColor, RoundedCornerShape(8.dp)),
                    textStyle = TextStyle(color = White),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        cursorColor = Border,
                        focusedTextColor = White,
                        unfocusedTextColor = White,
                        focusedLabelColor = Border,
                        unfocusedLabelColor = White.copy(alpha = 0.5f)
                    ),
                    enabled = !userState.isLoading,
                    shape = RoundedCornerShape(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de Apellido
            Text(
                text = "Apellido",
                color = White,
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .border(3.dp, Border, RoundedCornerShape(8.dp))
            ) {
                OutlinedTextField(
                    value = userState.user.apellido,
                    onValueChange = { viewModel.onFieldChanged("apellido", it) },
                    label = { Text("", color = White) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(BackgroundColor, RoundedCornerShape(8.dp)),
                    textStyle = TextStyle(color = White),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        cursorColor = Border,
                        focusedTextColor = White,
                        unfocusedTextColor = White,
                        focusedLabelColor = Border,
                        unfocusedLabelColor = White.copy(alpha = 0.5f)
                    ),
                    enabled = !userState.isLoading,
                    shape = RoundedCornerShape(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de numero celular
            Text(
                text = "Número de celular",
                color = White,
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .border(3.dp, Border, RoundedCornerShape(8.dp))
            ) {
                OutlinedTextField(
                    value = userState.user.telefono,
                    onValueChange = { viewModel.onFieldChanged("telefono", it) },
                    label = { Text("", color = White) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(BackgroundColor, RoundedCornerShape(8.dp)),
                    textStyle = TextStyle(color = White),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        cursorColor = Border,
                        focusedTextColor = White,
                        unfocusedTextColor = White,
                        focusedLabelColor = Border,
                        unfocusedLabelColor = White.copy(alpha = 0.5f)
                    ),
                    enabled = !userState.isLoading,
                    shape = RoundedCornerShape(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de Género
            Text(
                text = "Género",
                color = White,
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .border(3.dp, Border, RoundedCornerShape(8.dp))
            ) {
                OutlinedTextField(
                    value = userState.user.genero,
                    onValueChange = { viewModel.onFieldChanged("genero", it) },
                    label = { Text("", color = White) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(BackgroundColor, RoundedCornerShape(8.dp)),
                    textStyle = TextStyle(color = White),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        cursorColor = Border,
                        focusedTextColor = White,
                        unfocusedTextColor = White,
                        focusedLabelColor = Border,
                        unfocusedLabelColor = White.copy(alpha = 0.5f)
                    ),
                    enabled = !userState.isLoading,
                    shape = RoundedCornerShape(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de Edad
            Text(
                text = "Edad",
                color = White,
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .border(3.dp, Border, RoundedCornerShape(8.dp))
            ) {
                OutlinedTextField(
                    value = userState.user.edad,
                    onValueChange = { viewModel.onFieldChanged("edad", it) },
                    label = { Text("", color = White) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(BackgroundColor, RoundedCornerShape(8.dp)),
                    textStyle = TextStyle(color = White),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        cursorColor = Border,
                        focusedTextColor = White,
                        unfocusedTextColor = White,
                        focusedLabelColor = Border,
                        unfocusedLabelColor = White.copy(alpha = 0.5f)
                    ),
                    enabled = !userState.isLoading,
                    shape = RoundedCornerShape(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de Cédula
            Text(
                text = "Cédula",
                color = White,
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .border(3.dp, Border, RoundedCornerShape(8.dp))
            ) {
                OutlinedTextField(
                    value = userState.user.cedula,
                    onValueChange = { viewModel.onFieldChanged("cedula", it) },
                    label = { Text("", color = White) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(BackgroundColor, RoundedCornerShape(8.dp)),
                    textStyle = TextStyle(color = White),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        cursorColor = Border,
                        focusedTextColor = White,
                        unfocusedTextColor = White,
                        focusedLabelColor = Border,
                        unfocusedLabelColor = White.copy(alpha = 0.5f)
                    ),
                    enabled = !userState.isLoading,
                    shape = RoundedCornerShape(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de Rol
            Text(
                text = "Rol",
                color = White,
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .border(3.dp, Border, RoundedCornerShape(8.dp))
            ) {
                OutlinedTextField(
                    value = userState.user.rol,
                    onValueChange = { viewModel.onFieldChanged("rol", it) },
                    label = { Text("", color = White) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(BackgroundColor, RoundedCornerShape(8.dp)),
                    textStyle = TextStyle(color = White),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        cursorColor = Border,
                        focusedTextColor = White,
                        unfocusedTextColor = White,
                        focusedLabelColor = Border,
                        unfocusedLabelColor = White.copy(alpha = 0.5f)
                    ),
                    enabled = !userState.isLoading,
                    shape = RoundedCornerShape(8.dp)
                )
            }

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
                    Text("Agregar usuario", color = White, fontSize = 16.sp)
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
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AddUserScreenPreview() {
    AddUserScreen(
        viewModel = AddUserViewModel(),
        onUserCreated = {}
    )
} 