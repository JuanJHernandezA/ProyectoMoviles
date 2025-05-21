package com.example.calendapp.administrador

import android.app.DatePickerDialog
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

import java.text.SimpleDateFormat
import java.util.*

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*

import com.google.firebase.Timestamp

import kotlinx.coroutines.tasks.await

data class Horario(
    val descripcion: String = "",
    val empleadoId: String = "",
    val fecha: Date? = null, // nuevo campo
    val fechas: List<String> = emptyList(), // nuevo
    val horaInicio: String = "",
    val horaFin: String = "",
    val ubicacion: String = "",
    val empleado: String = ""
)



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdministradorScreen(navController: NavHostController) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

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
                Image(
                    painter = painterResource(id = R.drawable.encabezado), // Reemplázalo con tu imagen
                    contentDescription = "Imagen de encabezado",
                    modifier = Modifier.fillMaxWidth().height(150.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                HorariosContent(Modifier.weight(1f))


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
fun HorariosContent(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val db = remember { FirebaseFirestore.getInstance() }

    val dateFormatter = remember {
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("America/Bogota") // Configurar zona horaria de Colombia
        }
    }

    var selectedDate by remember { mutableStateOf(dateFormatter.format(Date())) }


    var horarios by remember { mutableStateOf<List<Horario>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val timeFormatter = remember {
        SimpleDateFormat("HH:mm:ss", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("America/Bogota") // Configurar zona horaria de Colombia
        }
    }

    var currentTime by remember { mutableStateOf(timeFormatter.format(Date())) }

    LaunchedEffect(Unit) {
        while (true) {
            currentTime = timeFormatter.format(Date())
            kotlinx.coroutines.delay(1000) // Esperar 1 segundo antes de actualizar
        }
    }



    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            selectedDate = "$year-${(month + 1).toString().padStart(2, '0')}-${dayOfMonth.toString().padStart(2, '0')}"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    LaunchedEffect(selectedDate) {
        loading = true
        errorMessage = null
        try {
            val snapshot = db.collection("horarios")
                .get()
                .await()

            val fetchedHorarios = snapshot.documents.mapNotNull { doc ->
                runCatching {
                    val fechasArray = doc.get("fechas") as? List<String> ?: emptyList()

                    Horario(
                        descripcion = doc.getString("descripcion") ?: "",
                        empleadoId = doc.getString("empleadoId") ?: "",
                        fecha = (doc.getTimestamp("fecha") ?: Timestamp.now()).toDate(),
                        fechas = fechasArray,
                        horaInicio = doc.getString("horaInicio") ?: "",
                        horaFin = doc.getString("horaFin") ?: "",
                        ubicacion = doc.getString("ubicacion") ?: "",
                        empleado = doc.getString("empleado") ?: ""
                    )
                }.getOrNull()
            }

            // Filtrar por selectedDate usando fechas o fecha individual
            horarios = fetchedHorarios.filter { horario ->
                horario.fechas.contains(selectedDate) ||
                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(horario.fecha) == selectedDate
            }

            println("Horarios cargados: $horarios")

        } catch (e: Exception) {
            errorMessage = "Error al cargar horarios: ${e.localizedMessage}"
            horarios = emptyList()
        } finally {
            loading = false
        }
    }


    val startHour = 5
    val endHour = 24
    val hourHeightDp = 60.dp


    val backgroundColor = Color(0xFF1A1E29)
    val timelineColor = Color(0xFF3F4861)
    val eventColorBase = Color(0xFF01C383)
    val headerColor = Color(0xFF132D46)
    val headerTextColor = Color.White

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
            Text(
                text = "Fecha: $selectedDate \nHora actual: $currentTime",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = headerTextColor,
                modifier = Modifier.weight(1f)
            )

            Button(onClick = { datePickerDialog.show() }) {
                Text("Seleccionar día")
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        if (loading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Column
        }
        if (errorMessage != null) {
            Text(
                text = errorMessage ?: "",
                color = Color.Red,
                modifier = Modifier.padding(8.dp)
            )
            return@Column
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
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

            // Agrupar por horaInicio redondeada
            val agrupados = horarios.groupBy { it.horaInicio.toHourDecimal() }

            // Agrupar eventos que se cruzan
            val eventosPorHora = mutableListOf<MutableList<Horario>>()
            agrupados.forEach { (_, lista) ->
                lista.forEach { horario ->
                    // Verificar si el evento se cruza con otros
                    val solapado = eventosPorHora.indexOfFirst { grupo ->
                        grupo.any { it.horaInicio.toHourDecimal() < horario.horaFin.toHourDecimal() &&
                                it.horaFin.toHourDecimal() > horario.horaInicio.toHourDecimal() }
                    }
                    if (solapado != -1) {
                        eventosPorHora[solapado].add(horario)
                    } else {
                        eventosPorHora.add(mutableListOf(horario))
                    }
                }
            }

            // Posicionar eventos
            eventosPorHora.forEachIndexed { columnIndex, lista ->
                lista.forEachIndexed { index, horario ->
                    val start = horario.horaInicio.toHourDecimal()
                    val end = horario.horaFin.toHourDecimal().coerceAtLeast(start + 0.25f)
                    if (end <= startHour || start >= endHour) return@forEachIndexed

                    val clampedStart = start.coerceIn(startHour.toFloat(), endHour.toFloat())
                    val clampedEnd = end.coerceIn(startHour.toFloat(), endHour.toFloat())
                    val topOffset = ((clampedStart - startHour) * hourHeightDp.value).dp
                    val boxHeight = ((clampedEnd - clampedStart) * hourHeightDp.value).dp
                    val cardColor = Color(
                        0xFF01C383
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 68.dp + (index * 10).dp + (columnIndex * 80).dp, end = 8.dp)
                            .absoluteOffset(y = topOffset)
                            .height(boxHeight)
                            .background(cardColor, shape = MaterialTheme.shapes.small)
                            .padding(8.dp)
                    ) {
                        Column {
                            Text(
                                text = horario.empleado.ifBlank { "Empleado" },
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                fontSize = 14.sp
                            )
                            Text(
                                text = horario.descripcion.ifBlank { "Sin descripción" },
                                color = Color.Black,
                                fontSize = 12.sp
                            )
                            Text(
                                text = horario.ubicacion.ifBlank { "Sin ubicación" },
                                color = Color.Black,
                                fontSize = 12.sp
                            )
                            Text(
                                text = "De: ${SimpleDateFormat("HH:mm", Locale.getDefault()).format(horario.horaInicio.toHourDecimal())} a ${SimpleDateFormat("HH:mm", Locale.getDefault()).format(horario.horaFin.toHourDecimal())}",
                                color = Color.Black,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

