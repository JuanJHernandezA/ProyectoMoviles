package com.example.calendapp.deleteUser

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun ConfirmadoEliminarScreen(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF1A1C23)),
        contentAlignment = Alignment.Center ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxSize()
                .background(Color(0xFF1A1C23))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "¡Usuario eliminado exitosamente!",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,

            )


            Spacer(modifier = Modifier.height(24.dp))

            IconButton(onClick = { navController.navigate("administrador") }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
            }
        }
    }
}
