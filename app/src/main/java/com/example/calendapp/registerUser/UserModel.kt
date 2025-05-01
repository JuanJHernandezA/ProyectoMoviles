package com.example.calendapp.registerUser

data class UserModel(
    val cedula: String = "",
    val nombre: String = "",
    val apellido: String = "",
    val genero: String = "",
    val edad: String = "",
    val telefono: String = "",
    val rol: String = "",
    val correo: String = "",
    val contrasena: String = ""
)

data class UserState(
    val user: UserModel = UserModel(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
) 