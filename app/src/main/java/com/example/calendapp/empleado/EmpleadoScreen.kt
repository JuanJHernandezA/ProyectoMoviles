package com.example.calendapp.empleado

import android.app.DatePickerDialog
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.calendapp.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import com.example.calendapp.config.UserViewModel

import java.text.SimpleDateFormat
import java.util.*

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.ui.layout.ContentScale
import com.example.calendapp.administrador.Horario
import com.example.calendapp.administrador.toHourDecimal

import com.google.firebase.Timestamp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

import kotlinx.coroutines.tasks.await
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.calendapp.notificaciones.viewmodel.NotificacionesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmpleadoScreen(
    navController: NavHostController,
    cedula: String,
    viewModel: EmpleadoViewModel = viewModel { EmpleadoViewModel(cedula) },
    notificacionesViewModel: NotificacionesViewModel = viewModel()
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val tieneNotificacionesNoLeidas by notificacionesViewModel.tieneNotificacionesNoLeidas.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadHorarios()
        notificacionesViewModel.cargarNotificaciones(cedula)
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                onNavigate = { route ->
                    navController.navigate(route)
                    scope.launch { drawerState.close() }
                },
                onLogout = {
                    navController.navigate("login")
                    scope.launch { drawerState.close() }
                },
                drawerState = drawerState
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("CalendApp") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menú")
                        }
                    },
                    actions = {
                        Box {
                            IconButton(
                                onClick = {
                                    notificacionesViewModel.marcarTodasComoLeidas()
                                    navController.navigate("notificaciones")
                                }
                            ) {
                                Icon(Icons.Default.Notifications, contentDescription = "Notificaciones")
                            }
                            if (tieneNotificacionesNoLeidas) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(Color.Red, shape = CircleShape)
                                        .align(Alignment.TopEnd)
                                        .offset(x = (-4).dp, y = 4.dp)
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF0A192F),
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White,
                        actionIconContentColor = Color.White
                    )
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier.fillMaxSize().padding(padding),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .background(Color(0xFF0A192F))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.encabezado),
                        contentDescription = "Imagen de encabezado",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }
                HorariosContent(
                    modifier = Modifier.weight(1f),
                    cedula,
                    state = state,
                    onDateSelected = viewModel::updateSelectedDate
                )
            }
        }
    }
}

@Composable
fun DrawerContent(
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit,
    drawerState: DrawerState
) {
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(Color(0xFF0A192F))
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        IconButton(onClick = { scope.launch { drawerState.close() } }) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = Color.White)
        }
        Text("Panel empleado", color = Color.White, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        DrawerItem("Editar perfil", Icons.Default.Edit) { onNavigate("editar_perfil") }
        DrawerItem("Cerrar sesión", Icons.Default.Close) { onLogout() }
    }
}

@Composable
fun DrawerItem(text: String, icon: ImageVector, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = text, tint = Color.White)
        Spacer(modifier = Modifier.width(12.dp))
        Text(text, color = Color.White)
    }
}


