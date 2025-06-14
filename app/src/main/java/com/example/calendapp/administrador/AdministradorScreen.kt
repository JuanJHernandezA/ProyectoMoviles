package com.example.calendapp.administrador

import android.app.DatePickerDialog
import android.app.TimePickerDialog
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.calendapp.R
import com.example.calendapp.administrador.model.Horario
import com.example.calendapp.administrador.viewmodel.AdministradorViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.calendapp.administrador.model.EventoConPosicion
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdministradorScreen(navController: NavHostController) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val viewModel: AdministradorViewModel = viewModel()

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
                }
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
                        IconButton(onClick = {navController.navigate("notificaciones")}) {
                            Icon(Icons.Default.Notifications, contentDescription = "Notificaciones")
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
                HorariosContent(Modifier.weight(1f), viewModel)
            }
        }
    }
}

@Composable
fun DrawerContent(
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(Color(0xFF0A192F))
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        IconButton(onClick = { onNavigate("administrador") }) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = Color.White)
        }
        Text("Panel administrador", color = Color.White, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        DrawerItem("Agregar usuario", Icons.Default.PersonAdd) { onNavigate("agregar_usuario") }
        DrawerItem("Agregar turno", Icons.Default.Event) { onNavigate("agregar_turno") }
        DrawerItem("Eliminar usuario", Icons.Default.PersonRemove) { onNavigate("eliminar_usuario") }
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

fun String.toHourDecimal(): Float {
    val parts = this.split(":")
    val hour = parts.getOrNull(0)?.toIntOrNull() ?: 0
    val minute = parts.getOrNull(1)?.toIntOrNull() ?: 0
    return hour + minute / 60f
}

@Composable
fun HorariosContent(modifier: Modifier = Modifier, viewModel: AdministradorViewModel) {
    val context = LocalContext.current
    var currentTime by remember { mutableStateOf("") }
    val timeFormatter = remember {
        SimpleDateFormat("HH:mm:ss", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("America/Bogota")
        }
    }

    // TimePickerDialogs
    val timePickerInicio = TimePickerDialog(
        context,
        { _, hour, minute ->
            viewModel.updateEditedFields(horaInicio = String.format("%02d:%02d", hour, minute))
        },
        0, 0, true
    )

    val timePickerFin = TimePickerDialog(
        context,
        { _, hour, minute ->
            viewModel.updateEditedFields(horaFin = String.format("%02d:%02d", hour, minute))
        },
        0, 0, true
    )

    LaunchedEffect(Unit) {
        while (true) {
            currentTime = timeFormatter.format(Date())
            viewModel.loadWeatherInfo()
            kotlinx.coroutines.delay(1000)
        }
    }

    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val date = String.format("%d-%02d-%02d", year, month + 1, dayOfMonth)
            viewModel.updateSelectedDate(date)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF1A1E29))
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF132D46))
                .padding(8.dp)
        ) {
            val clima = viewModel.weatherInfo?.let { "${it.city}: ${it.temperature}°C" } ?: "Cargando clima..."
            Text(
                text = "Fecha: ${viewModel.selectedDate} \nHora actual: $currentTime \n$clima",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color.White,
                modifier = Modifier.weight(1f)
            )
            Button(onClick = { datePickerDialog.show() }) {
                Text("Seleccionar día")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (viewModel.loading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Column
        }

        if (viewModel.errorMessage != null) {
            Text(
                text = viewModel.errorMessage ?: "",
                color = Color.Red,
                modifier = Modifier.padding(8.dp)
            )
            return@Column
        }

        val scrollState = rememberScrollState()

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .horizontalScroll(scrollState)
        ) {
            val totalWidthDp = if (5 > viewModel.horarios.size && viewModel.horarios.size > 1) 500.dp else if (viewModel.horarios.size > 5) 800.dp else 360.dp

            Box(
                modifier = Modifier
                    .width(totalWidthDp)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState())
                    .background(Color(0xFF3F4861))
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    for (hour in 0..24) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
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

                val eventosConPosicion = mutableListOf<EventoConPosicion>()
                val ordenados = viewModel.horarios.sortedBy { it.horaInicio.toHourDecimal() }
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

                    if (end <= 0 || start >= 24) return@forEach

                    val clampedStart = start.coerceIn(0f, 24f)
                    val clampedEnd = end.coerceIn(0f, 24f)

                    val topOffset = ((clampedStart - 0) * 60.dp.value).dp
                    val boxHeight = ((clampedEnd - clampedStart) * 60.dp.value).dp

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
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = horario.empleado.ifBlank { "Empleado" },
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black,
                                    fontSize = 16.sp
                                )
                                Row {
                                    IconButton(
                                        onClick = { viewModel.showEditDialog(horario) },
                                        modifier = Modifier.size(20.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.Edit,
                                            contentDescription = "Editar horario",
                                            tint = Color.Blue,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(4.dp))
                                    IconButton(
                                        onClick = { viewModel.showDeleteConfirmation(horario) },
                                        modifier = Modifier.size(20.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.Delete,
                                            contentDescription = "Eliminar horario",
                                            tint = Color.Red,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
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

    // Delete Confirmation Dialog
    if (viewModel.showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { viewModel.hideDeleteConfirmation() },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Está seguro que desea eliminar este horario?") },
            confirmButton = {
                TextButton(
                    onClick = { viewModel.deleteHorario(context) }
                ) {
                    Text("Sí", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { viewModel.hideDeleteConfirmation() }
                ) {
                    Text("No")
                }
            }
        )
    }

    // Edit Dialog
    if (viewModel.showEditDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.hideEditDialog() },
            title = { 
                Text(
                    "Editar Horario",
                    color = Color.White
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    OutlinedTextField(
                        value = viewModel.editedDescripcion,
                        onValueChange = { viewModel.updateEditedFields(descripcion = it) },
                        label = { Text("Descripción", color = Color.White) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.Gray,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.Gray,
                            cursorColor = Color.White,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = { timePickerInicio.show() },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color(0xFF2A3C53)
                        )
                    ) {
                        Icon(Icons.Default.AccessTime, contentDescription = null, tint = Color.White)
                        Spacer(Modifier.width(4.dp))
                        Text(
                            "Hora Inicio: ${viewModel.editedHoraInicio}",
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = { timePickerFin.show() },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color(0xFF2A3C53)
                        )
                    ) {
                        Icon(Icons.Default.AccessTime, contentDescription = null, tint = Color.White)
                        Spacer(Modifier.width(4.dp))
                        Text(
                            "Hora Fin: ${viewModel.editedHoraFin}",
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = viewModel.editedUbicacion,
                        onValueChange = { viewModel.updateEditedFields(ubicacion = it) },
                        label = { Text("Ubicación", color = Color.White) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.Gray,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.Gray,
                            cursorColor = Color.White,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { viewModel.updateHorario(context) }
                ) {
                    Text("Guardar", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { viewModel.hideEditDialog() }
                ) {
                    Text("Cancelar", color = Color.White)
                }
            },
            containerColor = Color(0xFF3F4861),
            titleContentColor = Color.White,
            textContentColor = Color.White
        )
    }
}
