package com.example.calendapp.editar_usuario

data class EditUserModel(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val nombre: String = "",
    val apellido: String = "",
    val telefono: String = "",
    val genero: String = "",
    val rol: String = ""
)

data class EditUserState(
    val user: EditUserModel = EditUserModel(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
) 