@Composable
fun HorariosContent(
    modifier: Modifier = Modifier,
    cedula: String,
    state: EmpleadoState,
    onDateSelected: (String) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val selectedDate = "$year-${(month + 1).toString().padStart(2, '0')}-${dayOfMonth.toString().padStart(2, '0')}"
            onDateSelected(selectedDate)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    val startHour = 0
    val endHour = 24
    val hourHeightDp = 60.dp

    val backgroundColor = Color(0xFF1A1E29)
    val timelineColor = Color(0xFF3F4861)
    val eventColorBase = Color(0xFF01C383)
    val headerColor = Color(0xFF132D46)
    val headerTextColor = Color.White
    Log.d("EmpleadoScreen", "Estado del usuario - Cédula: $cedula")
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(headerColor)
                .padding(8.dp)
        ) {
            val clima = state.weatherInfo ?: "Cargando clima..."
            Text(
                text = "Fecha: ${state.selectedDate} \nHora actual: ${state.currentTime} \n$clima",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = headerTextColor,
                modifier = Modifier.weight(1f)
            )

            Button(
                onClick = { datePickerDialog.show() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0A446D)
                )
            ) {
                Text("Seleccionar día")
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Column
        }
        
        if (state.error != null) {
            Text(
                text = state.error,
                color = Color.Red,
                modifier = Modifier.padding(8.dp)
            )
            return@Column
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .horizontalScroll(rememberScrollState())
        ) {
            val totalWidthDp = 360.dp

            Box(
                modifier = Modifier
                    .width(totalWidthDp)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState())
                    .background(timelineColor)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    for (hour in startHour..endHour) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(hourHeightDp)
                                .border(0.5.dp, Color.Gray),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = String.format("%02d:00", hour),
                                modifier = Modifier
                                    .width(60.dp)
                                    .padding(start = 4.dp, top = 4.dp),
                                fontSize = 12.sp,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                            )
                        }
                    }
                }

                data class EventoConPosicion(
                    val horario: Horario,
                    val columna: Int,
                    val totalColumnas: Int
                )

                val eventosConPosicion = mutableListOf<EventoConPosicion>()
                val ordenados = state.horarios.sortedBy { it.horaInicio.toHourDecimal() }
                val columnas = mutableListOf<MutableList<Horario>>()

                ordenados.forEach { horario ->
                    var colocado = false
                    for ((i, columna) in columnas.withIndex()) {
                        if (columna.none { existente ->
                                existente.horaInicio.toHourDecimal() < horario.horaFin.toHourDecimal() &&
                                        existente.horaFin.toHourDecimal() > horario.horaInicio.toHourDecimal()
                            }) {
                            columna.add(horario)
                            eventosConPosicion.add(EventoConPosicion(horario, i, -1))
                            colocado = true
                            break
                        }
                    }
                    if (!colocado) {
                        columnas.add(mutableListOf(horario))
                        eventosConPosicion.add(EventoConPosicion(horario, columnas.lastIndex, columnas.size))
                    }
                }

                val maxColumnas = columnas.size
                val eventosFinal = eventosConPosicion.map {
                    it.copy(totalColumnas = maxColumnas)
                }

                eventosFinal.forEach { evento ->
                    val horario = evento.horario
                    val columna = evento.columna
                    val totalColumnas = evento.totalColumnas

                    val start = horario.horaInicio.toHourDecimal()
                    val end = horario.horaFin.toHourDecimal()

                    if (end <= startHour || start >= endHour) return@forEach

                    val clampedStart = start.coerceIn(startHour.toFloat(), endHour.toFloat())
                    val clampedEnd = end.coerceIn(startHour.toFloat(), endHour.toFloat())

                    val topOffset = ((clampedStart - startHour) * hourHeightDp.value).dp
                    val boxHeight = ((clampedEnd - clampedStart + 1) * hourHeightDp.value).dp

                    val columnWidth = ((totalWidthDp - 60.dp) / totalColumnas)
                    val cardColor = Color(0xA18682F5)

                    Box(
                        modifier = Modifier
                            .absoluteOffset(x = 60.dp + columnWidth * columna, y = topOffset)
                            .width(columnWidth - 8.dp)
                            .height(boxHeight)
                            .background(cardColor, shape = MaterialTheme.shapes.small)
                            .padding(4.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = horario.empleado.ifBlank { "Empleado" },
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                fontSize = 16.sp
                            )
                            Column {
                                Text(
                                    text = horario.descripcion.ifBlank { "Sin descripción" },
                                    color = Color.Black,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = horario.ubicacion.ifBlank { "Sin ubicación" },
                                    color = Color.Black,
                                    fontSize = 14.sp
                                )
                            }
                            Text(
                                text = "${horario.horaInicio} - ${horario.horaFin}",
                                color = Color.Black,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

