package com.example.calendapp.notificaciones

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PendingActions
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calendapp.R
import androidx.navigation.NavHostController

@Composable
fun NotificacionesUI(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121C2B)) // Fondo general oscuro
    ) {
        // Fondo de color específico detrás de la imagen
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(204.dp)
                .background(Color(0xFF132D46))
                .align(Alignment.TopCenter)
        )

        // Imagen de fondo encima del fondo de color
        Image(
            painter = painterResource(id = R.drawable.image3),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(204.dp)
                .align(Alignment.TopCenter)
                .graphicsLayer { alpha = 0.9f },
            contentScale = ContentScale.Crop
        )

        // Cabecera con el botón de retroceder y el nombre del usuario
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Botón de retroceder
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp) // Tamaño más grande
                )
            }

            // Nombre del usuario al lado del botón de retroceder
            Text(
                text = "Usuario Temporal",
                style = TextStyle(
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.Default
                ),
                modifier = Modifier.padding(start = 8.dp) // Espaciado entre el ícono y el texto
            )
        }

        // Título en la parte superior centrado
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 100.dp)
        ) {
            Text(
                text = "Centro de notificaciones",
                style = TextStyle(
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Default
                ),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        // Contenido principal debajo de la cabecera
        Column(
            modifier = Modifier
                .padding(top = 220.dp) // Alineamos todo este bloque debajo de la imagen
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Filtro + Encabezado de sección
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp) // Espaciado entre el botón de filtro y el texto
            ) {
                // TextBox para el botón de filtro
                Box(
                    modifier = Modifier
                        .size(48.dp) // Tamaño del TextBox igual al botón
                        .background(Color(0xFF2A3C53), shape = RoundedCornerShape(8.dp)) // Fondo gris con bordes redondeados
                        .padding(4.dp) // Espaciado interno
                ) {
                    IconButton(
                        onClick = { /* Acción de filtro */ },
                        modifier = Modifier.fillMaxSize() // Asegura que el botón ocupe todo el espacio del TextBox
                    ) {
                        Icon(Icons.Default.FilterList, contentDescription = "Filtro", tint = Color.White)
                    }
                }

                // TextBox de "Descripciones"
                Box(
                    modifier = Modifier
                        .fillMaxWidth() // Abarca el espacio restante hacia la derecha
                        .height(48.dp) // Igual altura que el botón de filtro
                        .background(Color(0xFF2A3C53), shape = RoundedCornerShape(8.dp)) // Fondo gris con bordes redondeados
                        .padding(horizontal = 12.dp) // Espaciado interno horizontal
                ) {
                    // Centrar el texto verticalmente dentro del Box
                    Box(
                        modifier = Modifier.fillMaxSize(), // Asegura que el texto ocupe todo el espacio disponible
                        contentAlignment = Alignment.Center // Centra el contenido dentro del Box
                    ) {
                        Text(
                            text = "Descripciones",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Lista de notificaciones
            val notificaciones = listOf(
                Pair(Icons.Default.AccessTime, "Tu turno comienza pronto"),
                Pair(Icons.Default.CheckCircle, "Se ha registrado tu horario para el dd/mm/yyyy"),
                Pair(Icons.Default.Work, "Trabajo pendiente")
            )

            notificaciones.forEach { (icono, texto) ->
                NotificacionItem(icono, texto)
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Cuadrículas vacías (simulación de placeholders)
            repeat(4) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .background(Color(0xFF2A3C53), shape = RoundedCornerShape(8.dp))
                ) {}
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

// Composable para cada notificación
@Composable
fun NotificacionItem(icon: ImageVector, texto: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxWidth() // Abarca todo el ancho disponible
            .background(Color(0xFF2A3C53), shape = RoundedCornerShape(8.dp)) // Fondo gris con bordes redondeados
            .padding(12.dp) // Espaciado interno
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = texto,
            color = Color.White,
            fontSize = 14.sp
        )
    }
}
