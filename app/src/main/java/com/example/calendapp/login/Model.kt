package com.example.calendapp.login

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false,
    val userRole: String? = null,
    val userName: String? = null,
    val cedula: String? = null
)
