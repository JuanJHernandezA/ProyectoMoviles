package com.example.calendapp.agregar_calendario

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.calendapp.agregar_calendario.model.Usuario
import com.example.calendapp.agregar_calendario.viewmodel.AgregarCalendarioViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class AgregarCalendarioViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var mockFirestore: FirebaseFirestore

    private lateinit var viewModel: AgregarCalendarioViewModel
    @Mock
    private lateinit var mockDb: FirebaseFirestore

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        MockitoAnnotations.openMocks(this)
        viewModel = AgregarCalendarioViewModel(mockDb)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test initial state`() = runTest {
        // Given: Initial state
        
        // When: ViewModel is created
        
        // Then: Initial values should be correct
        assertEquals("", viewModel.nombreUsuario.value)
        assertEquals("00:00", viewModel.horaInicio.value)
        assertEquals("00:00", viewModel.horaFin.value)
        assertEquals("", viewModel.ubicacion.value)
        assertEquals("", viewModel.descripcion.value)
        assertTrue(viewModel.fechasSeleccionadas.value.isEmpty())
        assertFalse(viewModel.mostrarVentanaSugerencias.value)
        assertFalse(viewModel.mostrarVentanaAsignados.value)
        assertFalse(viewModel.mostrarCalendario.value)
        assertFalse(viewModel.mostrarErrores.value)
        assertNull(viewModel.mensajeError.value)
        assertNull(viewModel.mensajeExito.value)
        assertTrue(viewModel.usuariosSeleccionados.value.isEmpty())
    }

    @Test
    fun `test actualizarNombreUsuario should update nombreUsuario`() = runTest {
        // Given: Test name
        val testName = "Juan"
        
        // When: Name is updated
        viewModel.actualizarNombreUsuario(testName)
        
        // Then: Name should be updated
        assertEquals(testName, viewModel.nombreUsuario.value)
    }

    @Test
    fun `test actualizarHoraInicio should update horaInicio`() = runTest {
        // Given: Test start time
        val testTime = "09:00"
        
        // When: Start time is updated
        viewModel.actualizarHoraInicio(testTime)
        
        // Then: Start time should be updated
        assertEquals(testTime, viewModel.horaInicio.value)
    }

    @Test
    fun `test actualizarHoraFin should update horaFin`() = runTest {
        // Given: Test end time and start time
        val startTime = "09:00"
        val endTime = "10:00"
        viewModel.actualizarHoraInicio(startTime)
        
        // When: End time is updated
        viewModel.actualizarHoraFin(endTime)
        
        // Then: End time should be updated
        assertEquals(endTime, viewModel.horaFin.value)
    }

    @Test
    fun `test actualizarHoraFin should show error when start time is not set`() = runTest {
        // Given: No start time set
        
        // When: End time is updated
        viewModel.actualizarHoraFin("10:00")
        
        // Then: Error message should be shown
        assertEquals("Debe ingresar primero la hora de inicio", viewModel.mensajeError.value)
    }

    @Test
    fun `test actualizarHoraFin should show error when end time is before start time`() = runTest {
        // Given: Start time is set
        viewModel.actualizarHoraInicio("10:00")
        
        // When: End time is before start time
        viewModel.actualizarHoraFin("09:00")
        
        // Then: Error message should be shown
        assertEquals("La hora de finalización no puede ser anterior a la hora de inicio", viewModel.mensajeError.value)
    }

    @Test
    fun `test actualizarUbicacion should update ubicacion`() = runTest {
        // Given: Test location
        val testLocation = "Oficina 101"
        
        // When: Location is updated
        viewModel.actualizarUbicacion(testLocation)
        
        // Then: Location should be updated
        assertEquals(testLocation, viewModel.ubicacion.value)
    }

    @Test
    fun `test actualizarDescripcion should update descripcion`() = runTest {
        // Given: Test description
        val testDescription = "Reunión de equipo"
        
        // When: Description is updated
        viewModel.actualizarDescripcion(testDescription)
        
        // Then: Description should be updated
        assertEquals(testDescription, viewModel.descripcion.value)
    }

    @Test
    fun `test seleccionarUsuario should add user to selected users`() = runTest {
        // Given: User in suggested users
        val testUser = Usuario("1", "Juan", "Pérez", "usuario", "123456")
        viewModel.actualizarNombreUsuario("Juan")
        // Simular que el usuario está en la lista de sugeridos
        val field = AgregarCalendarioViewModel::class.java.getDeclaredField("_usuariosSugeridos")
        field.isAccessible = true
        field.set(viewModel, kotlinx.coroutines.flow.MutableStateFlow(listOf(testUser)))
        
        // When: User is selected
        viewModel.seleccionarUsuario(0)
        
        // Then: User should be added to selected users
        assertTrue(viewModel.usuariosSeleccionados.value.contains(testUser))
    }

    @Test
    fun `test eliminarUsuario should remove user from selected users`() = runTest {
        // Given: User in selected users
        val testUser = Usuario("1", "Juan", "Pérez", "usuario", "123456")
        viewModel.usuariosSeleccionados.value = listOf(testUser)
        
        // When: User is removed
        viewModel.eliminarUsuario(0)
        
        // Then: User should be removed from selected users
        assertTrue(viewModel.usuariosSeleccionados.value.isEmpty())
    }

    @Test
    fun `test seleccionarFecha should add date to selected dates`() = runTest {
        // Given: Test date
        val testDate = "2024-01-15"
        
        // When: Date is selected
        viewModel.seleccionarFecha(testDate)
        
        // Then: Date should be added to selected dates
        assertTrue(viewModel.fechasSeleccionadas.value.contains(testDate))
    }

    @Test
    fun `test seleccionarFecha should remove date if already selected`() = runTest {
        // Given: Date already selected
        val testDate = "2024-01-15"
        viewModel.fechasSeleccionadas.value = listOf(testDate)
        
        // When: Date is selected again
        viewModel.seleccionarFecha(testDate)
        
        // Then: Date should be removed from selected dates
        assertFalse(viewModel.fechasSeleccionadas.value.contains(testDate))
    }

    @Test
    fun `test limpiarCampos should reset all fields`() = runTest {
        // Given: Fields with values
        viewModel.actualizarNombreUsuario("Juan")
        viewModel.actualizarHoraInicio("09:00")
        viewModel.actualizarHoraFin("10:00")
        viewModel.actualizarUbicacion("Oficina 101")
        viewModel.actualizarDescripcion("Reunión")
        viewModel.seleccionarFecha("2024-01-15")
        
        // When: Fields are cleared
        viewModel.limpiarCampos()
        
        // Then: All fields should be reset
        assertEquals("", viewModel.nombreUsuario.value)
        assertEquals("00:00", viewModel.horaInicio.value)
        assertEquals("00:00", viewModel.horaFin.value)
        assertEquals("", viewModel.ubicacion.value)
        assertEquals("", viewModel.descripcion.value)
        assertTrue(viewModel.fechasSeleccionadas.value.isEmpty())
        assertTrue(viewModel.usuariosSeleccionados.value.isEmpty())
    }

    @Test
    fun `test guardarCalendario should show error when nombreUsuario is blank`() = runTest {
        // Given: Empty user name
        
        // When: Calendar is saved
        viewModel.guardarCalendario()
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then: Error message should be shown
        assertEquals("El nombre del usuario es obligatorio", viewModel.mensajeError.value)
    }

    @Test
    fun `test guardarCalendario should show error when no users selected`() = runTest {
        // Given: User name but no users selected
        viewModel.actualizarNombreUsuario("Juan")
        
        // When: Calendar is saved
        viewModel.guardarCalendario()
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then: Error message should be shown
        assertEquals("Debe seleccionar al menos un usuario", viewModel.mensajeError.value)
    }

    @Test
    fun `test guardarCalendario should show error when times not set`() = runTest {
        // Given: User name and users selected but no times
        viewModel.actualizarNombreUsuario("Juan")
        val testUser = Usuario("1", "Juan", "Pérez", "usuario", "123456")
        viewModel.usuariosSeleccionados.value = listOf(testUser)
        
        // When: Calendar is saved
        viewModel.guardarCalendario()
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then: Error message should be shown
        assertEquals("Debe seleccionar una hora de inicio y fin", viewModel.mensajeError.value)
    }

    @Test
    fun `test guardarCalendario should show error when no dates selected`() = runTest {
        // Given: All fields set except dates
        viewModel.actualizarNombreUsuario("Juan")
        val testUser = Usuario("1", "Juan", "Pérez", "usuario", "123456")
        viewModel.usuariosSeleccionados.value = listOf(testUser)
        viewModel.actualizarHoraInicio("09:00")
        viewModel.actualizarHoraFin("10:00")
        viewModel.actualizarUbicacion("Oficina 101")
        viewModel.actualizarDescripcion("Reunión")
        
        // When: Calendar is saved
        viewModel.guardarCalendario()
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then: Error message should be shown
        assertEquals("Debe seleccionar al menos una fecha", viewModel.mensajeError.value)
    }

    @Test
    fun `test guardarCalendario should show error when ubicacion is blank`() = runTest {
        // Given: All fields set except location
        viewModel.actualizarNombreUsuario("Juan")
        val testUser = Usuario("1", "Juan", "Pérez", "usuario", "123456")
        viewModel.usuariosSeleccionados.value = listOf(testUser)
        viewModel.actualizarHoraInicio("09:00")
        viewModel.actualizarHoraFin("10:00")
        viewModel.actualizarDescripcion("Reunión")
        viewModel.seleccionarFecha("2024-01-15")
        
        // When: Calendar is saved
        viewModel.guardarCalendario()
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then: Error message should be shown
        assertEquals("La ubicación es obligatoria", viewModel.mensajeError.value)
    }

    @Test
    fun `test guardarCalendario should show error when descripcion is blank`() = runTest {
        // Given: All fields set except description
        viewModel.actualizarNombreUsuario("Juan")
        val testUser = Usuario("1", "Juan", "Pérez", "usuario", "123456")
        viewModel.usuariosSeleccionados.value = listOf(testUser)
        viewModel.actualizarHoraInicio("09:00")
        viewModel.actualizarHoraFin("10:00")
        viewModel.actualizarUbicacion("Oficina 101")
        viewModel.seleccionarFecha("2024-01-15")
        
        // When: Calendar is saved
        viewModel.guardarCalendario()
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then: Error message should be shown
        assertEquals("La descripción es obligatoria", viewModel.mensajeError.value)
    }

    @Test
    fun `test limpiarMensajes should clear error and success messages`() = runTest {
        // Given: Error and success messages set
        viewModel.mensajeError.value = "Error test"
        viewModel.mensajeExito.value = "Success test"
        viewModel.mostrarErrores.value = true
        
        // When: Messages are cleared
        viewModel.limpiarMensajes()
        
        // Then: Messages should be cleared
        assertNull(viewModel.mensajeError.value)
        assertNull(viewModel.mensajeExito.value)
        assertFalse(viewModel.mostrarErrores.value)
    }
} 