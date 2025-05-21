package com.example.calendapp

import com.example.calendapp.registerUser.UserModel
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



    
}
