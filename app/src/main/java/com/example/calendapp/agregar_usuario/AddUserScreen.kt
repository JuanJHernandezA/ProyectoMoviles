package com.example.calendapp.agregar_usuario
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calendapp.R
import com.example.calendapp.ui.theme.* // Aquí estamos importando los colores definidos

@Composable
fun AddUserScreen(
    onBackClick: () -> Unit = {} // Placeholder
) {
    val colorScheme = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor) // Usando el color de fondo
            .verticalScroll(rememberScrollState())
    ) {
        // Banner
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(BannerBackground) // Usando el color de fondo del banner
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_back),
                    contentDescription = "Botón de regreso",
                    modifier = Modifier.size(240.dp),
                    tint = White // Usando el color blanco para los íconos
                )
            }

            Image(
                painter = painterResource(id = R.drawable.profile_placeholder),
                contentDescription = "Foto de perfil",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
            )

            Column(
                modifier = Modifier.padding(start = 16.dp)
            ) {
                Text(
                    text = "Nombre usuario",
                    fontSize = 20.sp,
                    color = White // Usando color blanco para el texto
                )
                Text(
                    text = "Administrador",
                    fontSize = 14.sp,
                    color = AccentColor // Usando el color de acento
                )
            }
        }

        // Formulario
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Editor información",
                fontSize = 24.sp,
                color = White, // Usando color blanco
                modifier = Modifier.padding(top = 10.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))
            AddUserFormField(label = "Correo electrónico", keyboardType = KeyboardType.Email)
            AddUserFormField(label = "Contraseña", isPassword = true)
            AddUserFormField(label = "Confirmación de contraseña", isPassword = true)
            AddUserFormField(label = "Nombre", keyboardType = KeyboardType.Text)
            AddUserFormField(label = "Apellido", keyboardType = KeyboardType.Text)
            AddUserFormField(label = "Número de celular", keyboardType = KeyboardType.Phone)
            AddUserFormField(label = "Género", keyboardType = KeyboardType.Text)

            Button(
                onClick = { /* futura lógica */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ButtonBackground // Usando el color de fondo del botón
                )
            ) {
                Text("Agregar usuario", color = White) // Usando color blanco para el texto del botón
            }
        }
    }
}

@Composable
fun AddUserFormField(
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false
) {
    var value by remember { mutableStateOf("") }
    val colorScheme = MaterialTheme.colorScheme

    Text(
        text = label,
        color = White, // Usando el color blanco para los textos de las etiquetas
        modifier = Modifier.padding(top = 10.dp)
    )

    OutlinedTextField(
        value = value,
        onValueChange = { value = it },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        textStyle = TextStyle(color = colorScheme.onBackground),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Border, // Usando el color Contorno para el borde enfocado
            unfocusedBorderColor = colorScheme.onSurface.copy(alpha = 0.5f),
            cursorColor = Border, // Usando el color Contorno para el cursor
            focusedLabelColor = ButtonBackground, // Usando el color de fondo del botón para la etiqueta enfocada
            unfocusedLabelColor = colorScheme.onSurface.copy(alpha = 0.5f)
        )
    )
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AddUserScreenPreview() {
    CalendappTheme {
        AddUserScreen()
    }
}

