package com.example.calendapp.editar_usuario

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.calendapp.R
import com.example.calendapp.ui.theme.*

@Composable
fun EditUserScreen(
    viewModel: EditUserViewModel = viewModel(),
    onBackClick: () -> Unit = {}
) {
    val userState by viewModel.userState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(userState.isSuccess) {
        if (userState.isSuccess) {
            Toast.makeText(context, "Usuario actualizado exitosamente", Toast.LENGTH_SHORT).show()
            onBackClick()
            viewModel.resetState()
        }
    }

    EditUserScreenContent(
        email = userState.email,
        password = userState.password,
        confirmPassword = userState.confirmPassword,
        nombre = userState.nombre,
        apellido = userState.apellido,
        telefono = userState.telefono,
        genero = userState.genero,
        isLoading = userState.isLoading,
        error = userState.error,
        onFieldChanged = viewModel::onFieldChanged,
        onUpdateClick = { viewModel.updateUser() },
        onBackClick = onBackClick
    )
}

@Composable
fun EditUserScreenContent(
    email: String,
    password: String,
    confirmPassword: String,
    nombre: String,
    apellido: String,
    telefono: String,
    genero: String,
    isLoading: Boolean,
    error: String?,
    onFieldChanged: (String, String) -> Unit,
    onUpdateClick: () -> Unit,
    onBackClick: () -> Unit
) {
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
                Text(text = nombre, fontSize = 20.sp, color = White)
                Text(text = "Usuario", fontSize = 14.sp, color = AccentColor)
            }
        }

        // Formulario
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Editar Información",
                fontSize = 24.sp,
                color = White,
                modifier = Modifier.padding(top = 10.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            EditUserFormField(
                label = "Correo electrónico",
                value = email,
                onValueChange = { onFieldChanged("email", it) },
                keyboardType = KeyboardType.Email,
                enabled = false
            )
            EditUserFormField(
                label = "Contraseña",
                value = password,
                onValueChange = { onFieldChanged("password", it) },
                keyboardType = KeyboardType.Password,
                isPassword = true
            )
            EditUserFormField(
                label = "Confirmación de contraseña",
                value = confirmPassword,
                onValueChange = { onFieldChanged("confirmPassword", it) },
                keyboardType = KeyboardType.Password,
                isPassword = true
            )
            EditUserFormField(
                label = "Nombre",
                value = nombre,
                onValueChange = { onFieldChanged("nombre", it) },
                keyboardType = KeyboardType.Text
            )
            EditUserFormField(
                label = "Apellido",
                value = apellido,
                onValueChange = { onFieldChanged("apellido", it) },
                keyboardType = KeyboardType.Text
            )
            EditUserFormField(
                label = "Número de teléfono",
                value = telefono,
                onValueChange = { onFieldChanged("telefono", it) },
                keyboardType = KeyboardType.Phone
            )
            EditUserFormField(
                label = "Género",
                value = genero,
                onValueChange = { onFieldChanged("genero", it) },
                keyboardType = KeyboardType.Text
            )

            error?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Button(
                onClick = onUpdateClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ButtonBackground),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = White
                    )
                } else {
                    Text("Guardar", color = White)
                }
            }
        }
    }
}

@Composable
fun EditUserFormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    enabled: Boolean = true
) {
    Text(
        text = label,
        color = White,
        modifier = Modifier.padding(top = 10.dp)
    )

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        textStyle = TextStyle(color = White),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        enabled = enabled,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Border,
            unfocusedBorderColor = White.copy(alpha = 0.5f),
            cursorColor = Border,
            focusedTextColor = White,
            unfocusedTextColor = White,
            focusedLabelColor = ButtonBackground,
            unfocusedLabelColor = White.copy(alpha = 0.5f)
        )
    )
}


