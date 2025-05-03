package com.example.calendapp.ui.theme

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calendapp.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun EditUserScreen(onBackClick: () -> Unit = {}) {
    val context = LocalContext.current
    val db = Firebase.firestore

    // Estados del formulario
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    val nombre = remember { mutableStateOf("") }
    val apellido = remember { mutableStateOf("") }
    val celular = remember { mutableStateOf("") }
    val genero = remember { mutableStateOf("") }

    EditUserScreenContent(
        email = email,
        password = password,
        confirmPassword = confirmPassword,
        nombre = nombre,
        apellido = apellido,
        celular = celular,
        genero = genero,
        onAddClick = {
            if (password.value != confirmPassword.value) {
                Toast.makeText(context, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return@EditUserScreenContent
            }

            val user = hashMapOf(
                "email" to email.value,
                "password" to password.value,
                "nombre" to nombre.value,
                "apellido" to apellido.value,
                "celular" to celular.value,
                "genero" to genero.value
            )

            db.collection("usuarios")
                .add(user)
                .addOnSuccessListener {
                    Toast.makeText(context, "Usuario agregado exitosamente", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        },
        onBackClick = onBackClick
    )
}

@Composable
fun EditUserScreenContent(
    email: MutableState<String>,
    password: MutableState<String>,
    confirmPassword: MutableState<String>,
    nombre: MutableState<String>,
    apellido: MutableState<String>,
    celular: MutableState<String>,
    genero: MutableState<String>,
    onAddClick: () -> Unit,
    onBackClick: () -> Unit = {}
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
                    modifier = Modifier.size(240.dp),
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
                Text(text = "Nombre usuario", fontSize = 20.sp, color = White)
                Text(text = "Rol", fontSize = 14.sp, color = AccentColor)
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

            EditUserFormField("Correo electrónico", email.value, { email.value = it }, KeyboardType.Email)
            EditUserFormField("Contraseña", password.value, { password.value = it }, KeyboardType.Password, isPassword = true)
            EditUserFormField("Confirmación de contraseña", confirmPassword.value, { confirmPassword.value = it }, KeyboardType.Password, isPassword = true)
            EditUserFormField("Nombre", nombre.value, { nombre.value = it }, KeyboardType.Text)
            EditUserFormField("Apellido", apellido.value, { apellido.value = it }, KeyboardType.Text)
            EditUserFormField("Número de celular", celular.value, { celular.value = it }, KeyboardType.Phone)
            EditUserFormField("Género", genero.value, { genero.value = it }, KeyboardType.Text)

            Button(
                onClick = onAddClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ButtonBackground)
            ) {
                Text("Guardar", color = White)
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
    isPassword: Boolean = false
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
        textStyle = TextStyle(color = White), // 👈 Color del texto dentro del campo
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Border,
            unfocusedBorderColor = White.copy(alpha = 0.5f),
            cursorColor = Border,
            focusedTextColor = White,       // 👈 Asegura el color cuando está enfocado
            unfocusedTextColor = White,     // 👈 Asegura el color cuando no está enfocado
            focusedLabelColor = ButtonBackground,
            unfocusedLabelColor = White.copy(alpha = 0.5f)
        )
    )
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EditUserScreenPreview() {
    CalendappTheme {
        EditUserScreenContent(
            email = remember { mutableStateOf("correo@ejemplo.com") },
            password = remember { mutableStateOf("123456") },
            confirmPassword = remember { mutableStateOf("123456") },
            nombre = remember { mutableStateOf("Marlon") },
            apellido = remember { mutableStateOf("Astudillo") },
            celular = remember { mutableStateOf("3001234567") },
            genero = remember { mutableStateOf("Masculino") },
            onAddClick = {},
            onBackClick = {}
        )
    }
}
