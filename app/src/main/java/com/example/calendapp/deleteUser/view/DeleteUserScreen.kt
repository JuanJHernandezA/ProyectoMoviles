package com.example.calendapp.deleteUser.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.calendapp.R
import com.example.calendapp.deleteUser.viewmodel.DeleteUserViewModel
import androidx.compose.ui.layout.ContentScale

@Composable
fun ConfirmDeleteDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("¿Estás seguro de eliminar el usuario?", color = Color.White) },
        text = { Text("Esta acción no se puede deshacer.", color = Color.White) },
        containerColor = Color(0xFF1A1C23),
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0A446D)
                )
            ) {
                Text("Sí")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Gray
                )
            ) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun SuccessDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Usuario eliminado exitosamente", color = Color.White) },
        text = { Text("El usuario ha sido eliminado de la base de datos.", color = Color.White) },
        containerColor = Color(0xFF1A1C23),
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0A446D)
                )
            ) {
                Text("Aceptar")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteUserScreen(
    navController: NavController,
    viewModel: DeleteUserViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    var showConfirmDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    LaunchedEffect(state.success) {
        if (state.success) {
            showSuccessDialog = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1C23))
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Fondo de color específico detrás de la imagen
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .background(Color(0xFF0A192F))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Regresar", tint = Color.White)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Eliminar usuario",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Image(
                        painter = painterResource(id = R.drawable.encabezado),
                        contentDescription = "Imagen de encabezado",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            // Contenido principal
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = state.email,
                    onValueChange = { viewModel.updateEmail(it) },
                    label = { Text("Digite el correo electrónico del empleado") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    textStyle = androidx.compose.ui.text.TextStyle(color = Color.White),
                    isError = state.error != null,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF0A446D),
                        unfocusedBorderColor = Color.Gray,
                        focusedLabelColor = Color(0xFF0A446D),
                        unfocusedLabelColor = Color.Gray
                    )
                )

                state.error?.let { error ->
                    Text(
                        text = error,
                        color = Color.Red,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { showConfirmDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    enabled = !state.isLoading && state.email.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (state.email.isNotBlank()) Color(0xFF0A446D) else Color(
                            0xFF0A2459
                        ),
                        disabledContainerColor = Color(0xFF0A446D)
                    )
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White
                        )
                    } else {
                        Text(
                            "Eliminar usuario",
                            color = if (state.email.isNotBlank()) Color.White else Color.Gray
                        )
                    }
                }
            }
        }
    }

    if (showConfirmDialog) {
        ConfirmDeleteDialog(
            onConfirm = {
                viewModel.deleteUser()
                showConfirmDialog = false
            },
            onDismiss = { showConfirmDialog = false }
        )
    }

    if (showSuccessDialog) {
        SuccessDialog(
            onDismiss = {
                showSuccessDialog = false
                navController.popBackStack()
            }
        )
    }
} 