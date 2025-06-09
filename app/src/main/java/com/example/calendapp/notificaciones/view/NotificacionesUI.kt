package com.example.calendapp.notificaciones.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.calendapp.config.UserViewModel
import com.example.calendapp.notificaciones.model.Notificacion
import com.example.calendapp.notificaciones.viewmodel.NotificacionesViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificacionesUI(
    navController: NavHostController,
    userViewModel: UserViewModel,
    viewModel: NotificacionesViewModel = viewModel()
) {
    val userState by userViewModel.userState.collectAsState()
    val notificaciones by viewModel.notificaciones.collectAsState()

    // Cargar notificaciones cuando se inicia la pantalla
    LaunchedEffect(userState.cedula) {
        userState.cedula?.let { cedula ->
            viewModel.cargarNotificaciones(cedula)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121C2B))
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            // Barra superior con botón de retroceso y título
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF132D46))
                    .padding(22.dp),
                verticalAlignment = Alignment.CenterVertically

            ) {

                IconButton(onClick = { navController.navigateUp() }) {

                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver",
                        tint = Color.White
                    )
                }

                Text(
                    text = "Notificaciones",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            // Contenido principal
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                if (notificaciones.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No hay notificaciones",
                            color = Color.White,
                            fontSize = 18.sp
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(notificaciones) { notificacion ->
                            NotificacionItem(notificacion = notificacion)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NotificacionItem(notificacion: Notificacion) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2A3C53)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = notificacion.descripcion,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = notificacion.fecha,
                color = Color.Gray,
                fontSize = 12.sp
            )
        }
    }
} 