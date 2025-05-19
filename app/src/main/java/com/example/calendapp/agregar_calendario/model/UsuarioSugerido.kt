package com.example.calendapp.agregar_calendario.model

data class UsuarioSugerido(
    val id: Int = 0,
    val nombre: String,
    val email: String = "",
    val foto: String = ""
)

// Lista de ejemplo de usuarios sugeridos
object UsuariosSugeridosEjemplo {
    val lista = listOf(
        UsuarioSugerido(1, "Juan Pérez", "juan@email.com"),
        UsuarioSugerido(2, "María García", "maria@email.com"),
        UsuarioSugerido(3, "Carlos López", "carlos@email.com"),
        UsuarioSugerido(4, "Ana Martínez", "ana@email.com"),
        UsuarioSugerido(5, "Pedro Sánchez", "pedro@email.com")
    )
} 