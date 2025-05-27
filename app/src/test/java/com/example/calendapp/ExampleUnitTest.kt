package com.example.calendapp

import com.example.calendapp.registerUser.UserModel
import com.example.calendapp.agregar_calendario.model.Calendario
import com.example.calendapp.agregar_calendario.model.Usuario
import org.junit.Test
import org.junit.Assert.*

class ExampleUnitTest {

    @Test
    fun `test user registration with valid data`() {
        // Arrange: Datos de prueba
        val user = UserModel(
            cedula = "123456789",
            nombre = "Juan",
            apellido = "Pérez",
            genero = "Masculino",
            edad = "30",
            telefono = "3001234567",
            rol = "Administrador",
            correo = "juan.perez@example.com",
            contrasena = "password123"
        )

        // Act: Verificar que los datos del usuario sean correctos
        // Assert: Comprobaciones
        assertEquals("123456789", user.cedula)
        assertEquals("Juan", user.nombre)
        assertEquals("Pérez", user.apellido)
        assertEquals("Masculino", user.genero)
        assertEquals("30", user.edad)
        assertEquals("3001234567", user.telefono)
        assertEquals("Administrador", user.rol)
        assertEquals("juan.perez@example.com", user.correo)
        assertEquals("password123", user.contrasena)
    }

    @Test
    fun `test user registration with empty fields`() {
        // Arrange: Crear un nuevo usuario con campos vacíos
        val user = UserModel()

        // Act & Assert: Verificar que los campos estén vacíos
        assertEquals("", user.cedula)
        assertEquals("", user.nombre)
        assertEquals("", user.apellido)
        assertEquals("", user.genero)
        assertEquals("", user.edad)
        assertEquals("", user.telefono)
        assertEquals("", user.rol)
        assertEquals("", user.correo)
        assertEquals("", user.contrasena)
    }

    @Test
    fun `test user edit with valid data`() {
        // Arrange: Datos de prueba para edición
        val user = UserModel(
            cedula = "123456789",
            nombre = "Juan",
            apellido = "Pérez",
            genero = "Masculino",
            edad = "30",
            telefono = "3001234567",
            rol = "Administrador",
            correo = "juan.perez@example.com",
            contrasena = "password123"
        )

        // Act: Modificar algunos campos
        val updatedUser = user.copy(
            nombre = "Juan Carlos",
            telefono = "3009876543",
            contrasena = "newPassword123"
        )

        // Assert: Verificar que los cambios se aplicaron correctamente
        assertEquals("Juan Carlos", updatedUser.nombre)
        assertEquals("3009876543", updatedUser.telefono)
        assertEquals("newPassword123", updatedUser.contrasena)
        // Verificar que otros campos no cambiaron
        assertEquals("123456789", updatedUser.cedula)
        assertEquals("Pérez", updatedUser.apellido)
        assertEquals("Masculino", updatedUser.genero)
        assertEquals("30", updatedUser.edad)
        assertEquals("Administrador", updatedUser.rol)
        assertEquals("juan.perez@example.com", updatedUser.correo)
    }

    @Test
    fun `test agregar calendario con datos validos`() {
        // Arrange: Crear un calendario con datos válidos
        val calendario = Calendario(
            horaInicio = "09:00",
            horaFin = "10:00",
            ubicacion = "Sala de conferencias",
            descripcion = "Reunión semanal de planificación",
            usuarios = listOf(
                Usuario(
                    id = "1",
                    nombre = "Juan",
                    apellido = "Pérez",
                    rol = "usuario",
                    cedula = "123456789"
                )
            ),
            fechas = listOf("2024-03-20")
        )

        // Assert: Verificar que los datos del calendario sean correctos
        assertEquals("09:00", calendario.horaInicio)
        assertEquals("10:00", calendario.horaFin)
        assertEquals("Sala de conferencias", calendario.ubicacion)
        assertEquals("Reunión semanal de planificación", calendario.descripcion)
        assertEquals(1, calendario.usuarios.size)
        assertEquals("Juan", calendario.usuarios[0].nombre)
        assertEquals("Pérez", calendario.usuarios[0].apellido)
        assertEquals(1, calendario.fechas.size)
        assertEquals("2024-03-20", calendario.fechas[0])
    }

    @Test
    fun `test validacion de horas en calendario`() {
        // Arrange: Crear un calendario con horas inválidas
        val calendario = Calendario(
            horaInicio = "10:00",
            horaFin = "09:00" // Hora de fin anterior a la hora de inicio
        )

        // Assert: Verificar que la hora de fin es anterior a la hora de inicio
        assertTrue(calendario.horaFin < calendario.horaInicio)
    }

    @Test
    fun `test calendario con campos vacios`() {
        // Arrange: Crear un calendario con campos vacíos
        val calendario = Calendario()

        // Assert: Verificar que los campos estén vacíos
        assertEquals("", calendario.nombre)
        assertEquals("", calendario.horaInicio)
        assertEquals("", calendario.horaFin)
        assertEquals("", calendario.ubicacion)
        assertEquals("", calendario.descripcion)
        assertTrue(calendario.usuarios.isEmpty())
        assertTrue(calendario.fechas.isEmpty())
    }
}
